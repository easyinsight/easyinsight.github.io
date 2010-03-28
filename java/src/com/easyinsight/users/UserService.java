package com.easyinsight.users;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.preferences.UISettingRetrieval;
import com.easyinsight.salesautomation.SalesEmail;
import com.easyinsight.security.PasswordService;
import com.easyinsight.security.UserPrincipal;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.DefaultSecurityProvider;
import com.easyinsight.logging.LogClass;
import com.easyinsight.email.UserStub;
import com.easyinsight.email.AccountMemberInvitation;
import com.easyinsight.util.RandomTextGenerator;
import com.easyinsight.groups.Group;
import com.easyinsight.groups.GroupStorage;
import com.easyinsight.billing.BrainTreeBillingSystem;
import com.easyinsight.outboundnotifications.BuyOurStuffTodo;

import java.util.List;
import java.util.Date;
import java.util.Calendar;
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
public class UserService implements IUserService {

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
            if(rs.next() && rs.getString(2).equals(passwordResetValidation)) {

                List l = s.createQuery("from User where userName = ? and userID = ?").setString(0, username).setLong(1, rs.getLong(1)).list();
                if(l.size() == 1) {
                    User u = (User) l.get(0);
                    u.setPassword(PasswordService.getInstance().encrypt(password));
                    s.update(u);
                    success = true;
                    PreparedStatement deleteStatement = conn.prepareStatement("delete from password_reset where password_request_string = ?");
                    deleteStatement.setString(1, passwordResetValidation);
                    deleteStatement.executeUpdate();
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
                    }
                }
            }

            stmt.close();
            s.flush();
            conn.commit();
            s.close();
        } catch(SQLException e) {
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
    public String doesUserExist(String userName, String email) {
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
                        user.setUiSettings(UISettingRetrieval.getUISettings(user.getPersonaID(), conn));
                    }
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
    }    

    public void updateUserLabels(String userName, String fullName, String email) {
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
            account.addUser(user);
            session.save(account);
            user.setAccount(account);
            session.update(user);
            if (account.getAccountType() == Account.PROFESSIONAL || account.getAccountType() == Account.PREMIUM || account.getAccountType() == Account.ENTERPRISE) {
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
            //if (account.getAccountType() == Account.FREE || account.getAccountType() == Account.INDIVIDUAL) {
            String activationKey = RandomTextGenerator.generateText(12);
            PreparedStatement insertActivationStmt = conn.prepareStatement("INSERT INTO ACCOUNT_ACTIVATION (ACCOUNT_ID, ACTIVATION_KEY, CREATION_DATE, target_url) VALUES (?, ?, ?, ?)");
            insertActivationStmt.setLong(1, account.getAccountID());
            insertActivationStmt.setString(2, activationKey);
            insertActivationStmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            insertActivationStmt.setString(4, sourceURL);
            insertActivationStmt.execute();
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
                new AccountMemberInvitation().sendActivationEmail(user.getEmail(), activationKey);
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
        if (account.getAccountType() == Account.ENTERPRISE) {
            account.setMaxUsers(500);
            account.setMaxSize(1000000000);
        } else if (account.getAccountType() == Account.PREMIUM) {
            account.setMaxUsers(50);
            account.setMaxSize(200000000);
        } else if (account.getAccountType() == Account.BASIC) {
            account.setMaxUsers(1);
            account.setMaxSize(20000000);
        } else if (account.getAccountType() == Account.PERSONAL) {
            account.setMaxUsers(1);
            account.setMaxSize(1000000);
        } else if (account.getAccountType() == Account.PROFESSIONAL) {
            account.setMaxUsers(10);
            account.setMaxSize(10000000);
        }
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
            if(account.getAccountState() == Account.CLOSED || account.getAccountState() == Account.DELINQUENT)
                return null;
            UserServiceResponse response = new UserServiceResponse(true, user.getUserID(), user.getAccount().getAccountID(), user.getName(),
                                account.getAccountType(), account.getMaxSize(), user.getEmail(), user.getUserName(),
                                user.getPassword(), user.isAccountAdmin(), user.isDataSourceCreator(), user.isInsightCreator(), (user.getAccount().isBillingInformationGiven() != null && user.getAccount().isBillingInformationGiven()), user.getAccount().getAccountState(),
                    user.getUiSettings());
            response.setActivated(account.isActivated());
            return response;
        }
    }

    public UserStub getUserStub(String userName) {
        UserStub userStub = null;
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from User where userName = ?").setString(0, userName).list();
            if (results.size() > 0) {
                User user = (User) results.get(0);
                userStub = new UserStub(user.getUserID(), user.getUserName(), user.getEmail(), user.getName(), user.getAccount().getAccountID());
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
                        user.setUiSettings(UISettingRetrieval.getUISettings(user.getPersonaID(), conn));
                    }
                    if (account.getAccountState() == Account.ACTIVE || account.getAccountState() == Account.TRIAL || account.getAccountState() == Account.DELINQUENT) {
                        userServiceResponse = new UserServiceResponse(true, user.getUserID(), user.getAccount().getAccountID(), user.getName(),
                            user.getAccount().getAccountType(), account.getMaxSize(), user.getEmail(), user.getUserName(), encryptedPassword, user.isAccountAdmin(), user.isDataSourceCreator(),
                                user.isInsightCreator(), (user.getAccount().isBillingInformationGiven() != null && user.getAccount().isBillingInformationGiven()), user.getAccount().getAccountState(),
                                user.getUiSettings());
                        userServiceResponse.setActivated(account.isActivated());
                    } else {
                        userServiceResponse = new UserServiceResponse(false, "Your account is not active.");
                    }
                } else {
                    userServiceResponse = new UserServiceResponse(false, "Incorrect password, please try again.");
                }
            } else {
                userServiceResponse = new UserServiceResponse(false, "Unknown user name or email address, please try again.");
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
    public UserServiceResponse authenticate(String userName, String password) {

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
            } else {
                results = session.createQuery("from User where email = ?").setString(0, userName).list();
                if (results.size() > 0) {
                    User user = (User) results.get(0);
                    userServiceResponse = getUser(password, session, user, conn);
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
            if (user.getPersonaID() != null) {
                user.setUiSettings(UISettingRetrieval.getUISettings(user.getPersonaID(), conn));
            }
            Account account = (Account) accountResults.get(0);
            if (account.getAccountState() == Account.ACTIVE || account.getAccountState() == Account.TRIAL || account.getAccountState() == Account.CLOSING  || account.getAccountState() == Account.DELINQUENT) {
                userServiceResponse = new UserServiceResponse(true, user.getUserID(), user.getAccount().getAccountID(), user.getName(),
                     user.getAccount().getAccountType(), account.getMaxSize(), user.getEmail(), user.getUserName(), encryptedPassword, user.isAccountAdmin(), user.isDataSourceCreator(), user.isInsightCreator(),
                        (user.getAccount().isBillingInformationGiven() != null && user.getAccount().isBillingInformationGiven()), user.getAccount().getAccountState(), user.getUiSettings());
                userServiceResponse.setActivated(account.isActivated());
                user.setLastLoginDate(new Date());
                session.update(user);
            } else {
                userServiceResponse = new UserServiceResponse(false, "Your account is not active.");
            }
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
