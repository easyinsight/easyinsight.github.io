package com.easyinsight.users;

import com.easyinsight.analysis.ReportTypeOptions;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
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
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import flex.messaging.FlexContext;


/**
 * User: jboe
 * Date: Jan 2, 2008
 * Time: 5:34:56 PM
 */
public class UserService {

    public String getBuildPath() {
        try {
            URL url = getClass().getClassLoader().getResource("version.properties");
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(url.getFile())));
            return (String) properties.get("ei.version");
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
                bulkCreateUser(accountSetupData.getUsers(), account, admin, conn);
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

    private void bulkCreateUser(List<UserPersonaObject> users, Account account, User admin, EIConnection conn) throws SQLException {
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
    }

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
            stmt.setString(1,passwordResetString);
            stmt.setDate(2, new java.sql.Date(c.getTimeInMillis()));
            ResultSet rs = stmt.executeQuery();
            if(rs.next() && rs.getString(1).equals(passwordResetString))
                success = true;
            stmt.close();
            conn.commit();
        } catch(Exception e){
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
            stmt.setString(1,passwordResetValidation);
            stmt.setDate(2, new java.sql.Date(c.getTimeInMillis()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String verifiedResetString = rs.getString(2);
                if (passwordResetValidation.equals(verifiedResetString)) {
                    List l = s.createQuery("from User where userName = ? and userID = ?").setString(0, username).setLong(1, rs.getLong(1)).list();
                    if(l.size() == 1) {
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
        } catch(Exception e) {
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
        } catch(Exception e) {
            session.getTransaction().rollback();
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        finally {
            session.close();
        }
    }

    public void cancelPaidAccount(String username) {
        long accountID = SecurityUtil.getAccountID();
        Session session = Database.instance().createSession();
        try {
            User user = (User) session.createQuery("from User where userName = ?").setString(0, username).list().get(0);
            cancelPaidAccount(user.getAccount().getAccountID(), session);
        } catch(Exception e) {
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
        } catch(Exception e) {
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
        billingSystem.setUsername("testapi");
        billingSystem.setPassword("password1");
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
        }
        catch(Exception e) {
            session.getTransaction().rollback();
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        finally {
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

                if(updateStatement.executeUpdate() == 0)
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
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            User user = (User) session.createQuery("from User where userID = ?").setLong(0, SecurityUtil.getUserID()).list().get(0);            
            String encryptedPassword = PasswordService.getInstance().encrypt(password, user.getHashSalt(), user.getHashType());
            user.setPassword(encryptedPassword);
            user.setInitialSetupDone(true);
            session.update(user);
            session.getTransaction().commit();
            return encryptedPassword;
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public String updatePassword(String existingPassword, String password) {
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            User user = (User) session.createQuery("from User where userID = ?").setLong(0, SecurityUtil.getUserID()).list().get(0);
            String encryptedExistingPassword = PasswordService.getInstance().encrypt(existingPassword, user.getHashSalt(), user.getHashType());
            if (!encryptedExistingPassword.equals(user.getPassword())) {
                return null;
            }
            String encryptedPassword = PasswordService.getInstance().encrypt(password, user.getHashSalt(), "SHA-256");
            user.setPassword(encryptedPassword);
            user.setHashType("SHA-256");
            session.update(user);
            session.getTransaction().commit();
            return encryptedPassword;
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
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
            if (sourceURL == null) {
                sourceURL = "https://www.easy-insight.com/app";
            }
            createAccountActivation(conn, account.getAccountID(), activationKey, sourceURL);
            //}
            session.flush();
            conn.commit();
            if (SecurityUtil.getSecurityProvider() instanceof DefaultSecurityProvider) {
                new AccountMemberInvitation().sendActivationEmail(user.getEmail(), user.getFirstName(), activationKey);
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
        if(salt == null) {
            password = PasswordService.getInstance().encrypt(password, user.getHashSalt(), "SHA-256");
        }
        user.setPassword(password);
        user.setHashType("SHA-256");
        user.setUserKey(RandomTextGenerator.generateText(20));
        user.setUserSecretKey(RandomTextGenerator.generateText(20));
        return user;
    }

    private void configureNewAccount(Account account) {
        account.setAccountState(Account.INACTIVE);
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
    }

    public UserServiceResponse isSessionLoggedIn() {
        UserPrincipal existing = (UserPrincipal) FlexContext.getFlexSession().getUserPrincipal();
        if (existing == null) {
            return null;
        } else {
            UserInfo userInfo = retrieveUser();
            User user = userInfo.user;
            Account account = user.getAccount();
            UserServiceResponse response = new UserServiceResponse(true, user.getUserID(), user.getAccount().getAccountID(), user.getName(),
                                account.getAccountType(), account.getMaxSize(), user.getEmail(), user.getUserName(),
                    user.isAccountAdmin(), (user.getAccount().isBillingInformationGiven() != null && user.getAccount().isBillingInformationGiven()), user.getAccount().getAccountState(),
                    user.getUiSettings(), user.getFirstName(), !account.isUpgraded(), !user.isInitialSetupDone(), user.getLastLoginDate(), account.getName(),
                    user.getPersonaID(), account.getDateFormat(), account.isDefaultReportSharing(), true, user.isGuestUser(), account.getCurrencySymbol(),
                    userInfo.settings, account.getFirstDayOfWeek(), user.getUserKey(), user.getUserSecretKey(), user.isOptInEmail(), user.getFixedDashboardID(),
                    new ReportTypeOptions());
            response.setScenario(existing.getScenario());
            return response;
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
                    Account account = user.getAccount();
                    if (user.getPersonaID() != null) {
                        user.setUiSettings(UISettingRetrieval.getUISettings(user.getPersonaID(), conn, account));
                    }
                    userServiceResponse = new UserServiceResponse(true, user.getUserID(), user.getAccount().getAccountID(), user.getName(),
                         user.getAccount().getAccountType(), account.getMaxSize(), user.getEmail(), user.getUserName(), user.isAccountAdmin(),
                            (user.getAccount().isBillingInformationGiven() != null && user.getAccount().isBillingInformationGiven()),
                            user.getAccount().getAccountState(), user.getUiSettings(), user.getFirstName(),
                            !account.isUpgraded(), !user.isInitialSetupDone(), user.getLastLoginDate(), account.getName(),
                            user.getPersonaID(), account.getDateFormat(), account.isDefaultReportSharing(), true, user.isGuestUser(), account.getCurrencySymbol(),
                            ApplicationSkinSettings.retrieveSkin(userID, session, user.getAccount().getAccountID()), account.getFirstDayOfWeek(),
                            user.getUserKey(), user.getUserSecretKey(), user.isOptInEmail(), user.getFixedDashboardID(),
                    new ReportTypeOptions());
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
                    Account account = user.getAccount();
                    if (user.getPersonaID() != null) {
                        user.setUiSettings(UISettingRetrieval.getUISettings(user.getPersonaID(), conn, account));
                    }
                    userServiceResponse = new UserServiceResponse(true, user.getUserID(), user.getAccount().getAccountID(), user.getName(),
                         user.getAccount().getAccountType(), account.getMaxSize(), user.getEmail(), user.getUserName(), user.isAccountAdmin(),
                            (user.getAccount().isBillingInformationGiven() != null && user.getAccount().isBillingInformationGiven()),
                            user.getAccount().getAccountState(), user.getUiSettings(), user.getFirstName(),
                            !account.isUpgraded(), !user.isInitialSetupDone(), user.getLastLoginDate(), account.getName(), 
                            user.getPersonaID(), account.getDateFormat(), account.isDefaultReportSharing(), true, user.isGuestUser(), account.getCurrencySymbol(),
                            ApplicationSkinSettings.retrieveSkin(userID, session, user.getAccount().getAccountID()), account.getFirstDayOfWeek(),
                            user.getUserKey(), user.getUserSecretKey(), user.isOptInEmail(), user.getFixedDashboardID(),
                    new ReportTypeOptions());
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
            if(results.size() > 0) {
                User user = (User) results.get(0);
                int state = user.getAccount().getAccountState();
                delinquent = (state == Account.DELINQUENT || state == Account.CLOSED);
            }
        }
        finally {
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
                    List accountResults = session.createQuery("from Account where accountID = ?").setLong(0, user.getAccount().getAccountID()).list();
                    Account account = (Account) accountResults.get(0);


                        if (user.getPersonaID() != null) {
                            user.setUiSettings(UISettingRetrieval.getUISettings(user.getPersonaID(), conn, account));
                        }
                        userServiceResponse = new UserServiceResponse(true, user.getUserID(), user.getAccount().getAccountID(), user.getName(),
                            user.getAccount().getAccountType(), account.getMaxSize(), user.getEmail(), user.getUserName(), user.isAccountAdmin(),
                                (user.getAccount().isBillingInformationGiven() != null && user.getAccount().isBillingInformationGiven()), user.getAccount().getAccountState(),
                                user.getUiSettings(), user.getFirstName(), !account.isUpgraded(), !user.isInitialSetupDone(), user.getLastLoginDate(), account.getName(),
                                user.getPersonaID(), account.getDateFormat(), account.isDefaultReportSharing(), true, user.isGuestUser(),
                                account.getCurrencySymbol(), ApplicationSkinSettings.retrieveSkin(user.getUserID(), session, user.getAccount().getAccountID()), account.getFirstDayOfWeek(),
                                user.getUserKey(), user.getUserSecretKey(), user.isOptInEmail(), user.getFixedDashboardID(),
                    new ReportTypeOptions());


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
            List accountResults = session.createQuery("from Account where accountID = ?").setLong(0, user.getAccount().getAccountID()).list();
            Account account = (Account) accountResults.get(0);
            user.setUiSettings(UISettingRetrieval.getUISettings(user.getPersonaID(), conn, account));
            userServiceResponse = new UserServiceResponse(true, user.getUserID(), user.getAccount().getAccountID(), user.getName(),
                 user.getAccount().getAccountType(), account.getMaxSize(), user.getEmail(), user.getUserName(), user.isAccountAdmin(),
                    (user.getAccount().isBillingInformationGiven() != null && user.getAccount().isBillingInformationGiven()),
                    user.getAccount().getAccountState(), user.getUiSettings(), user.getFirstName(),
                    !account.isUpgraded(), !user.isInitialSetupDone(), user.getLastLoginDate(), account.getName(),
                    user.getPersonaID(), account.getDateFormat(), account.isDefaultReportSharing(), true, user.isGuestUser(), account.getCurrencySymbol(),
                    ApplicationSkinSettings.retrieveSkin(user.getUserID(), session, user.getAccount().getAccountID()), account.getFirstDayOfWeek(),
                    user.getUserKey(), user.getUserSecretKey(), user.isOptInEmail(), user.getFixedDashboardID(),
                    new ReportTypeOptions());
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
            if (user.getPersonaID() != null) {
                user.setUiSettings(UISettingRetrieval.getUISettings(user.getPersonaID(), conn, account));
            }
            if (user.getUserKey() == null) {
                user.setUserKey(RandomTextGenerator.generateText(20));
            }
            if (user.getUserSecretKey() == null) {
                user.setUserSecretKey(RandomTextGenerator.generateText(20));
            }
            userServiceResponse = new UserServiceResponse(true, user.getUserID(), user.getAccount().getAccountID(), user.getName(),
                 user.getAccount().getAccountType(), account.getMaxSize(), user.getEmail(), user.getUserName(), user.isAccountAdmin(),
                    (user.getAccount().isBillingInformationGiven() != null && user.getAccount().isBillingInformationGiven()),
                    user.getAccount().getAccountState(), user.getUiSettings(), user.getFirstName(),
                    !account.isUpgraded(), !user.isInitialSetupDone(), user.getLastLoginDate(), account.getName(),
                    user.getPersonaID(), account.getDateFormat(), account.isDefaultReportSharing(), true, user.isGuestUser(), account.getCurrencySymbol(),
                    ApplicationSkinSettings.retrieveSkin(user.getUserID(), session, user.getAccount().getAccountID()), account.getFirstDayOfWeek(),
                    user.getUserKey(), user.getUserSecretKey(), user.isOptInEmail(), user.getFixedDashboardID(),
                    new ReportTypeOptions());
            user.setLastLoginDate(new Date());
            session.update(user);
        } else if (account.getExternalLogin() != null) {
            String result = account.getExternalLogin().login(userName, password);
            if (result == null) {
                session.update(account.getExternalLogin());
                if (user.getPersonaID() != null) {
                    user.setUiSettings(UISettingRetrieval.getUISettings(user.getPersonaID(), conn, account));
                }
                userServiceResponse = new UserServiceResponse(true, user.getUserID(), user.getAccount().getAccountID(), user.getName(),
                     user.getAccount().getAccountType(), account.getMaxSize(), user.getEmail(), user.getUserName(), user.isAccountAdmin(),
                        (user.getAccount().isBillingInformationGiven() != null && user.getAccount().isBillingInformationGiven()),
                        user.getAccount().getAccountState(), user.getUiSettings(), user.getFirstName(),
                        !account.isUpgraded(), !user.isInitialSetupDone(), user.getLastLoginDate(), account.getName(),
                        user.getPersonaID(), account.getDateFormat(), account.isDefaultReportSharing(), true, user.isGuestUser(), account.getCurrencySymbol(),
                        ApplicationSkinSettings.retrieveSkin(user.getUserID(), session, user.getAccount().getAccountID()), account.getFirstDayOfWeek(),
                        user.getUserKey(), user.getUserSecretKey(), user.isOptInEmail(), user.getFixedDashboardID(),
                    new ReportTypeOptions());
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

    public UserServiceResponse guestLogin(String userName, String scenarioKey) {
        UserServiceResponse userServiceResponse = null;
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        List results;
        try {
            conn.setAutoCommit(false);
            @SuppressWarnings({"unchecked"}) List<Scenario> scenarios = session.createQuery("from Scenario where scenarioKey = ?").setString(0, scenarioKey).list();
            if (scenarios.size() > 0) {
                Scenario scenario = scenarios.get(0);
                results = session.createQuery("from User where userName = ?").setString(0, userName).list();
                if (results.size() > 0) {
                    User user = (User) results.get(0);
                    if (user.isGuestUser()) {
                        List accountResults = session.createQuery("from Account where accountID = ?").setLong(0, user.getAccount().getAccountID()).list();
                        Account account = (Account) accountResults.get(0);

                        if (user.getPersonaID() != null) {
                            user.setUiSettings(UISettingRetrieval.getUISettings(user.getPersonaID(), conn, account));
                        }
                        userServiceResponse = new UserServiceResponse(true, user.getUserID(), user.getAccount().getAccountID(), user.getName(),
                             user.getAccount().getAccountType(), account.getMaxSize(), user.getEmail(), user.getUserName(), user.isAccountAdmin(),
                                (user.getAccount().isBillingInformationGiven() != null && user.getAccount().isBillingInformationGiven()),
                                user.getAccount().getAccountState(), user.getUiSettings(), user.getFirstName(),
                                !account.isUpgraded(), !user.isInitialSetupDone(), user.getLastLoginDate(), account.getName(), 
                                user.getPersonaID(), account.getDateFormat(), account.isDefaultReportSharing(), true, user.isGuestUser(), account.getCurrencySymbol(),
                                ApplicationSkinSettings.retrieveSkin(user.getUserID(), session, user.getAccount().getAccountID()), account.getFirstDayOfWeek(),
                                user.getUserKey(), user.getUserSecretKey(), user.isOptInEmail(), user.getFixedDashboardID(),
                    new ReportTypeOptions());
                        userServiceResponse.setScenario(scenario);
                        user.setLastLoginDate(new Date());
                        session.update(user);
                    }
                }
                session.flush();
                conn.commit();
            }
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
}
