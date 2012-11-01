package com.easyinsight.users;

import com.easyinsight.config.ConfigLoader;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.html.RedirectUtil;
import com.easyinsight.preferences.ApplicationSkin;
import com.easyinsight.preferences.ApplicationSkinSettings;
import com.easyinsight.preferences.PreferencesService;
import com.easyinsight.preferences.UISettingRetrieval;
import com.easyinsight.salesautomation.SalesEmail;
import com.easyinsight.security.*;
import com.easyinsight.logging.LogClass;
import com.easyinsight.email.UserStub;
import com.easyinsight.email.AccountMemberInvitation;
import com.easyinsight.security.SecurityException;
import com.easyinsight.util.RandomTextGenerator;
import com.easyinsight.groups.Group;
import com.easyinsight.groups.GroupStorage;
import com.easyinsight.billing.BrainTreeBillingSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import flex.messaging.FlexContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * User: jboe
 * Date: Jan 2, 2008
 * Time: 5:34:56 PM
 */
public class UserService {

    public static void checkAccountStateOnLogin(HttpSession session, UserServiceResponse userServiceResponse, HttpServletRequest request, HttpServletResponse response, String oldRedirectUrl) throws IOException, SQLException {
        if (userServiceResponse.getAccountState() == Account.CLOSED) {
            response.sendRedirect(RedirectUtil.getURL(request, "/app/billing/billingSetupAction.jsp"));
        } else if (userServiceResponse.getAccountState() == Account.DELINQUENT) {
            response.sendRedirect(RedirectUtil.getURL(request,"/app/billing/billingSetupAction.jsp"));
        } else if (userServiceResponse.getAccountState() == Account.BILLING_FAILED) {
            response.sendRedirect(RedirectUtil.getURL(request,"/app/billing/billingSetupAction.jsp"));
        } else if (userServiceResponse.getAccountState() == Account.INACTIVE) {
            response.sendRedirect(RedirectUtil.getURL(request, "/app/activation/reactivate.jsp"));
        } else if (userServiceResponse.getAccountState() == Account.REACTIVATION_POSSIBLE) {
            response.sendRedirect(RedirectUtil.getURL(request, "/app/reactivate"));
        } else {
            String urlHash = request.getParameter("urlhash");
            String rememberMe = request.getParameter("rememberMeCheckbox");
            if ("on".equals(rememberMe)) {
                Cookie userNameCookie = new Cookie("eiUserName", userServiceResponse.getUserName());
                userNameCookie.setSecure(true);
                userNameCookie.setMaxAge(60 * 60 * 24 * 30);
                response.addCookie(userNameCookie);
                Cookie tokenCookie = new Cookie("eiRememberMe", new InternalUserService().createCookie(userServiceResponse.getUserID()));
                tokenCookie.setSecure(true);
                tokenCookie.setMaxAge(60 * 60 * 24 * 30);
                response.addCookie(tokenCookie);
            }
            if (userServiceResponse.isFirstLogin()) {
                response.sendRedirect(RedirectUtil.getURL(request, "/app/user/initialUserSetup.jsp"));
            } else {
                session.removeAttribute("loginRedirect");
                String redirectUrl = RedirectUtil.getURL(request, "/app/");
                //System.out.println("Redirect url = " + oldRedirectUrl);
                if(oldRedirectUrl != null) {
                   redirectUrl = oldRedirectUrl;
                }
                if(urlHash != null)
                   redirectUrl = redirectUrl + urlHash;
                response.sendRedirect(redirectUrl);
            }
        }
    }

    public void dismissNews() {
        EIConnection conn = Database.instance().getConnection();
        try {
            long userID = SecurityUtil.getUserID();
            PreparedStatement stmt = conn.prepareStatement("UPDATE USER SET NEWS_DISMISS_DATE = ? WHERE USER_ID = ?");
            stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            stmt.setLong(2, userID);
            stmt.executeUpdate();
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public TimerResponse runTimer(TimerRequest timerRequest) {
        boolean rerun = false;
        try {
            if (timerRequest.getDataSourceID() > 0 && timerRequest.getDate() != null) {
                EIConnection conn = Database.instance().getConnection();
                try {
                    PreparedStatement stmt = conn.prepareStatement("SELECT refresh_time FROM data_source_refresh_log WHERE data_source_id = ?");
                    stmt.setLong(1, timerRequest.getDataSourceID());
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        Date refreshTime = rs.getTimestamp(1);
                        if (timerRequest.getDate().compareTo(refreshTime) < 0) {
                            rerun = true;
                        }
                    }
                } finally {
                    Database.closeConnection(conn);
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return new TimerResponse(rerun, getBuildPath());
    }

    public BasicInfo getBuildPath() {
        try {
            URL url = getClass().getClassLoader().getResource("version.properties");
            Properties properties = new Properties();
            FileInputStream fis = new FileInputStream(new File(url.getFile()));
            properties.load(fis);
            fis.close();
            BasicInfo basicInfo = new BasicInfo();
            basicInfo.setVersion((String) properties.get("ei.version"));
            if (ConfigLoader.instance().isProduction()) {
                basicInfo.setPrefix("https://www.easy-insight.com");
            } else {
                basicInfo.setPrefix("");
            }
            return basicInfo;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private void createAccountActivation(EIConnection conn, long accountID, String activationKey, String targetURL) throws SQLException {
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO ACCOUNT_ACTIVATION (ACCOUNT_ID, ACTIVATION_KEY, CREATION_DATE, TARGET_URL) VALUES (?, ?, ?, ?)");
        insertStmt.setLong(1, accountID);
        insertStmt.setString(2, activationKey);
        insertStmt.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));
        insertStmt.setString(4, targetURL);
        insertStmt.execute();
    }

    public void reactivate() {
        long accountID = SecurityUtil.getAccountID();
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            Account account = (Account) session.createQuery("from Account where accountID = ?").setLong(0, accountID).list().get(0);
            if (account.getAccountState() == Account.REACTIVATION_POSSIBLE) {
                account.setAccountState(Account.TRIAL);
                PreparedStatement nukeTimedState = conn.prepareStatement("DELETE FROM ACCOUNT_TIMED_STATE WHERE ACCOUNT_ID = ?");
                nukeTimedState.setLong(1, accountID);
                nukeTimedState.executeUpdate();
                PreparedStatement addTimedState = conn.prepareStatement("INSERT INTO ACCOUNT_TIMED_STATE (ACCOUNT_ID, ACCOUNT_STATE, STATE_CHANGE_TIME) VALUES (?, ?, ?)");
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, 30);
                addTimedState.setLong(1, accountID);
                addTimedState.setInt(2, Account.ACTIVE);
                addTimedState.setTimestamp(3, new java.sql.Timestamp(cal.getTimeInMillis()));
                addTimedState.execute();
                session.update(account);
                new AccountActivityStorage().generateSalesEmailSchedules(SecurityUtil.getUserID(), conn);
            } else {
                throw new RuntimeException("Attempt to reactivate an account not in reactivation possible state");
            }
            session.flush();
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
            session.close();
            Database.closeConnection(conn);
        }
    }

    public AccountSetupData applySetupData(AccountSetupData accountSetupData) {
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            User admin = (User) session.createQuery("from User where userID = ?").setLong(0, SecurityUtil.getUserID()).list().get(0);
            Account account = admin.getAccount();
            boolean tooManyUsers = false;
            if (account.getMaxUsers() < account.getUsers().size() + accountSetupData.getUsers().size()) {
                tooManyUsers = true;
            }
            boolean problem = isProblem(accountSetupData, conn);
            if (!problem && !tooManyUsers) {
                account.setDateFormat(accountSetupData.getDateFormat());
                Map<Integer, Long> personaMap = new HashMap<Integer, Long>();
                personaMap.put(AccountSetupData.BIZ_USER, null);
                personaMap.put(AccountSetupData.DEVELOPER, null);
                personaMap.put(AccountSetupData.BI_GURU, null);
                PreparedStatement personasStmt = conn.prepareStatement("SELECT PERSONA_ID, PERSONA_NAME FROM PERSONA WHERE PERSONA.account_id = ?");
                personasStmt.setLong(1, SecurityUtil.getAccountID());
                ResultSet personaRS = personasStmt.executeQuery();
                while (personaRS.next()) {
                    long personaID = personaRS.getLong(1);
                    String personaName = personaRS.getString(2);
                    if ("Business User".equals(personaName)) {
                        personaMap.put(AccountSetupData.BIZ_USER, personaID);
                    } else if ("Developer".equals(personaName)) {
                        personaMap.put(AccountSetupData.DEVELOPER, personaID);
                    } else if ("BI Guru".equals(personaName)) {
                        personaMap.put(AccountSetupData.BI_GURU, personaID);
                    }
                }
                if (personaMap.get(AccountSetupData.BIZ_USER) == null) {
                    personaMap.put(AccountSetupData.BIZ_USER, new PreferencesService().savePersona(accountSetupData.getPersonas().get(0), conn));
                }
                if (personaMap.get(AccountSetupData.DEVELOPER) == null) {
                    personaMap.put(AccountSetupData.DEVELOPER, new PreferencesService().savePersona(accountSetupData.getPersonas().get(1), conn));
                }
                if (personaMap.get(AccountSetupData.BI_GURU) == null) {
                    personaMap.put(AccountSetupData.BI_GURU, new PreferencesService().savePersona(accountSetupData.getPersonas().get(2), conn));
                }
                PreparedStatement updateUserStmt = conn.prepareStatement("UPDATE USER SET PERSONA_ID = ?, INITIAL_SETUP_DONE = ? WHERE USER_ID = ?");
                updateUserStmt.setLong(1, personaMap.get(accountSetupData.getMyPersona()));
                updateUserStmt.setBoolean(2, true);
                updateUserStmt.setLong(3, SecurityUtil.getUserID());
                updateUserStmt.executeUpdate();
                for (UserPersonaObject user : accountSetupData.getUsers()) {
                    user.setPersonaID(personaMap.get(user.getPersona()));
                }
                //bulkCreateUser(accountSetupData.getUsers(), account, admin, conn);
                session.update(account);
                session.flush();
                conn.commit();
            } else {
                return accountSetupData;
            }

            return null;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    /*private void bulkCreateUser(List<UserPersonaObject> users, Account account, User admin, EIConnection conn) throws SQLException {
        for (UserTransferObject userTransferObject : users) {
            User user = userTransferObject.toUser();
            user.setAccount(account);
            final String adminFirstName = admin.getFirstName();
            final String adminName = admin.getName();
            final String userEmail = user.getEmail();
            final String userName = user.getUserName();
            final String password = RandomTextGenerator.generateText(12);
            user.setPassword(PasswordService.getInstance().encrypt(password, user.getHashSalt(), user.getHashType()));
            account.addUser(user);
            user.setAccount(account);
            new Thread(new Runnable() {
                public void run() {
                    new AccountMemberInvitation().sendAccountEmail(userEmail, adminFirstName, adminName, userName, password);
                }
            }).start();
            if (account.getAccountType() != Account.PERSONAL) {
                if (account.getGroupID() != null) {
                    new GroupStorage().addUserToGroup(user.getUserID(), account.getGroupID(), userTransferObject.isAccountAdmin() ? Roles.OWNER : Roles.SUBSCRIBER, conn);
                }
            }
        }
    }*/

    private boolean isProblem(AccountSetupData accountSetupData, EIConnection conn) throws SQLException {
        boolean problem = false;
        PreparedStatement queryStmt = conn.prepareStatement("SELECT EMAIL FROM USER WHERE EMAIL = ?");
        for (UserPersonaObject user : accountSetupData.getUsers()) {
            queryStmt.setString(1, user.getEmail());
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                problem = true;
                user.setBadEmail(true);
            }
        }
        PreparedStatement userNameStmt = conn.prepareStatement("SELECT USERNAME FROM USER WHERE USERNAME = ?");
        for (UserPersonaObject user : accountSetupData.getUsers()) {
            userNameStmt.setString(1, user.getUserName());
            ResultSet rs = userNameStmt.executeQuery();
            if (rs.next()) {
                problem = true;
                user.setBadUserName(true);
            }
        }
        return problem;
    }

    public boolean verifyPasswordReset(String passwordResetString) {
        EIConnection conn = Database.instance().getConnection();
        boolean success = false;
        try {
            conn.setAutoCommit(false);
            PreparedStatement stmt = conn.prepareStatement("select password_request_string from password_reset where password_request_string = ? and request_date > ?");
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.setTimeInMillis(c.getTimeInMillis() - 172800000L);
            stmt.setString(1, passwordResetString);
            stmt.setDate(2, new java.sql.Date(c.getTimeInMillis()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getString(1).equals(passwordResetString))
                success = true;
            stmt.close();
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return success;
    }

    public boolean resetPassword(String passwordResetValidation, String username, String password) {
        boolean success = false;
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            Session s = Database.instance().createSession(conn);
            PreparedStatement stmt = conn.prepareStatement("select user_id, password_request_string from password_reset where password_request_string = ? and request_date > ?");
            Calendar c = Calendar.getInstance();

            c.setTime(new Date());
            c.setTimeInMillis(c.getTimeInMillis() - 86400000L);
            stmt.setString(1, passwordResetValidation);
            stmt.setDate(2, new java.sql.Date(c.getTimeInMillis()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String verifiedResetString = rs.getString(2);
                if (passwordResetValidation.equals(verifiedResetString)) {
                    List l = s.createQuery("from User where userName = ? and userID = ?").setString(0, username).setLong(1, rs.getLong(1)).list();
                    if (l.size() == 1) {
                        User u = (User) l.get(0);
                        u.setPassword(PasswordService.getInstance().encrypt(password, u.getHashSalt(), u.getHashType()));
                        s.update(u);
                        success = true;
                        PreparedStatement deleteStatement = conn.prepareStatement("delete from password_reset where password_request_string = ?");
                        deleteStatement.setString(1, passwordResetValidation);
                        deleteStatement.executeUpdate();
                        break;
                    } else if (l.size() == 0) {
                        l = s.createQuery("from User where email = ? and userID = ?").setString(0, username).setLong(1, rs.getLong(1)).list();
                        if (l.size() == 1) {
                            User u = (User) l.get(0);
                            u.setPassword(PasswordService.getInstance().encrypt(password, u.getHashSalt(), u.getHashType()));
                            s.update(u);
                            success = true;
                            PreparedStatement deleteStatement = conn.prepareStatement("delete from password_reset where password_request_string = ?");
                            deleteStatement.setString(1, passwordResetValidation);
                            deleteStatement.executeUpdate();
                            break;
                        }
                    }
                }
            }

            stmt.close();
            s.flush();
            conn.commit();
            s.close();
        } catch (Exception e) {
            conn.rollback();
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }

        return success;
    }

    public void cancelPaidAccount() {
        long accountID = SecurityUtil.getAccountID();
        Session session = Database.instance().createSession();
        try {
            cancelPaidAccount(accountID, session);
        } catch (Exception e) {
            session.getTransaction().rollback();
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public void cancelPaidAccount(String username) {
        long accountID = SecurityUtil.getAccountID();
        Session session = Database.instance().createSession();
        try {
            User user = (User) session.createQuery("from User where userName = ?").setString(0, username).list().get(0);
            cancelPaidAccount(user.getAccount().getAccountID(), session);
        } catch (Exception e) {
            session.getTransaction().rollback();
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public void cancelPaidAccount(long accountID, Session session) {
        session.getTransaction().begin();
        Account a = (Account) session.createQuery("from Account where accountID  = ?").setLong(0, accountID).list().get(0);
        cancelAccount(session, a);
        session.getTransaction().commit();
    }

    public void cancelSnappCloudAccount(String snappCloudId) {
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            Account a = (Account) session.createQuery("from Account where snappCloudId = ?").setString(0, snappCloudId).list().get(0);
            cancelAccount(session, a);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    private void cancelAccount(Session session, Account a) {
        a.setAccountState(Account.CLOSING);
        a.setBillingInformationGiven(false);
        BrainTreeBillingSystem billingSystem = new BrainTreeBillingSystem();
        billingSystem.setUsername(ConfigLoader.instance().getBillingUsername());
        billingSystem.setPassword(ConfigLoader.instance().getBillingPassword());
        billingSystem.cancelPlan(a.getAccountID());
        session.save(a);
    }

    public void cancelFreeAccount() {
        long accountID = SecurityUtil.getAccountID();
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            Account a = (Account) session.createQuery("from Account where accountID  = ?").setLong(0, accountID).list().get(0);
            a.setAccountState(Account.CLOSED);
            session.save(a);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public void salesRequest(String userName, String email, String company, String additionalInfo) {
        try {
            new AccountMemberInvitation().salesNotification(userName, email, company, additionalInfo);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public boolean remindPassword(String emailAddress) {
        boolean success;
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            List results = session.createQuery("from User where email = ?").setString(0, emailAddress).list();
            if (results.size() == 0) {
                success = false;
            } else {
                User user = (User) results.get(0);
                String passwordPrefix = RandomTextGenerator.generateText(20);

                PreparedStatement updateStatement = conn.prepareStatement("update password_reset set request_date = ?, password_request_string = ? where user_id = ?");
                updateStatement.setDate(1, new java.sql.Date(new Date().getTime()));
                updateStatement.setString(2, passwordPrefix);
                updateStatement.setLong(3, user.getUserID());


                PreparedStatement insertStatement = conn.prepareStatement("insert into password_reset(user_id, request_date, password_request_string) values (?,?,?)");
                insertStatement.setLong(1, user.getUserID());
                insertStatement.setDate(2, new java.sql.Date(new Date().getTime()));
                insertStatement.setString(3, passwordPrefix);

                if (updateStatement.executeUpdate() == 0)
                    insertStatement.execute();

                new AccountMemberInvitation().resetPassword(emailAddress, passwordPrefix);
                success = true;
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return success;
    }

    public boolean remindUserName(String emailAddress) {
        boolean success;
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from User where email = ?").setString(0, emailAddress).list();
            if (results.size() == 0) {
                success = false;
            } else {
                User user = (User) results.get(0);
                new AccountMemberInvitation().remindUserName(emailAddress, user.getUserName());
                success = true;
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return success;
    }


    @Nullable
    public String doesUserExist(String userName, String email, String accountName) {
        Session session = Database.instance().createSession();
        String message = null;
        List results;
        try {
            session.beginTransaction();
            results = session.createQuery("from User where userName = ?").setString(0, userName).list();
            if (results.size() > 0) {
                message = "A user already exists by that name.";
            } else {
                results = session.createQuery("from User where email = ?").setString(0, email).list();
                if (results.size() > 0) {
                    message = "That email address is already used.";
                } else {
                    results = session.createQuery("from Account where name = ?").setString(0, accountName).list();
                    if (results.size() > 0) {
                        message = "That company name is already used.";
                    }
                }
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return message;
    }

    public boolean doesAccountExist(String accountName) {
        Session session = Database.instance().createSession();
        List results;
        try {
            session.beginTransaction();
            results = session.createQuery("from Account where name = ?").setString(0, accountName).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return (results.size() > 0);
    }

    private UserInfo retrieveUser(long userID) {
        try {
            User user = null;
            ApplicationSkin applicationSkin = null;
            EIConnection conn = Database.instance().getConnection();
            Session session = Database.instance().createSession(conn);
            List results;
            try {
                conn.setAutoCommit(false);
                results = session.createQuery("from User where userID = ?").setLong(0, userID).list();
                if (results.size() > 0) {
                    user = (User) results.get(0);
                    if (user.getPersonaID() != null) {
                        user.setUiSettings(UISettingRetrieval.getUISettings(user.getPersonaID(), conn, user.getAccount()));
                    }
                    user.setLastLoginDate(new Date());
                    session.update(user);
                    applicationSkin = ApplicationSkinSettings.retrieveSkin(userID, session, user.getAccount().getAccountID());
                }
                session.flush();
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new RuntimeException(e);
            } finally {
                conn.setAutoCommit(true);
                session.close();
                Database.closeConnection(conn);
            }
            UserInfo userInfo = new UserInfo();
            userInfo.user = user;
            userInfo.settings = applicationSkin;
            return userInfo;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private UserInfo retrieveUser() {
        long userID = SecurityUtil.getUserID();
        try {
            User user = null;
            ApplicationSkin applicationSkin = null;
            EIConnection conn = Database.instance().getConnection();
            Session session = Database.instance().createSession(conn);
            List results;
            try {
                conn.setAutoCommit(false);
                results = session.createQuery("from User where userID = ?").setLong(0, userID).list();
                if (results.size() > 0) {
                    user = (User) results.get(0);
                    if (user.getPersonaID() != null) {
                        user.setUiSettings(UISettingRetrieval.getUISettings(user.getPersonaID(), conn, user.getAccount()));
                    }
                    user.setLastLoginDate(new Date());
                    session.update(user);
                    applicationSkin = ApplicationSkinSettings.retrieveSkin(userID, session, user.getAccount().getAccountID());
                }
                session.flush();
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new RuntimeException(e);
            } finally {
                conn.setAutoCommit(true);
                session.close();
                Database.closeConnection(conn);
            }
            UserInfo userInfo = new UserInfo();
            userInfo.user = user;
            userInfo.settings = applicationSkin;
            return userInfo;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }/*

    public void associateToOpenID(String userSuppliedString) {
        ConsumerManager manager = new ConsumerManager();
        List discoveries = manager.discover(userSuppliedString);

        // attempt to associate with the OpenID provider
        // and retrieve one service endpoint for authentication
        DiscoveryInformation discovered = manager.associate(discoveries);

        // store the discovery information in the user's session for later use
        // leave out for stateless operation / if there is no session
        HttpSession session = FlexContext.getHttpRequest().getSession();
        session.setAttribute("openid-disc", discovered);

        // obtain a AuthRequest message to be sent to the OpenID provider
        AuthRequest authReq = manager.authenticate(discovered, "");
    }
*/

    public void updateUserLabels(String userName, String fullName, String email, String firstName, boolean optIn) {
        User user = retrieveUser().user;
        if (SecurityUtil.getAccountID() != user.getAccount().getAccountID()) {
            throw new SecurityException();
        }
        if (!SecurityUtil.isAccountAdmin() && (SecurityUtil.getUserID() != user.getUserID())) {
            throw new SecurityException();
        }
        user.setUserName(userName);
        user.setName(fullName);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setOptInEmail(optIn);
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            session.saveOrUpdate(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public String updatePassword(String password) {
        String error = UserService.checkPassword(password);
        if(error != null)
            return error;
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            User user = (User) session.createQuery("from User where userID = ?").setLong(0, SecurityUtil.getUserID()).list().get(0);
            if (user.isInitialSetupDone())
                throw new RuntimeException("You've already set your password!");
            String encryptedPassword = PasswordService.getInstance().encrypt(password, user.getHashSalt(), user.getHashType());
            user.setPassword(encryptedPassword);
            user.setInitialSetupDone(true);
            session.update(user);
            session.flush();
            session.getTransaction().commit();
            return null;
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public String updatePassword(String existingPassword, String password) {
        String error = checkPassword(password);
        if(error != null) {
            return error;
        }
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            User user = (User) session.createQuery("from User where userID = ?").setLong(0, SecurityUtil.getUserID()).list().get(0);
            String encryptedExistingPassword = PasswordService.getInstance().encrypt(existingPassword, user.getHashSalt(), user.getHashType());
            if (!encryptedExistingPassword.equals(user.getPassword())) {
                return "Your old password was not correct.";
            }
            String encryptedPassword = PasswordService.getInstance().encrypt(password, user.getHashSalt(), "SHA-256");
            user.setPassword(encryptedPassword);
            user.setHashType("SHA-256");
            session.update(user);
            session.getTransaction().commit();
            return null;
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public static String checkPassword(String password) {
        String errorString = null;
        Pattern digits = Pattern.compile("[0-9]");
        Pattern upperCase = Pattern.compile("[A-Z]");
        Pattern lowerCase = Pattern.compile("[a-z]");
        Pattern special = Pattern.compile("[^0-9A-Za-z]");
        List<Pattern> l = Arrays.asList(digits, upperCase, lowerCase, special);
        int count = 0;
        for (Pattern p : l) {
            Matcher m = p.matcher(password);
            if (m.find()) {
                count = count + 1;
            }
        }
        if (count < 2) {
            errorString = "You must use at least two of the following types of characters: Uppercase, lowercase, digits, and special characters.";
        } else if (password == null || "".equals(password.trim())) {
            errorString = "Please enter the new password.";
        } else if (password.length() < 8) {
            errorString = "Your password must be at least eight characters.";
        } else if (password.length() > 20) {
            errorString = "Your password must be less than twenty characters.";
        }
        return errorString;
    }

    public long createAccount(UserTransferObject userTransferObject, AccountTransferObject accountTransferObject, String password) {
        return createAccount(userTransferObject, accountTransferObject, password, null);
    }

    public long createAccount(UserTransferObject userTransferObject, AccountTransferObject accountTransferObject, String password, String sourceURL) {
        return createAccount(userTransferObject, accountTransferObject, password, sourceURL, Account.WEBSITE);
    }

    public long createAccount(UserTransferObject userTransferObject, AccountTransferObject accountTransferObject, String password, String sourceURL, int accountSource) {
        return createAccount(userTransferObject, accountTransferObject, password, sourceURL, accountSource, null);
    }

    public long createAccount(UserTransferObject userTransferObject, AccountTransferObject accountTransferObject, String password, String sourceURL, int accountSource, String salt) {
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            Account account = accountTransferObject.toAccount();
            account.setCreationDate(new Date());
            account.setAccountSource(accountSource);
            configureNewAccount(account);
            User user = createInitialUser(userTransferObject, password, account, salt);
            account.addUser(user);
            session.save(account);
            user.setAccount(account);
            session.update(user);

            Group group = new Group();
            group.setName(account.getName());
            group.setDescription("This group was automatically created to act as a location for exposing data to all users in the account.");
            account.setGroupID(new GroupStorage().addGroup(group, user.getUserID(), conn));
            session.update(account);

            String activationKey = RandomTextGenerator.generateText(20);
            if (account.getGoogleDomainName() == null) {

                if (sourceURL == null) {
                    sourceURL = "https://www.easy-insight.com/app";
                }
                createAccountActivation(conn, account.getAccountID(), activationKey, sourceURL);
            }
            //}
            session.flush();
            conn.commit();
            if (SecurityUtil.getSecurityProvider() instanceof DefaultSecurityProvider) {
                if (account.getGoogleDomainName() == null) {
                    new AccountMemberInvitation().sendActivationEmail(user.getEmail(), user.getFirstName(), activationKey);
                }
                new Thread(new SalesEmail(account, user)).start();
            }
            new AccountActivityStorage().generateSalesEmailSchedules(user.getUserID(), conn);
            return account.getAccountID();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void resendActivationEmail() {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement getEmailStmt = conn.prepareStatement("SELECT EMAIL, FIRST_NAME FROM USER WHERE USER_ID = ?");
            getEmailStmt.setLong(1, SecurityUtil.getUserID());
            ResultSet rs = getEmailStmt.executeQuery();
            rs.next();
            String email = rs.getString(1);
            String firstName = rs.getString(2);
            String activationKey = RandomTextGenerator.generateText(20);
            createAccountActivation(conn, SecurityUtil.getAccountID(), activationKey, "https://www.easy-insight.com/app");
            new AccountMemberInvitation().sendActivationEmail(email, firstName, activationKey);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private User createInitialUser(UserTransferObject userTransferObject, String password, Account account) {
        return createInitialUser(userTransferObject, password, account, null);
    }

    private User createInitialUser(UserTransferObject userTransferObject, String password, Account account, String salt) {
        User user = userTransferObject.toUser();
        user.setAccount(account);
        user.setHashSalt(salt);
        if (salt == null) {
            password = PasswordService.getInstance().encrypt(password, user.getHashSalt(), "SHA-256");
        }
        user.setPassword(password);
        user.setHashType("SHA-256");
        user.setUserKey(RandomTextGenerator.generateText(20));
        user.setUserSecretKey(RandomTextGenerator.generateText(20));
        return user;
    }

    private void configureNewAccount(Account account) {
        if (account.getGoogleDomainName() == null) {
            account.setAccountState(Account.INACTIVE);
        }
        AccountLimits.configureAccount(account);
    }

    public void deleteAccount() {
        long accountID = SecurityUtil.getAccountID();
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            Account account = (Account) session.createQuery("from Account where accountID = ?").setLong(0, accountID).list().get(0);
            session.delete(account);
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    private static class UserInfo {
        User user;
        ApplicationSkin settings;
        String personaName;
    }

    public LoginResponse isSessionLoggedIn() {
        UserPrincipal existing = (UserPrincipal) FlexContext.getFlexSession().getUserPrincipal();
        if (existing == null) {
            HttpSession session = FlexContext.getHttpRequest().getSession();
            if (session != null) {
                Long userID = (Long) session.getAttribute("userID");
                if (userID != null) {
                    String random = RandomTextGenerator.generateText(40);
                    session.setAttribute("establishID", random);
                    UserInfo userInfo = retrieveUserNoSecurity(userID);
                    User user = userInfo.user;
                    return new LoginResponse(random, String.valueOf(userID), UserServiceResponse.createResponseWithUISettings(user, userInfo.settings, userInfo.personaName));
                }
            }
            return null;
        } else {
            UserInfo userInfo = retrieveUser();
            User user = userInfo.user;
            return new LoginResponse(UserServiceResponse.createResponseWithUISettings(user, userInfo.settings, userInfo.personaName));
        }
    }

    public void switchHtmlFlex(boolean mode) {
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            User user = (User) session.createQuery("from User where userID = ?").setLong(0, SecurityUtil.getUserID()).list().get(0);
            user.setHtmlOrFlex(mode);
            session.update(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    private UserInfo retrieveUserNoSecurity(long userID) {
        try {
            User user = null;
            ApplicationSkin applicationSkin = null;
            EIConnection conn = Database.instance().getConnection();
            Session session = Database.instance().createSession(conn);
            List results;
            try {
                conn.setAutoCommit(false);
                results = session.createQuery("from User where userID = ?").setLong(0, userID).list();
                if (results.size() > 0) {
                    user = (User) results.get(0);
                    if (user.getPersonaID() != null) {
                        user.setUiSettings(UISettingRetrieval.getUISettings(user.getPersonaID(), conn, user.getAccount()));
                    }
                    user.setLastLoginDate(new Date());
                    session.update(user);
                    applicationSkin = ApplicationSkinSettings.retrieveSkin(userID, session, user.getAccount().getAccountID());
                }
                session.flush();
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new RuntimeException(e);
            } finally {
                conn.setAutoCommit(true);
                session.close();
                Database.closeConnection(conn);
            }
            UserInfo userInfo = new UserInfo();
            userInfo.user = user;
            userInfo.settings = applicationSkin;
            return userInfo;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public UserServiceResponse seleniumCheck(String userName, String password) {
        UserServiceResponse userServiceResponse = null;
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            PreparedStatement queryStmt = conn.prepareStatement("SELECT SELENIUM_REQUEST.USER_ID FROM SELENIUM_REQUEST WHERE SELENIUM_REQUEST.username = ? and " +
                    "selenium_request.password = ?");
            queryStmt.setString(1, userName);
            queryStmt.setString(2, password);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long userID = rs.getLong(1);
                List results = session.createQuery("from User where userID = ?").setLong(0, userID).list();
                if (results.size() > 0) {
                    User user = (User) results.get(0);
                    userServiceResponse = UserServiceResponse.createResponse(user, session, conn);
                } else {
                    userServiceResponse = null;
                }
            }
            session.flush();
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException();
        } finally {
            conn.setAutoCommit(true);
            session.close();
            Database.closeConnection(conn);
        }
        return userServiceResponse;
    }

    public UserServiceResponse htmlEstablish(String token, String userIDString) {
        EIConnection conn = Database.instance().getConnection();
        Session hibernateSession = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            UserServiceResponse userServiceResponse = null;
            HttpSession session = FlexContext.getHttpRequest().getSession();
            String establishID = (String) session.getAttribute("establishID");
            if (userIDString.equals(establishID)) {
                long userID = Long.parseLong(token);
                List results = hibernateSession.createQuery("from User where userID = ?").setLong(0, userID).list();
                if (results.size() > 0) {
                    User user = (User) results.get(0);
                    userServiceResponse = UserServiceResponse.createResponse(user, hibernateSession, conn);
                }
            }
            hibernateSession.flush();
            conn.commit();
            return userServiceResponse;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            hibernateSession.close();
            Database.closeConnection(conn);
        }
    }

    public UserServiceResponse sessionCookieCheck(String cookie, String userName, boolean clearCookie) {
        UserServiceResponse userServiceResponse = null;
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            PreparedStatement queryStmt = conn.prepareStatement("SELECT USER.USER_ID FROM USER_SESSION, USER WHERE USER_SESSION.user_id = user.user_id and " +
                    "user_session.session_number = ? AND user.username = ?");
            queryStmt.setString(1, cookie);
            queryStmt.setString(2, userName);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long userID = rs.getLong(1);
                List results = session.createQuery("from User where userID = ?").setLong(0, userID).list();
                if (results.size() > 0) {
                    User user = (User) results.get(0);
                    userServiceResponse = UserServiceResponse.createResponse(user, session, conn);
                    String sessionCookie = RandomTextGenerator.generateText(30);
                    userServiceResponse.setSessionCookie(sessionCookie);
                    user.setLastLoginDate(new Date());
                    PreparedStatement saveCookieStmt = conn.prepareStatement("INSERT INTO USER_SESSION (USER_ID, SESSION_NUMBER," +
                            "USER_SESSION_DATE) VALUES (?, ?, ?)");
                    saveCookieStmt.setLong(1, user.getUserID());
                    saveCookieStmt.setString(2, sessionCookie);
                    saveCookieStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                    saveCookieStmt.execute();
                    session.update(user);
                    if (clearCookie) {
                        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM USER_SESSION WHERE USER_ID = ? AND SESSION_NUMBER = ?");
                        clearStmt.setLong(1, user.getUserID());
                        clearStmt.setString(2, cookie);
                        clearStmt.executeUpdate();
                    }
                } else {
                    userServiceResponse = null;
                }
            }
            /**/
            session.flush();
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException();
        } finally {
            conn.setAutoCommit(true);
            session.close();
            Database.closeConnection(conn);
        }
        return userServiceResponse;
    }

    public UserStub getUserStub(String userName) {
        UserStub userStub = null;
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from User where userName = ?").setString(0, userName).list();
            if (results.size() > 0) {
                User user = (User) results.get(0);
                userStub = new UserStub(user.getUserID(), user.getUserName(), user.getEmail(), user.getName(), user.getAccount().getAccountID(), user.getFirstName());
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return userStub;
    }

    public boolean isAccountDelinquentOrClosed(String userName) {
        Session session = Database.instance().createSession();
        boolean delinquent = false;
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from User where userName = ?").setString(0, userName).list();
            if (results.size() > 0) {
                User user = (User) results.get(0);
                int state = user.getAccount().getAccountState();
                delinquent = (state == Account.DELINQUENT || state == Account.CLOSED || state == Account.BILLING_FAILED);
            }
        } finally {
            session.close();
        }
        return delinquent;
    }

    public UserServiceResponse authenticateWithEncrypted(String userName, String encryptedPassword) {
        UserServiceResponse userServiceResponse;
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        List results;
        try {
            conn.setAutoCommit(false);
            results = session.createQuery("from User where userName = ?").setString(0, userName).list();
            if (results.size() > 0) {
                User user = (User) results.get(0);
                String actualPassword = user.getPassword();
                if (PasswordService.getInstance().encrypt(encryptedPassword, user.getHashSalt(), user.getHashType()).equals(actualPassword)) {
                    userServiceResponse = UserServiceResponse.createResponse(user, session, conn);
                } else {
                    userServiceResponse = new UserServiceResponse(false, "Invalid username or password, please try again.");
                }
            } else {
                userServiceResponse = new UserServiceResponse(false, "Invalid username or password, please try again.");
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException();
        } finally {
            conn.setAutoCommit(true);
            session.close();
            Database.closeConnection(conn);
        }
        return userServiceResponse;
    }

    public List<Scenario> getScenarios() {
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            @SuppressWarnings({"unchecked"}) List<Scenario> scenarios = session.createQuery("from Scenario").list();
            session.getTransaction().commit();
            return scenarios;
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public Scenario saveScenario(Scenario scenario) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            if (scenario.getScenarioID() == 0) {
                scenario.setScenarioKey(RandomTextGenerator.generateText(15));
            }
            session.saveOrUpdate(scenario);
            session.getTransaction().commit();
            return scenario;
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public void deleteScenario(Scenario scenario) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            session.delete(scenario);
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public UserServiceResponse loginScenario(String scenarioKey) {
        UserServiceResponse userServiceResponse;
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            Scenario scenario = (Scenario) session.createQuery("from Scenario where scenarioKey = ?").setString(0, scenarioKey).list().get(0);
            User user = (User) session.createQuery("from User where userID = ?").setLong(0, scenario.getUserID()).list().get(0);
            userServiceResponse = UserServiceResponse.createResponse(user, session, conn);
            user.setLastLoginDate(new Date());
            session.update(user);
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            session.close();
            Database.closeConnection(conn);
        }
        return userServiceResponse;
    }

    @NotNull
    public UserServiceResponse authenticate(String userName, String password, boolean rememberMe) {

        UserServiceResponse userServiceResponse;
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        List results;
        try {
            conn.setAutoCommit(false);
            results = session.createQuery("from User where userName = ?").setString(0, userName).list();
            if (results.size() > 0) {
                User user = (User) results.get(0);
                userServiceResponse = getUser(password, session, user, conn, userName);
                if (rememberMe) {
                    String sessionCookie = RandomTextGenerator.generateText(30);
                    PreparedStatement saveCookieStmt = conn.prepareStatement("INSERT INTO USER_SESSION (USER_ID, SESSION_NUMBER," +
                            "USER_SESSION_DATE) VALUES (?, ?, ?)");
                    saveCookieStmt.setLong(1, user.getUserID());
                    saveCookieStmt.setString(2, sessionCookie);
                    saveCookieStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                    saveCookieStmt.execute();
                    //user.setSessionCookie(sessionCookie);
                    userServiceResponse.setSessionCookie(sessionCookie);
                    session.update(user);
                }
            } else {
                results = session.createQuery("from User where email = ?").setString(0, userName).list();
                if (results.size() > 0) {
                    User user = (User) results.get(0);
                    userServiceResponse = getUser(password, session, user, conn, userName);
                    if (rememberMe) {
                        String sessionCookie = RandomTextGenerator.generateText(30);
                        //user.setSessionCookie(sessionCookie);
                        userServiceResponse.setSessionCookie(sessionCookie);
                        session.update(user);
                    }
                } else {
                    userServiceResponse = new UserServiceResponse(false, "We didn't recognize the username or password you entered.");
                }
            }
            session.flush();
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            session.close();
            Database.closeConnection(conn);
        }
        return userServiceResponse;
    }

    public ExternalLogin determineSSO() {
        SecurityUtil.authorizeAccountAdmin();
        ExternalLogin externalLogin;
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            Account account = (Account) session.createQuery("from Account where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list().get(0);
            externalLogin = account.getExternalLogin();
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return externalLogin;
    }

    public void establishSSO(ExternalLogin externalLogin) {
        SecurityUtil.authorizeAccountAdmin();
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            Account account = (Account) session.createQuery("from Account where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list().get(0);
            account.setExternalLogin(externalLogin);
            session.update(account);
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    private UserServiceResponse getUser(String password, Session session, User user, EIConnection conn, String userName) throws SQLException {
        UserServiceResponse userServiceResponse;
        String actualPassword = user.getPassword();
        String encryptedPassword = PasswordService.getInstance().encrypt(password, user.getHashSalt(), user.getHashType());
        List accountResults = session.createQuery("from Account where accountID = ?").setLong(0, user.getAccount().getAccountID()).list();
        Account account = (Account) accountResults.get(0);
        if (encryptedPassword.equals(actualPassword)) {
            if (user.getUserKey() == null) {
                user.setUserKey(RandomTextGenerator.generateText(20));
            }
            if (user.getUserSecretKey() == null) {
                user.setUserSecretKey(RandomTextGenerator.generateText(20));
            }
            userServiceResponse = UserServiceResponse.createResponse(user, session, conn);
            user.setLastLoginDate(new Date());
            session.update(user);
        } else if (account.getExternalLogin() != null) {
            String result = account.getExternalLogin().login(userName, password);
            if (result == null) {
                session.update(account.getExternalLogin());
                userServiceResponse = UserServiceResponse.createResponse(user, session, conn);
                user.setLastLoginDate(new Date());
                session.update(user);
            } else {
                userServiceResponse = new UserServiceResponse(false, result);
            }
        } else {
            userServiceResponse = new UserServiceResponse(false, "We didn't recognize the username or password you entered.");
        }
        return userServiceResponse;
    }

    public AccountTransferObject retrieveAccount() {
        long accountID = SecurityUtil.getAccountID();
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            List results = session.createQuery("from Account where accountID = ?").setLong(0, accountID).list();
            Account account = (Account) results.get(0);
            AccountTransferObject accountTransferObject = account.toTransferObject();
            session.getTransaction().commit();
            return accountTransferObject;
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public List<SuggestedUser> suggestUsers(long dataSourceID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(dataSourceID, conn);
            List<SuggestedUser> users = feedDefinition.retrieveUsers(conn);
            long userID = SecurityUtil.getUserID();
            PreparedStatement stmt = conn.prepareStatement("SELECT EMAIL FROM USER WHERE USER_ID = ?");
            stmt.setLong(1, userID);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            String email = rs.getString(1);
            Iterator<SuggestedUser> iter = users.iterator();
            while (iter.hasNext()) {
                SuggestedUser suggestedUser = iter.next();
                if (email.equals(suggestedUser.getEmailAddress())) {
                    iter.remove();
                }
            }
            for (SuggestedUser user : users) {
                user.fillInInfo(conn);
            }
            return users;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public byte[] getLoginImage(String subdomain) {
        byte[] bytes = null;
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement queryStmt = conn.prepareStatement("SELECT IMAGE_BYTES FROM ACCOUNT LEFT JOIN USER_IMAGE ON ACCOUNT.login_image = USER_IMAGE.user_image_id where account.subdomain = ?");
            queryStmt.setString(1, subdomain);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next())
                bytes = rs.getBytes(1);
            conn.commit();
            return bytes;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void logAuthentication(String username, Long userId, boolean success, String ipAddress, String userAgent) {
        EIConnection conn = Database.instance().getConnection();
        try {

            conn.setAutoCommit(false);

            if(userId == null || userId == 0) {
                PreparedStatement usernameStatement = conn.prepareStatement("SELECT USER_ID FROM USER WHERE username = ?");
                usernameStatement.setString(1, username);
                ResultSet rs = usernameStatement.executeQuery();
                if(rs.next()) {
                    userId = rs.getLong(1);
                }
                usernameStatement.close();
                rs.close();
                if(userId == null || userId == 0) {
                    PreparedStatement emailStatement = conn.prepareStatement("SELECT USER_ID FROM USER WHERE email = ?");
                    emailStatement.setString(1, username);
                    ResultSet ers = emailStatement.executeQuery();
                    if(ers.next()) {
                        userId = ers.getLong(1);
                    }
                    emailStatement.close();
                    ers.close();

                    if(userId == null || userId == 0) {
                        PreparedStatement apiKeyStatement = conn.prepareStatement("SELECT USER_ID FROM USER WHERE user_key = ?");
                        apiKeyStatement.setString(1, username);
                        ResultSet ars = apiKeyStatement.executeQuery();
                        if(ars.next()) {
                            userId = ars.getLong(1);
                        }
                        apiKeyStatement.close();
                        ars.close();
                    }
                }
            }


            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO authentication_log(username, user_id, success, login_time, ip_address, login_type, user_agent) VALUES(?,?,?,?,?,?,?)");

            insertStmt.setString(1, username);
            if (userId == 0) {
                insertStmt.setNull(2, Types.BIGINT);
            } else {
                insertStmt.setLong(2, userId);
            }
            insertStmt.setBoolean(3, success);
            insertStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            insertStmt.setString(5, ipAddress);
            insertStmt.setInt(6, 1);
            insertStmt.setString(7, userAgent);


            insertStmt.execute();
            conn.commit();
            insertStmt.close();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }
}
