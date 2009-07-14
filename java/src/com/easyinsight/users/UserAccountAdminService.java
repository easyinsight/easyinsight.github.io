package com.easyinsight.users;

import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.PasswordService;
import com.easyinsight.security.Roles;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.util.RandomTextGenerator;
import com.easyinsight.email.AccountMemberInvitation;
import com.easyinsight.logging.LogClass;
import com.easyinsight.groups.GroupStorage;
import org.hibernate.Session;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Date;
import java.util.Calendar;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Apr 27, 2009
 * Time: 3:15:01 PM
 */
public class UserAccountAdminService {

    public AccountAPISettings regenerateSecretKey() {
        long accountID = SecurityUtil.getAccountID();
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            Account account = (Account) session.createQuery("from Account where accountID = ?").setLong(0, accountID).list().get(0);
            String accountSecretKey = RandomTextGenerator.generateText(16);
            account.setAccountSecretKey(accountSecretKey);
            session.save(account);
            AccountAPISettings settings = new AccountAPISettings(account.getAccountKey(), account.getAccountSecretKey(),
                    account.isUncheckedAPIEnabled(), account.isValidatedAPIEnabled(), account.isDynamicAPIAllowed());
            session.getTransaction().commit();
            return settings;
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
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

    public AccountInfo getAccountInfo() {
        AccountInfo accountInfo = new AccountInfo();
        long accountID = SecurityUtil.getAccountID();
        AccountActivityStorage storage = new AccountActivityStorage();
        Connection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            List results = session.createQuery("from Account where accountID = ?").setLong(0, accountID).list();
            Account account = (Account) results.get(0);
            accountInfo.setAccountType(account.getAccountType());            
            accountInfo.setAccountState(account.getAccountState());
            if (account.getAccountState() == Account.TRIAL) {
                Date trialDate = storage.getTrialTime(accountID, conn);
                accountInfo.setTrialEndDate(trialDate);
            }
        } catch (SQLException e) {
            LogClass.error(e);
        } finally {
            session.close();
            Database.instance().closeConnection(conn);
        }
        return accountInfo;
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
            if (!SecurityUtil.isAccountAdmin()) {
                if (user.getUserID() != SecurityUtil.getUserID()) {
                    throw new SecurityException();
                }
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

    public UserCreationResponse addUserToAccount(UserTransferObject userTransferObject) {
        SecurityUtil.authorizeAccountAdmin();
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
            if (account.getAccountType() == Account.GROUP || account.getAccountType() == Account.PROFESSIONAL || account.getAccountType() == Account.ENTERPRISE) {
                try {
                    new GroupStorage().addUserToGroup(user.getUserID(), account.getGroupID(), userTransferObject.isAccountAdmin() ? Roles.OWNER : Roles.SUBSCRIBER);
                } catch (Exception e) {
                    LogClass.error(e);
                }
            }
        }
        return userCreationResponse;
    }

    public UpgradeAccountResponse upgradeAccount(int toType) {
        UpgradeAccountResponse response = new UpgradeAccountResponse();
        if (toType == Account.ADMINISTRATOR) {
            throw new SecurityException();
        }
        SecurityUtil.authorizeAccountAdmin();
        long accountID = SecurityUtil.getAccountID();
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            Account account = (Account) session.createQuery("from Account where accountID = ?").setLong(0, accountID).list().get(0);
            User user = (User) session.createQuery("from User where userID = ?").setLong(0, SecurityUtil.getUserID()).list().get(0);
            account.setAccountType(toType);
            Date trialEnd = new AccountActivityStorage().getTrialTime(account.getAccountID(), conn);
            if(trialEnd != null && trialEnd.after(new Date()) && account.getBillingDayOfMonth() == null) {
                Calendar c = Calendar.getInstance();
                c.setTime(trialEnd);
                account.setBillingDayOfMonth(c.get(Calendar.DAY_OF_MONTH));
            }
            else {
                if(account.isBillingInformationGiven() == null || !account.isBillingInformationGiven()) {
                    account.setAccountState(Account.DELINQUENT);
                }
            }
            if(account.isBillingInformationGiven() == null || !account.isBillingInformationGiven())
                response.setBillingInformationNeeded(true);

            if (toType == Account.GROUP || toType == Account.PROFESSIONAL || toType == Account.ENTERPRISE) {
                user.setAccountAdmin(true);
                user.setDataSourceCreator(true);
                user.setInsightCreator(true);
                session.update(user);
            }
            session.update(account);
            account.toTransferObject();
            session.flush();
            conn.commit();
            response.setUser(user.toUserTransferObject());
            return response;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void deleteUser(long userID) {
        SecurityUtil.authorizeAccountAdmin();
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            List results = session.createQuery("from User where userID = ?").setLong(0, userID).list();
            if (results.size() > 0) {
                User user = (User) results.get(0);
                if (SecurityUtil.getAccountID() != user.getAccount().getAccountID()) {
                    throw new SecurityException();
                }
                session.delete(user);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public AccountAPISettings getAccountAPISettings() {
        AccountAPISettings accountAPISettings;
        long accountID = SecurityUtil.getAccountID();
        SecurityUtil.authorizeAccountAdmin();
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from Account where accountID = ?").setLong(0, accountID).list();
            Account account = (Account) results.get(0);
            boolean changed = false;
            if (account.getAccountKey() == null) {
                String accountKey = RandomTextGenerator.generateText(12);
                account.setAccountKey(accountKey);
                changed = true;
            }
            if (account.getAccountSecretKey() == null) {
                String accountSecretKey = RandomTextGenerator.generateText(16);
                account.setAccountSecretKey(accountSecretKey);
                changed = true;
            }
            if (changed) {
                session.update(account);
            }
            accountAPISettings = new AccountAPISettings(account.getAccountKey(), account.getAccountSecretKey(),
                    account.isUncheckedAPIEnabled(), account.isValidatedAPIEnabled(), account.isDynamicAPIAllowed());
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return accountAPISettings;
    }

    public String activateAccount(String activationID) {
        Connection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            PreparedStatement queryStmt = conn.prepareStatement("SELECT ACCOUNT_ID, target_url FROM ACCOUNT_ACTIVATION WHERE ACTIVATION_KEY = ?");
            queryStmt.setString(1, activationID);
            String url = null;            
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long accountID = rs.getLong(1);
                url = rs.getString(2);
                List results = session.createQuery("from Account where accountID = ?").setLong(0, accountID).list();
                Account account = (Account) results.get(0);
                if (account.getAccountType() == Account.FREE) {
                    account.setAccountState(Account.ACTIVE);
                } else {
                    account.setAccountState(Account.TRIAL);                    
                }
                session.update(account);
                session.flush();
                if (account.getAccountType() == Account.FREE) {
                    new AccountActivityStorage().saveAccountActivity(new AccountActivity(account.getAccountType(),
                        new Date(), account.getAccountID(), 0, AccountActivity.ACCOUNT_CREATED, "", 0, 0, Account.ACTIVE), conn);
                } else {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DAY_OF_YEAR, 30);
                    new AccountActivityStorage().saveAccountActivity(new AccountActivity(account.getAccountType(),
                        new Date(), account.getAccountID(), 0, AccountActivity.ACCOUNT_CREATED, "", 0, 0, Account.TRIAL), conn);
                    new AccountActivityStorage().saveAccountTimeChange(account.getAccountID(), Account.ACTIVE, cal.getTime(), conn);
                }
            }
            conn.commit();
            return url;
        } catch (SQLException e) {
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

    public AccountStats getAccountStats() {
        long accountID = SecurityUtil.getAccountID();
        long usedSize = 0;
        long maxSize = 0;
        int currentUsers = 0;
        int maxUsers = 0;
        long usedAPI = 0;
        long maxAPI = Account.getMaxCount(SecurityUtil.getAccountTier());
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryUsedStmt = conn.prepareStatement("select sum(feed_persistence_metadata.size) from feed_persistence_metadata, " +
                    "upload_policy_users, user where feed_persistence_metadata.feed_id = upload_policy_users.feed_id and user.user_id = upload_policy_users.user_id and user.account_id = ?");
            queryUsedStmt.setLong(1, accountID);
            ResultSet rs = queryUsedStmt.executeQuery();
            if (rs.next()) {
                usedSize = rs.getLong(1);
            }
            PreparedStatement usersStmt = conn.prepareStatement("SELECT count(user_id) from user where account_id = ?");
            usersStmt.setLong(1, accountID);
            ResultSet usersRS = usersStmt.executeQuery();
            if (usersRS.next()) {
                currentUsers = usersRS.getInt(1);
            }
            PreparedStatement apiTodayStmt = conn.prepareStatement("SELECT used_bandwidth from bandwidth_usage where account_id = ? AND bandwidth_date = ?");
            apiTodayStmt.setLong(1, accountID);
            apiTodayStmt.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            ResultSet apiRS = apiTodayStmt.executeQuery();
            if (apiRS.next()) {
                usedAPI = apiRS.getLong(1);
            }
            PreparedStatement accountStmt = conn.prepareStatement("SELECT max_size, max_users from account WHERE account_id = ?");
            accountStmt.setLong(1, accountID);
            ResultSet spaceRS = accountStmt.executeQuery();
            if (spaceRS.next()) {
                maxSize = spaceRS.getLong(1);
                maxUsers = spaceRS.getInt(2);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        AccountStats accountStats = new AccountStats();
        accountStats.setMaxSpace(maxSize);
        accountStats.setUsedSpace(usedSize);
        accountStats.setCurrentUsers(currentUsers);
        accountStats.setAvailableUsers(maxUsers);
        accountStats.setApiUsedToday(usedAPI);
        accountStats.setApiMaxToday(maxAPI);
        return accountStats;
    }

    public void saveAccountAPISettings(AccountAPISettings accountAPISettings) {
        long accountID = SecurityUtil.getAccountID();
        SecurityUtil.authorizeAccountAdmin();
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from Account where accountID = ?").setLong(0, accountID).list();
            Account account = (Account) results.get(0);
            account.setUncheckedAPIEnabled(accountAPISettings.isUncheckedAPIEnabled());
            account.setValidatedAPIEnabled(accountAPISettings.isValidatedAPIEnabled());
            account.setDynamicAPIAllowed(accountAPISettings.isDynamicAPIEnabled());
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
        if (SecurityUtil.getAccountID() != accountTransferObject.getAccountID()) {
            throw new SecurityException();
        }
        SecurityUtil.authorizeAccountAdmin();
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
}
