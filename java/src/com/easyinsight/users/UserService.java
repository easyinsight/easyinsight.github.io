package com.easyinsight.users;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
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
import com.easyinsight.outboundnotifications.BuyOurStuffTodo;

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
                PreparedStatement updateUserStmt = conn.prepareStatement("UPDATE USER SET PERSONA_ID = ? WHERE USER_ID = ?");
                updateUserStmt.setLong(1, personaMap.get(accountSetupData.getMyPersona()));
                updateUserStmt.setLong(2, SecurityUtil.getUserID());
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
            user.setPassword(PasswordService.getInstance().encrypt(password));
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
            c.setTimeInMillis(c.getTimeInMillis() - 86400000L);
            stmt.setString(1,passwordResetString);
            stmt.setDate(2, new java.sql.Date(c.getTimeInMillis()));
            ResultSet rs = stmt.executeQuery();
            if(rs.next() && rs.getString(1).equals(passwordResetString))
                success = true;
            stmt.close();
            conn.commit();
        } catch(SQLException e){
            conn.rollback();
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
                        u.setPassword(PasswordService.getInstance().encrypt(password));
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
                            u.setPassword(PasswordService.getInstance().encrypt(password));
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
            session.getTransaction().begin();
            Account a = (Account) session.createQuery("from Account where accountID  = ?").setLong(0, accountID).list().get(0);
            a.setAccountState(Account.CLOSING);
            a.setBillingInformationGiven(false);
            BrainTreeBillingSystem billingSystem = new BrainTreeBillingSystem();
            billingSystem.setUsername("testapi");
            billingSystem.setPassword("password1");
            billingSystem.cancelPlan(a.getAccountID());
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
                    insertStatement.executeUpdate();

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

    private User retrieveUser() {
        long userID = SecurityUtil.getUserID();
        try {
            User user = null;
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
                }
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new RuntimeException(e);
            } finally {
                conn.setAutoCommit(true);
                session.close();
                Database.closeConnection(conn);
            }
            return user;
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
    public void updateUserLabels(String userName, String fullName, String email, String firstName) {
        User user = retrieveUser();
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
            String encryptedPassword = PasswordService.getInstance().encrypt(password);
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
            String encryptedExistingPassword = PasswordService.getInstance().encrypt(existingPassword);
            User user = (User) session.createQuery("from User where userID = ?").setLong(0, SecurityUtil.getUserID()).list().get(0);
            if (!encryptedExistingPassword.equals(user.getPassword())) {
                return null;
            }
            String encryptedPassword = PasswordService.getInstance().encrypt(password);
            user.setPassword(encryptedPassword);
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
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            Account account = accountTransferObject.toAccount();
            account.setCreationDate(new Date());
            configureNewAccount(account);
            User user = createInitialUser(userTransferObject, password, account);
            user.setInitialSetupDone(true);
            account.addUser(user);
            session.save(account);
            user.setAccount(account);
            session.update(user);
            if (account.getAccountType() != Account.PERSONAL) {
                Group group = new Group();
                group.setName(account.getName());
                group.setPubliclyVisible(false);
                group.setPubliclyJoinable(false);
                group.setDescription("This group was automatically created to act as a location for exposing data to all users in the account.");
                account.setGroupID(new GroupStorage().addGroup(group, user.getUserID(), conn));
                session.update(account);
            }
            if(account.getAccountType() != Account.PERSONAL) {
                BuyOurStuffTodo todo = new BuyOurStuffTodo();
                todo.setUserID(user.getUserID());
                session.save(todo);
            }
            new AccountActivityStorage().saveAccountActivity(new AccountActivity(account.getAccountType(),
                    new Date(), account.getAccountID(), 0, AccountActivity.ACCOUNT_CREATED, "", 0, 0, Account.ACTIVE), conn);
            if (account.getAccountType() != Account.PERSONAL) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, 30);
                new AccountActivityStorage().saveAccountTimeChange(account.getAccountID(), Account.ACTIVE, cal.getTime(), conn);
            }
            //}
            session.flush();
            conn.commit();
            if (SecurityUtil.getSecurityProvider() instanceof DefaultSecurityProvider) {
                //new AccountMemberInvitation().sendActivationEmail(user.getEmail(), activationKey);
                new Thread(new SalesEmail(account, user)).start();
            }
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

    private User createInitialUser(UserTransferObject userTransferObject, String password, Account account) {
        User user = userTransferObject.toUser();
        user.setAccount(account);
        user.setPassword(PasswordService.getInstance().encrypt(password));
        return user;
    }

    private void configureNewAccount(Account account) {
        if (account.getAccountType() == Account.PERSONAL) {
            account.setAccountState(Account.ACTIVE);
        } else {
            account.setAccountState(Account.TRIAL);
        }
        account.setActivated(false);
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

    public UserServiceResponse isSessionLoggedIn() {
        UserPrincipal existing = (UserPrincipal) FlexContext.getFlexSession().getUserPrincipal();
        if (existing == null) {
            return null;
        } else {
            User user = retrieveUser();
            Account account = user.getAccount();
            UserServiceResponse response = new UserServiceResponse(true, user.getUserID(), user.getAccount().getAccountID(), user.getName(),
                                account.getAccountType(), account.getMaxSize(), user.getEmail(), user.getUserName(),
                    user.isAccountAdmin(), (user.getAccount().isBillingInformationGiven() != null && user.getAccount().isBillingInformationGiven()), user.getAccount().getAccountState(),
                    user.getUiSettings(), user.getFirstName(), !account.isUpgraded(), !user.isInitialSetupDone(), user.getLastLoginDate(), account.getName(), user.isRenewalOptionAvailable(),
                    user.getPersonaID(), account.getDateFormat(), account.isDefaultReportSharing(), true);
            response.setActivated(account.isActivated());
            return response;
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
                    Account account = user.getAccount();
                    if (user.getPersonaID() != null) {
                        user.setUiSettings(UISettingRetrieval.getUISettings(user.getPersonaID(), conn, account));
                    }
                    userServiceResponse = new UserServiceResponse(true, user.getUserID(), user.getAccount().getAccountID(), user.getName(),
                         user.getAccount().getAccountType(), account.getMaxSize(), user.getEmail(), user.getUserName(), user.isAccountAdmin(),
                            (user.getAccount().isBillingInformationGiven() != null && user.getAccount().isBillingInformationGiven()),
                            user.getAccount().getAccountState(), user.getUiSettings(), user.getFirstName(),
                            !account.isUpgraded(), !user.isInitialSetupDone(), user.getLastLoginDate(), account.getName(), user.isRenewalOptionAvailable(),
                            user.getPersonaID(), account.getDateFormat(), account.isDefaultReportSharing(), true);
                    userServiceResponse.setActivated(account.isActivated());
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
                if (encryptedPassword.equals(actualPassword)) {
                    List accountResults = session.createQuery("from Account where accountID = ?").setLong(0, user.getAccount().getAccountID()).list();
                    Account account = (Account) accountResults.get(0);


                        if (user.getPersonaID() != null) {
                            user.setUiSettings(UISettingRetrieval.getUISettings(user.getPersonaID(), conn, account));
                        }
                        userServiceResponse = new UserServiceResponse(true, user.getUserID(), user.getAccount().getAccountID(), user.getName(),
                            user.getAccount().getAccountType(), account.getMaxSize(), user.getEmail(), user.getUserName(), user.isAccountAdmin(),
                                (user.getAccount().isBillingInformationGiven() != null && user.getAccount().isBillingInformationGiven()), user.getAccount().getAccountState(),
                                user.getUiSettings(), user.getFirstName(), !account.isUpgraded(), !user.isInitialSetupDone(), user.getLastLoginDate(), account.getName(),
                                user.isRenewalOptionAvailable(), user.getPersonaID(), account.getDateFormat(), account.isDefaultReportSharing(), true);

                        userServiceResponse.setActivated(account.isActivated());

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
                userServiceResponse = getUser(password, session, user, conn);
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
                    userServiceResponse = getUser(password, session, user, conn);
                    if (rememberMe) {
                        String sessionCookie = RandomTextGenerator.generateText(30);
                        //user.setSessionCookie(sessionCookie);
                        userServiceResponse.setSessionCookie(sessionCookie);
                        session.update(user);
                    }
                } else {
                    userServiceResponse = new UserServiceResponse(false, "Unknown user name or email address, please try again.");
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

    private UserServiceResponse getUser(String password, Session session, User user, EIConnection conn) throws SQLException {
        UserServiceResponse userServiceResponse;
        String actualPassword = user.getPassword();
        String encryptedPassword = PasswordService.getInstance().encrypt(password);
        if (encryptedPassword.equals(actualPassword)) {
            List accountResults = session.createQuery("from Account where accountID = ?").setLong(0, user.getAccount().getAccountID()).list();
            Account account = (Account) accountResults.get(0);

                if (user.getPersonaID() != null) {
                    user.setUiSettings(UISettingRetrieval.getUISettings(user.getPersonaID(), conn, account));
                }
                userServiceResponse = new UserServiceResponse(true, user.getUserID(), user.getAccount().getAccountID(), user.getName(),
                     user.getAccount().getAccountType(), account.getMaxSize(), user.getEmail(), user.getUserName(), user.isAccountAdmin(),
                        (user.getAccount().isBillingInformationGiven() != null && user.getAccount().isBillingInformationGiven()),
                        user.getAccount().getAccountState(), user.getUiSettings(), user.getFirstName(),
                        !account.isUpgraded(), !user.isInitialSetupDone(), user.getLastLoginDate(), account.getName(), user.isRenewalOptionAvailable(),
                        user.getPersonaID(), account.getDateFormat(), account.isDefaultReportSharing(), true);
                userServiceResponse.setActivated(account.isActivated());
                user.setLastLoginDate(new Date());
                session.update(user);

            // FlexContext.getFlexSession().getRemoteCredentials();
        } else {
            userServiceResponse = new UserServiceResponse(false, "Incorrect password, please try again.");
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
}
