package com.easyinsight.users;

import com.easyinsight.database.Database;
import com.easyinsight.security.PasswordService;
import com.easyinsight.security.UserPrincipal;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.logging.LogClass;
import com.easyinsight.email.UserStub;
import com.easyinsight.email.AccountMemberInvitation;
import com.easyinsight.util.RandomTextGenerator;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;

import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import flex.messaging.FlexContext;

/**
 * User: jboe
 * Date: Jan 2, 2008
 * Time: 5:34:56 PM
 */
public class UserService implements IUserService {    

    public void deleteUser(User user) {
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            session.delete(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public long doesUserExist(String userName) {
        Session session = Database.instance().createSession();
        List results;
        try {
            session.beginTransaction();
            results = session.createQuery("from User where userName = ?").setString(0, userName).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        long accountID = 0;
        if (results.size() > 0) {
            User user = (User) results.get(0);
            accountID = user.getAccountID().getAccountID();
        }
        return accountID;
    }

    public User retrieveUser() {
        long userID = SecurityUtil.getUserID();
        try {
            User user = null;
            Session session = Database.instance().createSession();
            List results;
            try {
                session.beginTransaction();
                results = session.createQuery("from User where userID = ?").setLong(0, userID).list();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new RuntimeException(e);
            } finally {
                session.close();
            }
            if (results.size() > 0) {
                user = (User) results.get(0);
                user.setLicenses(new ArrayList<SubscriptionLicense>());
            }
            return user;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public User retrieveUser(Connection conn) {
        long userID = SecurityUtil.getUserID();
        try {
            User user = null;
            Session session = Database.instance().createSession(conn);
            List results;
            try {
                session.beginTransaction();
                results = session.createQuery("from User where userID = ?").setLong(0, userID).list();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new RuntimeException(e);
            } finally {
                session.close();
            }
            if (results.size() > 0) {
                user = (User) results.get(0);
                user.setLicenses(new ArrayList<SubscriptionLicense>());
            }
            return user;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    /*public void updateUser(String userName, String fullName, String email) {
        User user = retrieveUser();
        if (SecurityUtil.getAccountID() != user.getAccountID().getAccountID()) {
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
    }  */

    public boolean updatePassword(String existingPassword, String password) {
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            User user = (User) session.createQuery("from User where userID = ?").setLong(0, SecurityUtil.getUserID()).list().get(0);
            if (!PasswordService.getInstance().encrypt(existingPassword).equals(user.getPassword())) {
                return false;
            }
            user.setPassword(PasswordService.getInstance().encrypt(password));
            session.update(user);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public void updateUser(UserTransferObject userTransferObject) {
        long accountID = SecurityUtil.getAccountID();
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            List results = session.createQuery("from Account where accountID = ?").setLong(0, accountID).list();
            Account account = (Account) results.get(0);
            User user = null;
            for (User checkingUser : account.getUsers()) {
                if (checkingUser.getUserID() == userTransferObject.getUserID()) {
                    user = checkingUser;
                }
            }
            if (user == null) {
                throw new RuntimeException("Attempt made to update user who does not exist.");
            }
            user.setAccountAdmin(userTransferObject.isAccountAdmin());
            user.setAccountID(account);
            user.setDataSourceCreator(userTransferObject.isDataSourceCreator());
            user.setEmail(userTransferObject.getEmail());
            user.setInsightCreator(userTransferObject.isInsightCreator());
            user.setName(userTransferObject.getName());
            user.setUserName(userTransferObject.getUserName());
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

    /*public void addLicenses(long feedID, int numberOfLicenses) {
        long accountID = SecurityUtil.getAccountID();
        Account account = getAccount(accountID);
        for (int i = 0; i < numberOfLicenses; i++) {
            SubscriptionLicense subscriptionLicense = new SubscriptionLicense();
            subscriptionLicense.setFeedID(feedID);
            account.addLicense(subscriptionLicense);
        }
        updateAccount(account);
    } */

    // okay, so creating a user is going to create the user and the account
    // authenticate is going to return the user and the account ID (NOT the account)
    // add user to account
    // remove user from account (delete the user)
    // upgrade the account
    // downgrade the account
    // change the permissions on a user within the context of an account
    // reassign licenses between users

    public long createAccount(User initialUser, AccountTransferObject accountTransferObject) {
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            Account account = accountTransferObject.toAccount();
            initialUser.setPassword(PasswordService.getInstance().encrypt(initialUser.getPassword()));
            //session.save(initialUser);
            initialUser.setPermissions(AccountUserPermissions.GRANT_SUBSCRIBE_EXTERNAL |
                    AccountUserPermissions.ADD_USERS | AccountUserPermissions.DELETE_USERS |
                    AccountUserPermissions.SUBSCRIBE_EXTERNAL);
            account.addUser(initialUser);
            session.save(account);
            session.getTransaction().commit();
            return account.getAccountID();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public Account getAccount(long accountID) {
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            List results = session.createQuery("from Account where accountID = ?").setLong(0, accountID).list();
            Account account = (Account) results.get(0);
            account.setLicenses(new ArrayList<SubscriptionLicense>(account.getLicenses()));
            for (User user : account.getUsers()) {
                List<SubscriptionLicense> userLicenses = new ArrayList<SubscriptionLicense>(user.getLicenses());
                user.setLicenses(userLicenses);            
            }
            session.getTransaction().commit();
            return account;
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public void createAccount(Account account) {
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            session.save(account);
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public void updateAccount(AccountTransferObject accountTransferObject) {
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            List accountResults = session.createQuery("from Account where accountID = ?").setLong(0, accountTransferObject.getAccountID()).list();
            Account account = (Account) accountResults.get(0);
            account.setAccountType(accountTransferObject.getAccountType().getAccountType());
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

    public UserCreationResponse addUserToAccount(UserTransferObject userTransferObject) {
        long accountID = SecurityUtil.getAccountID();
        UserCreationResponse userCreationResponse;
        long exists = doesUserExist(userTransferObject.getUserName());
        if (exists > 0) {
            userCreationResponse = new UserCreationResponse("A user already exists by that name.");
        } else {
            Session session = Database.instance().createSession();
            try {
                session.beginTransaction();
                List results = session.createQuery("from Account where accountID = ?").setLong(0, accountID).list();
                Account account = (Account) results.get(0);
                User admin = (User) session.createQuery("from User where userID = ?").setLong(0, SecurityUtil.getUserID()).list().get(0);
                User user = new User();
                user.setAccountAdmin(userTransferObject.isAccountAdmin());
                user.setAccountID(account);
                user.setDataSourceCreator(userTransferObject.isDataSourceCreator());
                user.setEmail(userTransferObject.getEmail());
                user.setInsightCreator(userTransferObject.isInsightCreator());
                user.setName(userTransferObject.getName());
                user.setUserName(userTransferObject.getUserName());
                final String password = RandomTextGenerator.generateText(12);
                final String adminName = admin.getName();
                final String userEmail = user.getEmail();
                final String userName = user.getUserName();
                user.setPassword(PasswordService.getInstance().encrypt(password));
                account.addUser(user);                
                user.setAccountID(account);
                session.update(account);
                session.getTransaction().commit();
                new Thread(new Runnable() {
                    public void run() {
                        new AccountMemberInvitation().sendAccountEmail(userEmail, adminName, userName, password);
                    }
                }).start();
                userCreationResponse = new UserCreationResponse(userTransferObject.getUserID());
            } catch (Exception e) {
                LogClass.error(e);
                session.getTransaction().rollback();
                throw new RuntimeException(e);
            } finally {
                session.close();
            }
        }
        return userCreationResponse;
    }



    public void removeUser(Account account, User newUser) {
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            account.removeUser(newUser);
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public void deleteAccount(Account account) {
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    @NotNull
    public UserServiceResponse createUser(User user) {
        String userName = user.getUserName();
        String password = user.getPassword();
        UserServiceResponse userServiceResponse;
        if (userName == null || password == null) {
             userServiceResponse = new UserServiceResponse(false, "User name and password must have values.");
             return userServiceResponse;
        }
        String trimmedUserName = userName.trim();
        String trimmedPassword = password.trim();
        if (trimmedUserName.equals("") || trimmedPassword.equals("")) {
            userServiceResponse = new UserServiceResponse(false, "User name and password must have values.");
            return userServiceResponse;
        }
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            List results = session.createQuery("from User where userName = ?").setString(0, user.getUserName()).list();
            String encryptedPassword = PasswordService.getInstance().encrypt(user.getPassword());
            user.setPassword(encryptedPassword);
            session.saveOrUpdate(user);
            if (results.size() > 0) {
                userServiceResponse = new UserServiceResponse(false, "User already exists by that name.");
            } else {
                userServiceResponse = new UserServiceResponse(true, user.getUserID(), user.getAccountID().getAccountID(), user.getName(), null, 50000000, user.getEmail(),
                        user.getUserName(), encryptedPassword);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return userServiceResponse;
    }

    public UserServiceResponse isSessionLoggedIn() {
        UserPrincipal existing = (UserPrincipal) FlexContext.getFlexSession().getUserPrincipal();
        if (existing == null) {
            return null;
        } else {
            User user = retrieveUser();
            Account account = user.getAccountID();
            AccountType accountType = account.accountTypeObject();
            return new UserServiceResponse(true, user.getUserID(), user.getAccountID().getAccountID(), user.getName(),
                                accountType, account.getMaxSize(), user.getEmail(), user.getUserName(),
                                user.getPassword());
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
                userStub = new UserStub(user.getUserID(), user.getUserName(), user.getEmail(), user.getName());
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

    public UserServiceResponse authenticateWithEncrypted(String userName, String encryptedPassword) {
        try {
            UserServiceResponse userServiceResponse;
            Session session = Database.instance().createSession();
            List results;
            try {
                session.getTransaction().begin();
                results = session.createQuery("from User where userName = ?").setString(0, userName).list();
                if (results.size() > 0) {
                    User user = (User) results.get(0);
                    String actualPassword = user.getPassword();
                    if (encryptedPassword.equals(actualPassword)) {
                        List accountResults = session.createQuery("from Account where accountID = ?").setLong(0, user.getAccountID().getAccountID()).list();
                        Account account = (Account) accountResults.get(0);
                        AccountType accountType = account.accountTypeObject();
                        userServiceResponse = new UserServiceResponse(true, user.getUserID(), user.getAccountID().getAccountID(), user.getName(),
                                accountType, account.getMaxSize(), user.getEmail(), user.getUserName(), encryptedPassword);
                        // FlexContext.getFlexSession().getRemoteCredentials();
                    } else {
                        userServiceResponse = new UserServiceResponse(false, "Incorrect password, please try again.");
                    }
                } else {
                    userServiceResponse = new UserServiceResponse(false, "Incorrect user name, please try again.");
                }
                session.getTransaction().commit();
            } finally {
                session.close();
            }
            return userServiceResponse;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public UserServiceResponse authenticate(String userName, String password) {
        try {
            UserServiceResponse userServiceResponse;
            Session session = Database.instance().createSession();
            List results;
            try {
                session.getTransaction().begin();
                results = session.createQuery("from User where userName = ?").setString(0, userName).list();
                if (results.size() > 0) {
                    User user = (User) results.get(0);
                    String actualPassword = user.getPassword();
                    String encryptedPassword = PasswordService.getInstance().encrypt(password);
                    if (encryptedPassword.equals(actualPassword)) {
                        List accountResults = session.createQuery("from Account where accountID = ?").setLong(0, user.getAccountID().getAccountID()).list();
                        Account account = (Account) accountResults.get(0);
                        AccountType accountType = account.accountTypeObject();
                        userServiceResponse = new UserServiceResponse(true, user.getUserID(), user.getAccountID().getAccountID(), user.getName(),
                                accountType, account.getMaxSize(), user.getEmail(), user.getUserName(), encryptedPassword);
                        // FlexContext.getFlexSession().getRemoteCredentials();
                    } else {
                        userServiceResponse = new UserServiceResponse(false, "Incorrect password, please try again.");
                    }
                } else {
                    userServiceResponse = new UserServiceResponse(false, "Incorrect user name, please try again.");
                }
                session.getTransaction().commit();
            } finally {
                session.close();
            }
            return userServiceResponse;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
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

    public List<UserTransferObject> getUsers() {
        List<UserTransferObject> users = new ArrayList<UserTransferObject>();
        long accountID = SecurityUtil.getAccountID();
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            List results = session.createQuery("from Account where accountID = ?").setLong(0, accountID).list();
            Account account = (Account) results.get(0);
            for (User user : account.getUsers()) {
                users.add(user.toUserTransferObject());
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return users;
    }
}
