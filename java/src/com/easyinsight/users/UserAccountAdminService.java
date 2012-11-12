package com.easyinsight.users;

import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.groups.Group;
import com.easyinsight.preferences.ImageDescriptor;
import com.easyinsight.preferences.UserDLS;
import com.easyinsight.preferences.UserDLSFilter;
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
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * User: James Boe
 * Date: Apr 27, 2009
 * Time: 3:15:01 PM
 */
public class UserAccountAdminService {

    public String regenerateAccountSecretKey() {
        try {
            return RandomTextGenerator.generateText(16);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String regenerateUserSecretKey() {
        try {
            return RandomTextGenerator.generateText(16);
        } catch (Exception e) {
            throw new RuntimeException(e);
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

    public AccountAreaInfo getAccountAreaInfo() {
        SecurityUtil.authorizeAccountAdmin();
        long accountID = SecurityUtil.getAccountID();
        AccountAreaInfo accountAreaInfo = new AccountAreaInfo();
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            List results = session.createQuery("from Account where accountID = ?").setLong(0, accountID).list();
            Account account = (Account) results.get(0);
            accountAreaInfo.setAddressLine1(account.getAddressLine1());
            accountAreaInfo.setAddressLine2(account.getAddressLine2());
            accountAreaInfo.setCity(account.getCity());
            accountAreaInfo.setState(account.getState());
            accountAreaInfo.setPostalCode(account.getPostalCode());
            accountAreaInfo.setCountry(account.getCountry());
            accountAreaInfo.setVat(account.getVat());
            accountAreaInfo.setCompanyName(account.getName());
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return accountAreaInfo;
    }

    public String updateAccountAreaInfo(AccountAreaInfo accountAreaInfo) {
        SecurityUtil.authorizeAccountAdmin();
        long accountID = SecurityUtil.getAccountID();
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            List results = session.createQuery("from Account where accountID = ?").setLong(0, accountID).list();
            Account account = (Account) results.get(0);
            if (accountAreaInfo.getCompanyName() == null || accountAreaInfo.getCompanyName().trim().length() < 1) {
                session.getTransaction().commit();
                return "Invalid company name.";
            }
            if (!account.getName().equals(accountAreaInfo.getCompanyName())) {
                List existing = session.createQuery("from Account where name = ?").setString(0, accountAreaInfo.getCompanyName()).list();
                if (existing.size() > 0) {
                    session.getTransaction().commit();
                    return "That name is already taken.";
                }
            }
            account.setVat(accountAreaInfo.getVat());
            account.setAddressLine1(accountAreaInfo.getAddressLine1());
            account.setAddressLine2(accountAreaInfo.getAddressLine2());
            account.setCity(accountAreaInfo.getCity());
            account.setState(accountAreaInfo.getState());
            account.setPostalCode(accountAreaInfo.getPostalCode());
            account.setCountry(accountAreaInfo.getCountry());
            account.setName(accountAreaInfo.getCompanyName());
            session.update(account);
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
        EIConnection conn = Database.instance().getConnection();
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
            accountInfo.setAccount(account.toTransferObject());
            accountInfo.setAccountStats(getAccountStats(conn));
        } catch (SQLException e) {
            LogClass.error(e);
        } finally {
            session.close();
            Database.closeConnection(conn);
        }
        return accountInfo;
    }

    public void updateUser(UserTransferObject userTransferObject, List<UserDLS> userDLSList) {
        long accountID = SecurityUtil.getAccountID();
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
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
            user.update(userTransferObject);
            user.setUpdatedOn(new Date());
            session.update(user);
            session.flush();
            PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM USER_DLS WHERE USER_ID = ?");
            clearStmt.setLong(1, userTransferObject.getUserID());
            clearStmt.executeUpdate();
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO USER_DLS (DLS_ID, USER_ID) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            PreparedStatement insertFilterStmt = conn.prepareStatement("INSERT INTO USER_DLS_TO_FILTER (FILTER_ID, ORIGINAL_FILTER_ID, USER_DLS_ID) VALUES (?, ?, ?)");
            for (UserDLS userDLS : userDLSList) {
                insertStmt.setLong(1, userDLS.getDlsID());
                insertStmt.setLong(2, userTransferObject.getUserID());
                insertStmt.execute();
                long userDLSID = Database.instance().getAutoGenKey(insertStmt);
                for (UserDLSFilter userDLSFilter : userDLS.getUserDLSFilterList()) {
                    FilterDefinition filterDefinition = userDLSFilter.getFilterDefinition();
                    filterDefinition.beforeSave(session);
                    session.saveOrUpdate(filterDefinition);
                    session.flush();
                    insertFilterStmt.setLong(1, filterDefinition.getFilterID());
                    insertFilterStmt.setLong(2, userDLSFilter.getOriginalFilterID());
                    insertFilterStmt.setLong(3, userDLSID);
                    insertFilterStmt.execute();
                }
            }

            conn.commit();
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

    public void updateUsers(List<UserTransferObject> userTransferObject) {
        SecurityUtil.authorizeAccountAdmin();
        long accountID = SecurityUtil.getAccountID();
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            List results = session.createQuery("from Account where accountID = ?").setLong(0, accountID).list();
            Account account = (Account) results.get(0);
            for (User checkingUser : account.getUsers()) {
                for (UserTransferObject transferObject : userTransferObject) {
                    if (checkingUser.getUserID() == transferObject.getUserID()) {
                        checkingUser.update(transferObject);
                        session.update(checkingUser);
                    }
                }
            }            
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
                } results = session.createQuery("from Account where name = ?").setString(0, accountName).list();
                    if (results.size() > 0) {
                        message = "That company name is already used.";
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

    public List<InvoiceInfo> retrieveInvoices() {
        List<InvoiceInfo> invoices = new ArrayList<InvoiceInfo>();
        Session session = Database.instance().createSession();
        try {
            List accountResults = session.createQuery("from Account where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list();
            Account account = (Account) accountResults.get(0);
            @SuppressWarnings({"unchecked"}) List<AccountCreditCardBillingInfo> results = session.createQuery("from AccountCreditCardBillingInfo where accountId = ? and responseCode = ? and amount > ?").setLong(0, SecurityUtil.getAccountID()).setString(1, "100").setDouble(2, 20.).list();
            for (AccountCreditCardBillingInfo info : results) {
                invoices.add(new InvoiceInfo(info.getTransactionTime(), info.toInvoiceText(account)));
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return invoices;
    }

    @Nullable
    private String doesUserExist(String userName, String email) {
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

    public void importUsers(List<SuggestedUser> users) {
        SecurityUtil.authorizeAccountAdmin();
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            List results = session.createQuery("from Account where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list();
            Account account = (Account) results.get(0);
            User admin = (User) session.createQuery("from User where userID = ?").setLong(0, SecurityUtil.getUserID()).list().get(0);
            int maxUsers = account.getMaxUsers();
            int currentUsers = account.getUsers().size();
            if ((currentUsers + users.size()) >= maxUsers) {
                throw new RuntimeException("You are at the maximum number of users for your account.");
            } else {
                Map<String, String> passwordMap = new HashMap<String, String>();
                Map<String, User> userMap = new HashMap<String, User>();
                for (SuggestedUser suggestedUser : users) {
                    String valid = doesUserExist(suggestedUser.getUserName(), suggestedUser.getEmailAddress());
                    if (valid == null) {
                        User user = new User(suggestedUser.getUserName(), null, suggestedUser.getLastName(),
                                suggestedUser.getEmailAddress());
                        user.setFirstName(suggestedUser.getFirstName());
                        final String password = RandomTextGenerator.generateText(12);
                        userMap.put(user.getUserName(), user);
                        passwordMap.put(user.getUserName(), password);
                        user.setPassword(PasswordService.getInstance().encrypt(password, user.getHashSalt(), "SHA-256"));
                        user.setHashType("SHA-256");
                        user.setAccount(account);
                        account.addUser(user);
                    }
                }
                final String accountName = account.getName();
                final String loginURL = "https://www.easy-insight.com/app";
                final String adminEmail = admin.getEmail();
                session.update(account);
                session.getTransaction().commit();
                for (Map.Entry<String, User> entry : userMap.entrySet()) {
                    final String password = passwordMap.get(entry.getKey());
                    final String userEmail = entry.getValue().getEmail();
                    final String adminFirstName = admin.getFirstName();
                    final String adminName = admin.getName();
                    final String userName = entry.getValue().getUserName();
                    new Thread(new Runnable() {
                        public void run() {
                            new AccountMemberInvitation().sendAccountEmail(userEmail, adminFirstName, adminName, userName, password, accountName, loginURL, adminEmail, "");
                        }
                    }).start();
                }
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public static final int DEFAULT = 1;
    public static final int GOOGLE_APPS = 2;

    public UserCreationResponse addUserToAccount(UserTransferObject userTransferObject, List<UserDLS> userDLSList, boolean requirePasswordChange, final int source) {
        SecurityUtil.authorizeAccountAdmin();
        long accountID = SecurityUtil.getAccountID();
        UserCreationResponse userCreationResponse;
        String message = doesUserExist(userTransferObject.getUserName(), userTransferObject.getEmail());
        if (message != null) {
            if (source == GOOGLE_APPS) {
                // just ignore in the case of google apps linking to an existing user
                return null;
            }
            userCreationResponse = new UserCreationResponse(message);
        } else {
            EIConnection conn = Database.instance().getConnection();
            Session session = Database.instance().createSession(conn);
            Account account;
            User user = null;
            try {
                conn.setAutoCommit(false);
                List results = session.createQuery("from Account where accountID = ?").setLong(0, accountID).list();
                account = (Account) results.get(0);
                int maxUsers = account.getMaxUsers();
                int currentUsers = account.getUsers().size();
                if (currentUsers >= maxUsers) {
                    userCreationResponse = new UserCreationResponse("You are at the maximum number of users for your account.");
                } else {
                    User admin = (User) session.createQuery("from User where userID = ?").setLong(0, SecurityUtil.getUserID()).list().get(0);
                    user = userTransferObject.toUser();
                    user.setAccount(account);
                    final String adminFirstName = admin.getFirstName();
                    final String adminName = admin.getName();
                    final String userEmail = user.getEmail();
                    final String userName = user.getUserName();
                    final String password = RandomTextGenerator.generateText(12);
                    final String accountName = account.getName();
                    final String loginURL;
                    if (account.isSubdomainEnabled()) {
                        loginURL = "https://therapyworks.easy-insight.com/";
                    } else {
                        loginURL = "https://www.easy-insight.com/app";
                    }
                    final String adminEmail = admin.getEmail();
                    user.setPassword(PasswordService.getInstance().encrypt(password, user.getHashSalt(), "SHA-256"));
                    user.setHashType("SHA-256");
                    user.setInitialSetupDone(!requirePasswordChange);
                    account.addUser(user);
                    final String sso;
                    if (account.getExternalLogin() != null) {
                        sso = account.getExternalLogin().toSSOMessage();
                    } else {
                        sso = "";
                    }
                    user.setAccount(account);
                    session.update(account);
                    session.flush();
                    PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO USER_DLS (DLS_ID, USER_ID) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                    PreparedStatement insertFilterStmt = conn.prepareStatement("INSERT INTO USER_DLS_TO_FILTER (FILTER_ID, ORIGINAL_FILTER_ID, USER_DLS_ID) VALUES (?, ?, ?)");
                    for (UserDLS userDLS : userDLSList) {
                        insertStmt.setLong(1, userDLS.getDlsID());
                        insertStmt.setLong(2, user.getUserID());
                        insertStmt.execute();
                        long userDLSID = Database.instance().getAutoGenKey(insertStmt);
                        for (UserDLSFilter userDLSFilter : userDLS.getUserDLSFilterList()) {
                            FilterDefinition filterDefinition = userDLSFilter.getFilterDefinition();
                            filterDefinition.beforeSave(session);
                            session.saveOrUpdate(filterDefinition);
                            session.flush();
                            insertFilterStmt.setLong(1, filterDefinition.getFilterID());
                            insertFilterStmt.setLong(2, userDLSFilter.getOriginalFilterID());
                            insertFilterStmt.setLong(3, userDLSID);
                            insertFilterStmt.execute();
                        }
                    }
                    conn.commit();
                    new Thread(new Runnable() {
                        public void run() {
                            if (source == GOOGLE_APPS) {
                                new AccountMemberInvitation().sendGoogleAppsAccountEmail(userEmail, adminFirstName, adminName, accountName, adminEmail);
                            } else {
                                new AccountMemberInvitation().sendAccountEmail(userEmail, adminFirstName, adminName, userName, password, accountName, loginURL, adminEmail, sso);
                            }
                        }
                    }).start();
                    userCreationResponse = new UserCreationResponse(user.getUserID());
                }
            } catch (Exception e) {
                LogClass.error(e);
                conn.rollback();
                throw new RuntimeException(e);
            } finally {
                session.close();
                conn.setAutoCommit(true);
                Database.closeConnection(conn);
            }
            if (user != null && account.getAccountType() != Account.PERSONAL) {
                try {
                    if (account.getGroupID() != null) {
                        new GroupStorage().addUserToGroup(user.getUserID(), account.getGroupID(), userTransferObject.isAccountAdmin() ? Roles.OWNER : Roles.SUBSCRIBER);
                    }
                } catch (Exception e) {
                    LogClass.error(e);
                }
            }
        }
        return userCreationResponse;
    }

    public UserCreationResponse addUserToAccount(UserTransferObject userTransferObject, List<UserDLS> userDLSList, boolean requirePasswordChange) {
        return addUserToAccount(userTransferObject, userDLSList, requirePasswordChange, DEFAULT);
    }

    /*
    on pricing change

     */



    public void downgradeAccount(int toType) {
        if (toType == Account.ADMINISTRATOR) {
            throw new SecurityException();
        }
        SecurityUtil.authorizeAccountAdmin();
        if (toType >= Account.PREMIUM || SecurityUtil.getAccountTier() >= Account.PREMIUM) {
            throw new RuntimeException("You'll need Easy Insight support to deal with this account type change.");
        }
        long accountID = SecurityUtil.getAccountID();
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            Account account = (Account) session.createQuery("from Account where accountID = ?").setLong(0, accountID).list().get(0);            

            if (toType == Account.PERSONAL) {
                account.setAccountState(Account.ACTIVE);
                account.setBillingDayOfMonth(null);
                if (account.getGroupID() != null) {
                    new GroupStorage().deleteGroup(account.getGroupID(), conn);
                }
            } else {
                // is there anything we need to do here?
                
            }

            AccountLimits.configureAccount(account);

            account.setAccountType(toType);

            session.update(account);
            
            session.flush();
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void welcomeBack(WelcomeBackInfo welcomeBackInfo) {
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            User user = (User) session.createQuery("from User where userID = ?").setLong(0, SecurityUtil.getUserID()).list().get(0);
            Account account = user.getAccount();
            if (account.getAccountState() == Account.REACTIVATION_POSSIBLE) {
                
                user.setFirstName(welcomeBackInfo.getFirstName());
                user.setName(welcomeBackInfo.getLastName());
                user.setAccountAdmin(true);

                account.setName(welcomeBackInfo.getAccountName());
                user.setUpdatedOn(new Date());
                session.update(user);
                session.flush();
                new AccountActivityStorage().generateSalesEmailSchedules(user.getUserID(), conn);
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public UpgradeAccountResponse upgradeAccount(int toType, int numberDesigners, long maxStorage) {

        if (toType == Account.ADMINISTRATOR) {
            throw new SecurityException();
        }
        if (toType == Account.PREMIUM || toType == Account.ENTERPRISE) {
            throw new RuntimeException("You'll need Easy Insight support to upgrade your account to these tiers.");
        }
        SecurityUtil.authorizeAccountAdmin();
        long accountID = SecurityUtil.getAccountID();
        /*if (toType < SecurityUtil.getAccountTier()) {
            throw new RuntimeException();
        }*/
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            Account account = (Account) session.createQuery("from Account where accountID = ?").setLong(0, accountID).list().get(0);

            User user = (User) session.createQuery("from User where userID = ?").setLong(0, SecurityUtil.getUserID()).list().get(0);

            UpgradeAccountResponse response = updateAccount(toType, conn, session, account, user, numberDesigners, maxStorage);
            session.flush();
            conn.commit();
            return response;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    private UpgradeAccountResponse updateAccount(int toType, EIConnection conn, Session session, Account account, User user, int numberDesigners, long maxStorage) throws SQLException {
        UpgradeAccountResponse response = new UpgradeAccountResponse();

        if (account.getUsers().size() > numberDesigners) {
            response.setSuccessful(false);
            response.setResultMessage("You currently have " + account.getUsers().size() + " users on the account. You'll need to reduce the account to " + numberDesigners + " before making this account change.");
        } else {
            account.setAccountType(toType);
            AccountLimits.configureAccount(account);
            account.setMaxUsers(numberDesigners);
            if (account.getAccountType() >= Account.PROFESSIONAL) {
                account.setMaxSize(maxStorage);
            }
            response.setNewAccountType(toType);
            account.setUpgraded(true);
            session.update(account);
            SecurityUtil.changeAccountType(toType);
            response.setSuccessful(true);
            response.setUser(user.toUserTransferObject());
        }

        return response;
    }

    public void deleteUser(long userID) {
        SecurityUtil.authorizeAccountAdmin();
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            PreparedStatement findReportStmt = conn.prepareStatement("SELECT ANALYSIS_ID FROM USER_TO_ANALYSIS WHERE USER_ID = ?");
            PreparedStatement findUserReportStmt = conn.prepareStatement("SELECT USER_ID FROM USER_TO_ANALYSIS WHERE ANALYSIS_ID = ?");
            findReportStmt.setLong(1, userID);
            ResultSet reportRS = findReportStmt.executeQuery();
            Set<Long> reportIDs = new HashSet<Long>();
            while (reportRS.next()) {
                long reportID = reportRS.getLong(1);
                findUserReportStmt.setLong(1, reportID);
                ResultSet userRS = findUserReportStmt.executeQuery();
                boolean add = true;
                while (userRS.next()) {
                    long ownerID = userRS.getLong(1);
                    if (ownerID == SecurityUtil.getUserID()) {
                        add = false;
                    }
                }
                if (add) {
                    reportIDs.add(reportID);
                }
            }
            PreparedStatement findDashboardStmt = conn.prepareStatement("SELECT DASHBOARD_ID FROM USER_TO_DASHBOARD WHERE USER_ID = ?");
            PreparedStatement findUserDashboardStmt = conn.prepareStatement("SELECT USER_ID FROM USER_TO_DASHBOARD WHERE DASHBOARD_ID = ?");
            findDashboardStmt.setLong(1, userID);
            ResultSet dashboardDS = findDashboardStmt.executeQuery();
            Set<Long> dashboardIDs = new HashSet<Long>();
            while (dashboardDS.next()) {
                long dashboardID = dashboardDS.getLong(1);
                findUserDashboardStmt.setLong(1, dashboardID);
                ResultSet userRS = findUserDashboardStmt.executeQuery();
                boolean add = true;
                while (userRS.next()) {
                    long ownerID = userRS.getLong(1);
                    if (ownerID == SecurityUtil.getUserID()) {
                        add = false;
                    }
                }
                if (add) {
                    dashboardIDs.add(dashboardID);
                }
            }
            PreparedStatement findDataStmt = conn.prepareStatement("SELECT FEED_ID FROM UPLOAD_POLICY_USERS WHERE USER_ID = ?");
            PreparedStatement findUserDataStmt = conn.prepareStatement("SELECT USER_ID FROM UPLOAD_POLICY_USERS WHERE FEED_ID = ?");
            findDataStmt.setLong(1, userID);
            ResultSet dataSourceRS = findDataStmt.executeQuery();
            Set<Long> dataSourceIDs = new HashSet<Long>();
            while (dataSourceRS.next()) {
                long dataSourceID = dataSourceRS.getLong(1);
                findUserDataStmt.setLong(1, dataSourceID);
                ResultSet userRS = findUserDataStmt.executeQuery();
                boolean add = true;
                while (userRS.next()) {
                    long ownerID = userRS.getLong(1);
                    if (ownerID == SecurityUtil.getUserID()) {
                        add = false;
                    }
                }
                if (add) {
                    dataSourceIDs.add(dataSourceID);
                }
            }
            List results = session.createQuery("from User where userID = ?").setLong(0, userID).list();
            if (results.size() > 0) {
                User user = (User) results.get(0);
                if (SecurityUtil.getAccountID() != user.getAccount().getAccountID()) {
                    throw new SecurityException();
                }

                session.delete(user);
            }
            PreparedStatement addUserToReportStmt = conn.prepareStatement("INSERT INTO USER_TO_ANALYSIS (USER_ID, ANALYSIS_ID, RELATIONSHIP_TYPE, OPEN) VALUES (?, ?, ?, ?)");
            for (Long reportID : reportIDs) {
                addUserToReportStmt.setLong(1, SecurityUtil.getUserID());
                addUserToReportStmt.setLong(2, reportID);
                addUserToReportStmt.setInt(3, Roles.OWNER);
                addUserToReportStmt.setBoolean(4, false);
                addUserToReportStmt.execute();
            }
            PreparedStatement addUserToDashboardStmt = conn.prepareStatement("INSERT INTO USER_TO_DASHBOARD (USER_ID, DASHBOARD_ID) VALUES (?, ?)");
            for (Long dashboardID : dashboardIDs) {
                addUserToDashboardStmt.setLong(1, SecurityUtil.getUserID());
                addUserToDashboardStmt.setLong(2, dashboardID);
                addUserToDashboardStmt.execute();
            }
            PreparedStatement addUserToDataSourceStmt = conn.prepareStatement("INSERT INTO UPLOAD_POLICY_USERS (USER_ID, FEED_ID, ROLE) VALUES (?, ?, ?)");
            for (Long dataSourceID : dataSourceIDs) {
                addUserToDataSourceStmt.setLong(1, SecurityUtil.getUserID());
                addUserToDataSourceStmt.setLong(2, dataSourceID);
                addUserToDataSourceStmt.setInt(3, Roles.OWNER);
                addUserToDataSourceStmt.execute();
            }
            session.flush();
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
        } finally {
            session.close();
            Database.closeConnection(conn);
        }
    }

    public void saveAccountAPISettings(AccountAPISettings accountAPISettings) {
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from Account where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list();
            Account account = (Account) results.get(0);
            account.setApiEnabled(accountAPISettings.isApiEnabled());
            List userResults = session.createQuery("from User where userID = ?").setLong(0, SecurityUtil.getUserID()).list();
            User user = (User) userResults.get(0);
            user.setUserKey(accountAPISettings.getUserKey());
            user.setUserSecretKey(accountAPISettings.getUserSecretKey());
            user.setUpdatedOn(new Date());
            session.update(user);
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

    public AccountAPISettings getAccountAPISettings() {
        AccountAPISettings accountAPISettings;
        long accountID = SecurityUtil.getAccountID();
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
            List userResults = session.createQuery("from User where userID = ?").setLong(0, SecurityUtil.getUserID()).list();
            User user = (User) userResults.get(0);
            if (user.getUserKey() == null) {
                String accountKey = RandomTextGenerator.generateText(12);
                user.setUserKey(accountKey);
                changed = true;
            }
            if (user.getUserSecretKey() == null) {
                String accountSecretKey = RandomTextGenerator.generateText(16);
                user.setUserSecretKey(accountSecretKey);
                changed = true;
            }
            if (changed) {
                user.setUpdatedOn(new Date());
                session.update(user);
                session.update(account);
            }
            accountAPISettings = new AccountAPISettings(user.getUserKey(), user.getUserSecretKey(), account.isApiEnabled());
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

    public long getAccountStorage() throws SQLException {
        EIConnection conn = Database.instance().getConnection();
        try {
            List<DataSourceStats> statsList = sizeDataSources(conn, SecurityUtil.getAccountID());
            return usedSize(statsList);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static long usedSize(List<DataSourceStats> statsList) {
        long usedSize = 0;
        for (DataSourceStats stats : statsList) {
            if (stats.isVisible()) {
                usedSize += stats.getSize();
                for (DataSourceStats child : stats.getChildStats()) {
                    usedSize += child.getSize();
                    stats.setSize(stats.getSize() + child.getSize());
                }
            }
        }
        return usedSize;
    }

    public static List<DataSourceStats> sizeDataSources(EIConnection conn, long accountID) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("select feed_persistence_metadata.size, feed_persistence_metadata.feed_id, data_feed.feed_type, data_feed.visible, " +
                "data_feed.parent_source_id, data_feed.feed_name from feed_persistence_metadata, upload_policy_users, user, data_feed where " +
                "data_feed.data_feed_id = upload_policy_users.feed_id and feed_persistence_metadata.feed_id = upload_policy_users.feed_id and upload_policy_users.user_id = user.user_id and " +
                "user.account_id = ?");
        queryStmt.setLong(1, accountID);
        ResultSet qRS = queryStmt.executeQuery();
        Map<Long, DataSourceStats> statsMap = new HashMap<Long, DataSourceStats>();
        while (qRS.next()) {
            long size = qRS.getLong(1);
            long dataSourceID = qRS.getLong(2);
            int type = qRS.getInt(3);
            if (type == FeedType.COMPOSITE.getType()) {
                continue;
            }
            boolean visible = qRS.getBoolean(4);
            long parentSourceID = qRS.getLong(5);
            String feedName = qRS.getString(6);
            DataSourceStats dataSourceStats = statsMap.get(dataSourceID);
            if (dataSourceStats == null) {
                dataSourceStats = new DataSourceStats();
            }
            dataSourceStats.setSize(size);
            dataSourceStats.setVisible(visible);
            dataSourceStats.setDataSourceID(dataSourceID);
            dataSourceStats.setName(feedName);
            if (parentSourceID > 0) {
                DataSourceStats parent = statsMap.get(parentSourceID);
                if (parent == null) {
                    parent = new DataSourceStats();
                    statsMap.put(parentSourceID, parent);
                }
                parent.getChildStats().add(dataSourceStats);
            } else {
                statsMap.put(dataSourceID, dataSourceStats);
            }
        }
        queryStmt.close();

        List<DataSourceStats> validDataSources = new ArrayList<DataSourceStats>();
        for (DataSourceStats stats : statsMap.values()) {
            if (stats.isVisible()) {
                validDataSources.add(stats);
            }
        }
        return validDataSources;
    }

    private AccountStats getAccountStats(EIConnection conn) throws SQLException {
        long accountID = SecurityUtil.getAccountID();
        long maxSize;
        int currentUsers = 0;
        int maxUsers;
        long usedAPI = 0;
        long maxAPI = Account.getMaxCount(SecurityUtil.getAccountTier());
        PreparedStatement queryStmt = conn.prepareStatement("select feed_persistence_metadata.size, feed_persistence_metadata.feed_id, data_feed.feed_type, data_feed.visible, " +
                "data_feed.parent_source_id, data_feed.feed_name from feed_persistence_metadata, upload_policy_users, user, data_feed where " +
                "data_feed.data_feed_id = upload_policy_users.feed_id and feed_persistence_metadata.feed_id = upload_policy_users.feed_id and upload_policy_users.user_id = user.user_id and " +
                "user.account_id = ?");
        queryStmt.setLong(1, accountID);
        ResultSet qRS = queryStmt.executeQuery();
        Map<Long, DataSourceStats> statsMap = new HashMap<Long, DataSourceStats>();
        Map<Long, DataSourceStats> dupeMap = new HashMap<Long, DataSourceStats>();
        while (qRS.next()) {
            long size = qRS.getLong(1);
            long dataSourceID = qRS.getLong(2);
            if (dupeMap.containsKey(dataSourceID)) {
                continue;
            }

            int type = qRS.getInt(3);
            if (type == FeedType.COMPOSITE.getType()) {
                continue;
            }
            boolean visible = qRS.getBoolean(4);
            long parentSourceID = qRS.getLong(5);
            String feedName = qRS.getString(6);
            DataSourceStats dataSourceStats = statsMap.get(dataSourceID);
            dupeMap.put(dataSourceID, dataSourceStats);
            if (dataSourceStats == null) {
                dataSourceStats = new DataSourceStats();
            }
            dataSourceStats.setSize(size);
            dataSourceStats.setVisible(visible);
            dataSourceStats.setDataSourceID(dataSourceID);
            dataSourceStats.setName(feedName);
            if (parentSourceID > 0) {
                DataSourceStats parent = statsMap.get(parentSourceID);
                if (parent == null) {
                    parent = new DataSourceStats();
                    statsMap.put(parentSourceID, parent);
                }
                parent.getChildStats().add(dataSourceStats);
            } else {
                statsMap.put(dataSourceID, dataSourceStats);
            }
        }
        List<DataSourceStats> statsList = new ArrayList<DataSourceStats>();
        long usedSize = 0;
        for (DataSourceStats stats : statsMap.values()) {
            if (stats.isVisible()) {
                statsList.add(stats);
                usedSize += stats.getSize();
                for (DataSourceStats child : stats.getChildStats()) {
                    usedSize += child.getSize();
                    stats.setSize(stats.getSize() + child.getSize());
                }
            }
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
        PreparedStatement statsStmt = conn.prepareStatement("SELECT max_users, max_size FROM account where account_id = ?");
        statsStmt.setLong(1, accountID);
        ResultSet statRS = statsStmt.executeQuery();
        statRS.next();
        maxUsers = statRS.getInt(1);
        maxSize = statRS.getLong(2);
        AccountStats accountStats = new AccountStats();
        accountStats.setMaxSpace(maxSize);
        accountStats.setUsedSpace(usedSize);
        accountStats.setCurrentUsers(currentUsers);
        accountStats.setStatsList(statsList);
        accountStats.setAvailableUsers(maxUsers);
        accountStats.setApiUsedToday(usedAPI);
        accountStats.setApiMaxToday(maxAPI);
        return accountStats;
    }

    public void saveAccountSettings(AccountSettings accountSettings) {
        long accountID = SecurityUtil.getAccountID();
        SecurityUtil.authorizeAccountAdmin();
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from Account where accountID = ?").setLong(0, accountID).list();
            Account account = (Account) results.get(0);
            account.setApiEnabled(accountSettings.isApiEnabled());
            account.setDateFormat(accountSettings.getDateFormat());
            account.setGroupID(accountSettings.getGroupID() > 0 ? accountSettings.getGroupID() : null);
            account.setFirstDayOfWeek(accountSettings.getFirstDayOfWeek());
            account.setMarketplaceEnabled(accountSettings.isMarketplace());
            account.setPublicDataEnabled(accountSettings.isPublicData());
            account.setReportSharingEnabled(accountSettings.isReportSharing());
            account.setCurrencySymbol(accountSettings.getCurrencySymbol());
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public AccountSettings getAccountSettings() {
        AccountSettings accountSettings;
        long accountID = SecurityUtil.getAccountID();
        SecurityUtil.authorizeAccountAdmin();
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from Account where accountID = ?").setLong(0, accountID).list();
            Account account = (Account) results.get(0);
            accountSettings = new AccountSettings();
            accountSettings.setApiEnabled(account.isApiEnabled());
            accountSettings.setFirstDayOfWeek(account.getFirstDayOfWeek());
            accountSettings.setGroupID(account.getGroupID() != null ? account.getGroupID() : 0);
            accountSettings.setMarketplace(account.isMarketplaceEnabled());
            accountSettings.setPublicData(account.isPublicDataEnabled());
            accountSettings.setDateFormat(account.getDateFormat());
            accountSettings.setReportSharing(account.isReportSharingEnabled());
            accountSettings.setCurrencySymbol(account.getCurrencySymbol());
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return accountSettings;
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

    public ImageDescriptor getWhiteLabelImage() {
        long accountId;
        accountId = SecurityUtil.getAccountID();
        EIConnection conn = Database.instance().getConnection();
        ImageDescriptor desc = null;
        try {
            conn.setAutoCommit(false);
            PreparedStatement stmt = conn.prepareStatement("select image_name, user_image_id from account left join user_image on account.login_image = user_image.user_image_id where account_id = ?");
            stmt.setLong(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                desc = new ImageDescriptor();
                desc.setName(rs.getString(1));
                desc.setId(rs.getLong(2));
            }
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return desc;
    }

    public void saveWhiteLabelImage(ImageDescriptor imageDescriptor) {
        long accountId = SecurityUtil.getAccountID();
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement stmt = conn.prepareStatement("update account set login_image = ? where account_id = ?");
            if(imageDescriptor != null)
                stmt.setLong(1, imageDescriptor.getId());
            else
                stmt.setNull(1, Types.BIGINT);
            stmt.setLong(2, accountId);
            stmt.executeUpdate();
            stmt.close();
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }
}
