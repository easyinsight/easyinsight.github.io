package com.easyinsight.users;

import com.easyinsight.database.Database;
import com.easyinsight.security.PasswordService;
import com.easyinsight.security.UserPrincipal;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.Roles;
import com.easyinsight.logging.LogClass;
import com.easyinsight.email.UserStub;
import com.easyinsight.email.AccountMemberInvitation;
import com.easyinsight.util.RandomTextGenerator;
import com.easyinsight.groups.Group;
import com.easyinsight.groups.GroupStorage;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
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

    /*public String resetPassword(String emailAddress) {
        String message;
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from User where email = ?").setString(0, emailAddress).list();
            if (results.size() == 0) {
                message = "No user was found by that email address.";
            } else {
                User user = (User) results.get(0);
                String password = RandomTextGenerator.generateText(12);
                user.setPassword(password);

            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return message;
    }*/

    public UserTransferObject upgradeAccount(int toType) {
        if (toType == Account.ADMINISTRATOR) {
            throw new SecurityException();
        }
        long accountID = SecurityUtil.getAccountID();
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            Account account = (Account) session.createQuery("from Account where accountID = ?").setLong(0, accountID).list().get(0);
            User user = (User) session.createQuery("from User where userID = ?").setLong(0, SecurityUtil.getAccountID()).list().get(0);
            account.setAccountType(toType);
            if (toType == Account.PROFESSIONAL) {
                user.setAccountAdmin(true);
                user.setDataSourceCreator(true);
                user.setInsightCreator(true);
                session.update(user);
            }
            session.update(account);
            account.toTransferObject();
            session.getTransaction().commit();
            return user.toUserTransferObject();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

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

    public void updateUserLabels(String userName, String fullName, String email) {
        User user = retrieveUser();
        if (SecurityUtil.getAccountID() != user.getAccount().getAccountID()) {
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
            user.setAccount(account);
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

    public long createAccount(UserTransferObject userTransferObject, AccountTransferObject accountTransferObject, String password) {
        Connection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            Account account = accountTransferObject.toAccount();
            configureNewAccount(account);
            User user = createInitialUser(userTransferObject, password, account);
            account.addUser(user);
            session.save(account);
            user.setAccount(account);
            session.update(user);
            if (account.getAccountType() == Account.PROFESSIONAL || account.getAccountType() == Account.ENTERPRISE) {
                Group group = new Group();
                group.setName(account.getName());
                group.setPubliclyVisible(false);
                group.setPubliclyJoinable(false);
                group.setDescription("This group was automatically created to act as a location for exposing data to all users in the account.");
                account.setGroupID(new GroupStorage().addGroup(group, user.getUserID(), conn));
                session.update(account);
            }
            if (account.getAccountType() == Account.FREE || account.getAccountType() == Account.INDIVIDUAL) {
                String activationKey = RandomTextGenerator.generateText(12);
                PreparedStatement insertActivationStmt = conn.prepareStatement("INSERT INTO ACCOUNT_ACTIVATION (ACCOUNT_ID, ACTIVATION_KEY, CREATION_DATE) VALUES (?, ?, ?)");
                insertActivationStmt.setLong(1, account.getAccountID());
                insertActivationStmt.setString(2, activationKey);
                insertActivationStmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                insertActivationStmt.execute();
                new AccountMemberInvitation().sendActivationEmail(user.getEmail(), activationKey);
            }
            conn.commit();
            return account.getAccountID();
        } catch (Exception e) {
            LogClass.error(e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            throw new RuntimeException(e);
        } finally {
            session.close();
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            Database.instance().closeConnection(conn);
        }
    }

    private User createInitialUser(UserTransferObject userTransferObject, String password, Account account) {
        User user = new User();
        user.setAccountAdmin(userTransferObject.isAccountAdmin());
        user.setAccount(account);
        user.setDataSourceCreator(userTransferObject.isDataSourceCreator());
        user.setEmail(userTransferObject.getEmail());
        user.setInsightCreator(userTransferObject.isInsightCreator());
        user.setName(userTransferObject.getName());
        user.setUserName(userTransferObject.getUserName());
        user.setPassword(PasswordService.getInstance().encrypt(password));
        return user;
    }

    private void configureNewAccount(Account account) {
        if (account.getAccountType() == Account.ENTERPRISE) {
            account.setAccountState(Account.BETA);
            account.setMaxUsers(5);
            account.setMaxSize(1000000000);
        } else if (account.getAccountType() == Account.PROFESSIONAL) {
            account.setAccountState(Account.BETA);
            account.setMaxUsers(1);
            account.setMaxSize(200000000);
        } else if (account.getAccountType() == Account.INDIVIDUAL) {
            account.setAccountState(Account.BETA);
            account.setMaxUsers(1);
            account.setMaxSize(20000000);
        } else if (account.getAccountType() == Account.FREE) {
            account.setAccountState(Account.ACTIVE);
            account.setMaxUsers(1);
            account.setMaxSize(1000000);
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

    public void adminUpdate(AccountAdminTO accountTO) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            List accountResults = session.createQuery("from Account where accountID = ?").setLong(0, accountTO.getAccountID()).list();
            Account account = (Account) accountResults.get(0);
            Account updatedAccount = accountTO.toAccount();
            updatedAccount.setUsers(account.getUsers());
            updatedAccount.setLicenses(account.getLicenses());
            session.update(updatedAccount);
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
            account.setAccountType(accountTransferObject.getAccountType());
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

    public List<ConsultantTO> getConsultants() {
        List<ConsultantTO> consultants = new ArrayList<ConsultantTO>();
        long accountID = SecurityUtil.getAccountID();
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from Account where accountID = ?").setLong(0, accountID).list();
            Account account = (Account) results.get(0);
            for (Consultant consultant : account.getGuestUsers()) {
                consultants.add(consultant.toConsultantTO());
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return consultants;
    }

    public UserCreationResponse addConsultant(UserTransferObject userTransferObject) {
        long accountID = SecurityUtil.getAccountID();
        String message = doesUserExist(userTransferObject.getUserName(), userTransferObject.getEmail());
        UserCreationResponse userCreationResponse;
        if (message != null) {
            userCreationResponse = new UserCreationResponse(message);
        } else {
            Session session = Database.instance().createSession();
            try {
                session.getTransaction().begin();
                List results = session.createQuery("from Account where accountID = ?").setLong(0, accountID).list();
                Account account = (Account) results.get(0);
                User user = userTransferObject.toUser();
                user.setAccount(account);
                session.save(user);
                Consultant consultant = new Consultant();
                consultant.setUser(user);
                consultant.setState(Consultant.PENDING_EI_APPROVAL);
                session.save(consultant);
                session.getTransaction().commit();
                userCreationResponse = new UserCreationResponse(user.getUserID());
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

    public List<EIConsultant> getPendingConsultants() {
        List<EIConsultant> consultants = new ArrayList<EIConsultant>();
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from Consultant where state = ?").setInteger(0, Consultant.PENDING_EI_APPROVAL).list();
            for (Object obj : results) {
                Consultant consultant = (Consultant) obj;
                EIConsultant eiConsultant = consultant.toEIConsultant();
                Account account = consultant.getUser().getAccount();
                eiConsultant.setAccountName(account.getName());
                eiConsultant.setAccountID(account.getAccountID());
                consultants.add(eiConsultant);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return consultants;
    }

    public void eiApproveConsultant(long consultantID) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from Consultant where guestUserID = ?").setLong(0, consultantID).list();
            Consultant consultant = (Consultant) results.get(0);
            consultant.setState(Consultant.ACTIVE);
            session.update(consultant);
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public void deactivateConsultant(long consultantID) {
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from Consultant where guestUserID = ?").setLong(0, consultantID).list();
            Consultant consultant = (Consultant) results.get(0);
            consultant.setState(Consultant.DISABLED);
            session.update(consultant);
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public void removeConsultant(long consultantID) {
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from Consultant where guestUserID = ?").setLong(0, consultantID).list();
            Consultant consultant = (Consultant) results.get(0);
            session.delete(consultant);
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public void eiNewBizAccount(AccountTransferObject accountTransferObject, UserTransferObject initialUser) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        Session session = Database.instance().createSession();
        Account account;
        User user;
        try {
            session.getTransaction().begin();
            account = accountTransferObject.toAccount();
            configureNewAccount(account);
            account.setAccountState(Account.ACTIVE);
            initialUser.setAccountAdmin(true);
            initialUser.setDataSourceCreator(true);
            initialUser.setInsightCreator(true);
            String password = RandomTextGenerator.generateText(12);
            user = createInitialUser(initialUser, password, account);
            account.addUser(user);
            session.save(account);
            user.setAccount(account);
            session.update(user);
            session.getTransaction().commit();
            new AccountMemberInvitation().newProAccount(user.getEmail(), user.getUserName(), password);
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        long groupID;
        Connection conn = Database.instance().getConnection();
        try {
            Group group = new Group();
            group.setName(account.getName());
            group.setPubliclyVisible(false);
            group.setPubliclyJoinable(false);
            group.setDescription("This group was automatically created to act as a location for exposing data to all users in the account.");
            groupID = new GroupStorage().addGroup(group, user.getUserID(), conn);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            account.setGroupID(groupID);
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

    public void eiPrepareBizAccount(AccountTransferObject accountTransferObject, UserTransferObject initialConsultant, String consultantPassword) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        Connection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            Account account = accountTransferObject.toAccount();
            configureNewAccount(account);
            account.setAccountState(Account.PREPARING);
            initialConsultant.setAccountAdmin(true);
            initialConsultant.setDataSourceCreator(true);
            initialConsultant.setInsightCreator(true);
            User user = createInitialUser(initialConsultant, consultantPassword, account);
            Consultant consultant = new Consultant();
            consultant.setUser(user);
            consultant.setState(Consultant.ACTIVE);
            session.save(consultant);
            account.getGuestUsers().add(consultant);
            session.save(account);
            user.setAccount(account);
            session.update(user);
            if (account.getAccountType() == Account.PROFESSIONAL || account.getAccountType() == Account.ENTERPRISE) {
                Group group = new Group();
                group.setName(account.getName());
                group.setPubliclyVisible(false);
                group.setPubliclyJoinable(false);
                group.setDescription("This group was automatically created to act as a location for exposing data to all users in the account.");
                account.setGroupID(new GroupStorage().addGroup(group, user.getUserID(), conn));
                session.update(account);
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            throw new RuntimeException(e);
        } finally {
            session.close();
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            Database.instance().closeConnection(conn);
        }
    }

    public void eiActivateBizAccount(long accountID, UserTransferObject adminUser, boolean preserveConsultants) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from Account where accountID = ?").setLong(0, accountID).list();
            Account account = (Account) results.get(0);
            if (preserveConsultants) {
                for (Consultant consultant : account.getGuestUsers()) {
                    consultant.getUser().setAccountAdmin(false);
                    consultant.getUser().setDataSourceCreator(false);
                    consultant.getUser().setInsightCreator(false);
                }
            } else {
                for (Consultant consultant : account.getGuestUsers()) {
                    session.delete(consultant);
                }
                account.setGuestUsers(new ArrayList<Consultant>());
            }
            String password = RandomTextGenerator.generateText(12);
            User user = createInitialUser(adminUser, password, account);
            user.setAccount(account);
            account.addUser(user);
            session.save(user);
            account.setAccountState(Account.ACTIVE);
            session.update(account);
            session.getTransaction().commit();
            if (preserveConsultants && account.getGuestUsers().size() > 0) {
                Consultant consultant = account.getGuestUsers().get(0);
                new AccountMemberInvitation().newProAccountWithConsultant(user.getEmail(), user.getUserName(), password, consultant.getUser().getName(),
                        consultant.getUser().getEmail());
            } else {
                new AccountMemberInvitation().newProAccount(user.getEmail(), user.getUserName(), password);
            }
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
        String message = doesUserExist(userTransferObject.getUserName(), userTransferObject.getEmail());
        if (message != null) {
            userCreationResponse = new UserCreationResponse(message);
        } else {
            Session session = Database.instance().createSession();
            Account account;
            User user;
            try {
                session.beginTransaction();
                List results = session.createQuery("from Account where accountID = ?").setLong(0, accountID).list();
                account = (Account) results.get(0);
                User admin = (User) session.createQuery("from User where userID = ?").setLong(0, SecurityUtil.getUserID()).list().get(0);
                user = userTransferObject.toUser();
                user.setAccount(account);
                final String password = RandomTextGenerator.generateText(12);
                final String adminName = admin.getName();
                final String userEmail = user.getEmail();
                final String userName = user.getUserName();
                user.setPassword(PasswordService.getInstance().encrypt(password));
                account.addUser(user);                
                user.setAccount(account);
                session.update(account);
                session.getTransaction().commit();
                new Thread(new Runnable() {
                    public void run() {
                        new AccountMemberInvitation().sendAccountEmail(userEmail, adminName, userName, password);
                    }
                }).start();
                userCreationResponse = new UserCreationResponse(user.getUserID());
            } catch (Exception e) {
                LogClass.error(e);
                session.getTransaction().rollback();
                throw new RuntimeException(e);
            } finally {
                session.close();
            }
            if (account.getAccountType() == Account.PROFESSIONAL || account.getAccountType() == Account.ENTERPRISE) {
                try {
                    new GroupStorage().addUserToGroup(user.getUserID(), account.getGroupID(), userTransferObject.isAccountAdmin() ? Roles.OWNER : Roles.SUBSCRIBER);
                } catch (Exception e) {
                    LogClass.error(e);
                }
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
                userServiceResponse = new UserServiceResponse(true, user.getUserID(), user.getAccount().getAccountID(), user.getName(), user.getAccount().getAccountType(), 50000000, user.getEmail(),
                        user.getUserName(), encryptedPassword, user.isAccountAdmin(), user.isDataSourceCreator(), user.isInsightCreator());
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
            Account account = user.getAccount();
            return new UserServiceResponse(true, user.getUserID(), user.getAccount().getAccountID(), user.getName(),
                                account.getAccountType(), account.getMaxSize(), user.getEmail(), user.getUserName(),
                                user.getPassword(), user.isAccountAdmin(), user.isDataSourceCreator(), user.isInsightCreator());
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
                        List accountResults = session.createQuery("from Account where accountID = ?").setLong(0, user.getAccount().getAccountID()).list();
                        Account account = (Account) accountResults.get(0);
                        if (account.getAccountState() == Account.ACTIVE) {
                            userServiceResponse = new UserServiceResponse(true, user.getUserID(), user.getAccount().getAccountID(), user.getName(),
                                user.getAccount().getAccountType(), account.getMaxSize(), user.getEmail(), user.getUserName(), encryptedPassword, user.isAccountAdmin(), user.isDataSourceCreator(), user.isInsightCreator());
                        } else {
                            userServiceResponse = new UserServiceResponse(false, "Your account is not active.");
                        }
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
    public UserServiceResponse authenticateAdmin(String userName, String password) {
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
                        List accountResults = session.createQuery("from Account where accountID = ?").setLong(0, user.getAccount().getAccountID()).list();
                        Account account = (Account) accountResults.get(0);
                        if (account.getAccountType() != Account.ADMINISTRATOR) {
                            throw new SecurityException();
                        }
                        userServiceResponse = new UserServiceResponse(true, user.getUserID(), user.getAccount().getAccountID(), user.getName(),
                                user.getAccount().getAccountType(), account.getMaxSize(), user.getEmail(), user.getUserName(), encryptedPassword, user.isAccountAdmin(), user.isDataSourceCreator(), user.isInsightCreator());
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
                        List accountResults = session.createQuery("from Account where accountID = ?").setLong(0, user.getAccount().getAccountID()).list();
                        Account account = (Account) accountResults.get(0);
                        userServiceResponse = new UserServiceResponse(true, user.getUserID(), user.getAccount().getAccountID(), user.getName(),
                                user.getAccount().getAccountType(), account.getMaxSize(), user.getEmail(), user.getUserName(), encryptedPassword, user.isAccountAdmin(), user.isDataSourceCreator(), user.isInsightCreator());
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

    public boolean activateAccount(String activationID) {
        boolean activated = false;
        Connection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            PreparedStatement queryStmt = conn.prepareStatement("SELECT ACCOUNT_ID FROM ACCOUNT_ACTIVATION WHERE ACTIVATION_KEY = ?");
            queryStmt.setString(1, activationID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long accountID = rs.getLong(1);
                List results = session.createQuery("from Account where accountID = ?").setLong(0, accountID).list();
                Account account = (Account) results.get(0);
                account.setAccountState(Account.ACTIVE);
                session.update(account);
                session.flush();
                activated = true;
            }
            conn.commit();
        } catch (SQLException e) {
            LogClass.error(e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
        } finally {
            session.close();
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            Database.instance().closeConnection(conn);
        }
        return activated;
    }

    public List<AccountAdminTO> getAccounts() {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        List<AccountAdminTO> accounts = new ArrayList<AccountAdminTO>();
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            List results = session.createQuery("from Account").list();
            for (Object obj : results) {
                Account account = (Account) obj;
                accounts.add(account.toAdminTO());
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return accounts;
    }
}
