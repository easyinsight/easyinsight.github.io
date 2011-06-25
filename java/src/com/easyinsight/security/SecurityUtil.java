package com.easyinsight.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Arrays;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.preferences.ApplicationSkinSettings;
import com.easyinsight.preferences.UISettingRetrieval;
import com.easyinsight.users.UserService;
import com.easyinsight.users.UserServiceResponse;
import com.easyinsight.users.User;
import com.easyinsight.users.Account;
import com.easyinsight.logging.LogClass;
import com.easyinsight.logging.SecurityLogger;
import org.hibernate.Session;

/**
 * User: James Boe
 * Date: Aug 12, 2008
 * Time: 10:26:07 AM
 */
public class SecurityUtil {

    private static ISecurityProvider securityProvider;
    private static ThreadLocal<UserPrincipal> threadLocal = new ThreadLocal<UserPrincipal>();

    public static void populateThreadLocal(String userName, long userID, long accountID, int accountType, boolean accountAdmin, boolean guestUser, int firstDayOfWeek) {
        threadLocal.set(new UserPrincipal(userName, accountID, userID, accountType, accountAdmin, guestUser, null, firstDayOfWeek));
    }

    public static void setSecurityProvider(ISecurityProvider securityProvider) {
        SecurityUtil.securityProvider = securityProvider;
    }

    public static ISecurityProvider getSecurityProvider(){
        return SecurityUtil.securityProvider;
    }

    public static int getFirstDayOfWeek() {
        UserPrincipal userPrincipal = getSecurityProvider().getUserPrincipal();
        if(userPrincipal == null)
            userPrincipal = threadLocal.get();
        return userPrincipal.getFirstDayOfWeek();
    }

    public static boolean isGuestUser() {
        UserPrincipal userPrincipal = getSecurityProvider().getUserPrincipal();
        if(userPrincipal == null)
            userPrincipal = threadLocal.get();
        return userPrincipal.isGuestUser();
    }

    public static boolean isAccountAdmin() {
        UserPrincipal userPrincipal = getSecurityProvider().getUserPrincipal();
        if(userPrincipal == null)
            userPrincipal = threadLocal.get();
        return userPrincipal.isAccountAdmin();
    }

    public static String getUserName() {
        UserPrincipal userPrincipal = getSecurityProvider().getUserPrincipal();
        if(userPrincipal == null)
            userPrincipal = threadLocal.get();
        return userPrincipal.getUserName();
    }

    public static void authorizeAccountAdmin() {
        if (!isAccountAdmin()) {
            throw new SecurityException();
        }
    }

    public static long authenticate(String userName, String password){
        UserServiceResponse userServiceResponse = new UserService().authenticateWithEncrypted(userName, password);
        if (userServiceResponse.isSuccessful()) {
            return userServiceResponse.getUserID();
        } else {
            SecurityLogger.error("Unsuccessful login, user: " + userName);
            throw new SecurityException();
        }
    }

    public static UserServiceResponse authenticateKeys(String key, String secretKey) {
        UserServiceResponse userServiceResponse;
        if (key == null || secretKey == null) {
            throw new SecurityException();
        }
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            List results = session.createQuery("from User where userKey = ?").setString(0, key).list();
            if (results.size() > 0) {
                User user = (User) results.get(0);
                if (!secretKey.equals(user.getUserSecretKey())) {
                    throw new SecurityException();
                }
                Account account = user.getAccount();
                if(!Arrays.asList(Account.TRIAL, Account.ACTIVE).contains(account.getAccountState())) {
                    return new UserServiceResponse(false, "This account is not active. Please log in to re-activate your account.");
                }
                if (user.getPersonaID() != null) {
                    user.setUiSettings(UISettingRetrieval.getUISettings(user.getPersonaID(), conn, account));
                }
                userServiceResponse = new UserServiceResponse(true, user.getUserID(), user.getAccount().getAccountID(), user.getName(),
                                user.getAccount().getAccountType(), account.getMaxSize(), user.getEmail(), user.getUserName(), user.isAccountAdmin(),
                        account.isBillingInformationGiven() == null ? false : account.isBillingInformationGiven(), account.getAccountState(),
                        user.getUiSettings(), user.getFirstName(), !account.isUpgraded(), !user.isInitialSetupDone(), user.getLastLoginDate(), account.getName(),
                        user.getPersonaID(), account.getDateFormat(), account.isDefaultReportSharing(), false, user.isGuestUser(),
                        account.getCurrencySymbol(), ApplicationSkinSettings.retrieveSkin(user.getUserID(), session, user.getAccount().getAccountID()),
                        account.getFirstDayOfWeek(), user.getUserKey(), user.getUserSecretKey(), user.isOptInEmail(), user.getFixedDashboardID());
            } else {
                throw new SecurityException();
            }
            conn.commit();
        } catch (SecurityException se) {
            conn.rollback();
            throw se;
        } catch (Exception e) {
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            session.close();
            Database.closeConnection(conn);
        }
        return userServiceResponse;
    }

    public static UserServiceResponse authenticateToResponse(String userName, String password) {
        UserServiceResponse userServiceResponse = new UserService().authenticateWithEncrypted(userName, password);
        if (userServiceResponse.isSuccessful()) {
            return userServiceResponse;
        } else {
            SecurityLogger.error("Unsuccessful login, user: " + userName);
            throw new SecurityException();
        }
    }

    public static long getUserID() {
        return getUserID(true);
    }

    public static long getUserID(boolean required) {
        UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
        if (userPrincipal == null) {
            userPrincipal = threadLocal.get();
            if (userPrincipal == null) {
                if (required) {
                    SecurityLogger.error("Could not retrieve user principal.");
                    throw new SecurityException(SecurityException.LOGIN_REQUIRED);
                }
                else
                    return 0;
            } else {
                return userPrincipal.getUserID();
            }
        } else {
            return userPrincipal.getUserID();
        }
    }

    public static void authorizeAccountTier(int requiredTier) {
        UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
        if (userPrincipal == null) {
            userPrincipal = threadLocal.get();
            if(userPrincipal == null) {
                SecurityLogger.error("Could not retrieve user principal.");
                throw new SecurityException();
            }
        } else {
            if (userPrincipal.getAccountType() < requiredTier) {
                SecurityLogger.error("User " + userPrincipal.getUserName() + " could not access tier - " + requiredTier + "; their tier is " + userPrincipal.getAccountType());
                throw new SecurityException();
            }
        }
    }

    public static void authorizeScorecard(long scorecardID) {
        long userID = SecurityUtil.getUserID();
        authorizeScorecard(scorecardID, userID);
    }

    public static void authorizeScorecard(long scorecardID, long userID) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT USER.USER_ID, ACCOUNT_ID, ACCOUNT_VISIBLE FROM SCORECARD, USER WHERE SCORECARD_ID = ? AND " +
                    "USER.USER_ID = SCORECARD.USER_ID");
            queryStmt.setLong(1, scorecardID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {

                long scorecardUserID = rs.getLong(1);
                boolean accountVisible = rs.getBoolean(3);
                long accountID = rs.getLong(2);
                if (scorecardUserID != userID) {
                    if (accountVisible) {
                        if (accountID != getAccountID()) {
                            throw new SecurityException();
                        }
                    } else {
                        PreparedStatement queryGroupStmt = conn.prepareStatement("SELECT GROUP_TO_USER_JOIN.group_to_user_join_id FROM " +
                                "SCORECARD, GROUP_TO_USER_JOIN WHERE SCORECARD.group_id = GROUP_TO_USER_JOIN.group_id AND " +
                                "GROUP_TO_USER_JOIN.user_id = ? AND scorecard.scorecard_id = ?");
                        queryGroupStmt.setLong(1, userID);
                        queryGroupStmt.setLong(2, scorecardID);
                        ResultSet groupRS = queryGroupStmt.executeQuery();
                        if (!groupRS.next()) {
                            throw new SecurityException();
                        }
                    }
                }
            } else {
                throw new SecurityException();
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new SecurityException();
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static void authorizeKPI(long kpiID) {
        Connection conn = Database.instance().getConnection();
        long userID = SecurityUtil.getUserID();
        try {

            // HOW DO I DO THIS
            PreparedStatement queryStmt = conn.prepareStatement("SELECT OWNER " +
                    "FROM KPI_ROLE WHERE " +
                "KPI_ROLE.kpi_id = ? AND KPI_ROLE.USER_ID = ?");

            queryStmt.setLong(1, kpiID);
            queryStmt.setLong(2, userID);
            ResultSet rs = queryStmt.executeQuery();
            if (!rs.next()) {
                throw new SecurityException();
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new SecurityException();
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static void changeAccountType(int accountType) {
        securityProvider.getUserPrincipal().setAccountType(accountType);
    }

    public static int getAccountTier() {
        UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
        if (userPrincipal == null) {
            userPrincipal = threadLocal.get();
            if(userPrincipal == null) {
                SecurityLogger.error("Could not retrieve user principal.");
                throw new SecurityException();
            } else {
                return userPrincipal.getAccountType();
            }
        } else {
            return userPrincipal.getAccountType();
        }
    }

    public static long getAccountID() {
        UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
        if (userPrincipal == null) {
            userPrincipal = threadLocal.get();
            if (userPrincipal == null) {
                throw new SecurityException();
            } else {
                return userPrincipal.getAccountID();
            }
        } else {
            return userPrincipal.getAccountID();
        }
    }

    public static void authorizeFeed(long feedID, int requestedRole) {
        long userID = getUserID();
        int existingRole = getRole(userID, feedID);
        if (existingRole > requestedRole) {
            throw new SecurityException();
        }
    }

    public static int getUserRoleToFeed(long feedID) {
        long userID = getUserID();
        return getRole(userID, feedID);
    }

    public static int getInsightRole(long userID, long insightID) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement existingLinkQuery = conn.prepareStatement("SELECT RELATIONSHIP_TYPE FROM USER_TO_ANALYSIS WHERE " +
                    "USER_ID = ? AND ANALYSIS_ID = ?");
            existingLinkQuery.setLong(1, userID);
            existingLinkQuery.setLong(2, insightID);
            ResultSet rs = existingLinkQuery.executeQuery();
            if (rs.next()) {
                return Roles.OWNER;
            } else {
                PreparedStatement groupQueryStmt = conn.prepareStatement("select group_to_user_join.binding_type from group_to_insight, group_to_user_join where " +
                        "group_to_user_join.group_id = group_to_insight.group_id and group_to_user_join.user_id = ? and group_to_insight.insight_id = ?");
                groupQueryStmt.setLong(1, userID);
                groupQueryStmt.setLong(2, insightID);
                ResultSet groupRS = groupQueryStmt.executeQuery();
                if (groupRS.next()) {
                    return Roles.SUBSCRIBER;
                } else {
                    return Integer.MAX_VALUE;
                }
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static int getRole(long userID, long feedID) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement existingLinkQuery = conn.prepareStatement("SELECT ROLE FROM UPLOAD_POLICY_USERS WHERE " +
                    "USER_ID = ? AND FEED_ID = ?");
            existingLinkQuery.setLong(1, userID);
            existingLinkQuery.setLong(2, feedID);
            ResultSet rs = existingLinkQuery.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                PreparedStatement groupQueryStmt = conn.prepareStatement("select group_to_user_join.binding_type from upload_policy_groups, group_to_user_join where " +
                        "group_to_user_join.group_id = upload_policy_groups.group_id and group_to_user_join.user_id = ? and upload_policy_groups.feed_id = ?");
                groupQueryStmt.setLong(1, userID);
                groupQueryStmt.setLong(2, feedID);
                ResultSet groupRS = groupQueryStmt.executeQuery();
                if (groupRS.next()) {
                    return groupRS.getInt(1);
                } else {
                    PreparedStatement accountQueryStmt = conn.prepareStatement("select role from upload_policy_users, user, data_feed where " +
                            "data_feed.data_feed_id = ? AND data_feed.data_feed_id = upload_policy_users.feed_id and " +
                            "data_feed.account_visible = ? and upload_policy_users.user_id = user.user_id and " +
                            "user.account_id = ?");
                    accountQueryStmt.setLong(1, feedID);
                    accountQueryStmt.setBoolean(2, true);
                    accountQueryStmt.setLong(3, getAccountID());
                    ResultSet accountRS = accountQueryStmt.executeQuery();
                    if (accountRS.next()) {
                        return Roles.OWNER;
                    } else {
                        PreparedStatement forTimeBeingStmt = conn.prepareStatement("select account_id from user, data_feed, upload_policy_users where " +
                                "data_feed.data_feed_id = ? and data_feed.data_feed_id = upload_policy_users.feed_id and upload_policy_users.user_id = user.user_id");
                        forTimeBeingStmt.setLong(1, feedID);
                        ResultSet hackRS = forTimeBeingStmt.executeQuery();
                        if (hackRS.next()) {
                            long accountID = hackRS.getLong(1);
                            if (accountID == getAccountID()) {
                                return Roles.OWNER;
                            } else {
                                return Integer.MAX_VALUE;
                            }
                        } else {
                            return Integer.MAX_VALUE;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private static int getRoleForGroup(long userID, long groupID) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement existingLinkQuery = conn.prepareStatement("SELECT BINDING_TYPE FROM GROUP_TO_USER_JOIN WHERE " +
                    "USER_ID = ? AND GROUP_ID = ?");
            existingLinkQuery.setLong(1, userID);
            existingLinkQuery.setLong(2, groupID);
            ResultSet rs = existingLinkQuery.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return Integer.MAX_VALUE;
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static void authorizeGoal(long goalID, int requiredRole) {
        long userID = getUserID();
        int role;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement existingLinkQuery = conn.prepareStatement("SELECT USER_ROLE FROM USER_TO_GOAL_TREE, GOAL_TREE_NODE WHERE " +
                    "GOAL_TREE_NODE.goal_tree_id = USER_TO_GOAL_TREE.GOAL_TREE_ID and " +
                    "USER_ID = ? AND GOAL_TREE_NODE_ID = ?");
            existingLinkQuery.setLong(1, userID);
            existingLinkQuery.setLong(2, goalID);
            ResultSet rs = existingLinkQuery.executeQuery();
            if (rs.next()) {
                role = rs.getInt(1);
            } else {
                PreparedStatement groupQueryStmt = conn.prepareStatement("select group_to_goal_tree_join_id from group_to_goal_tree_join, group_to_user_join, GOAL_TREE_NODE where " +
                        "group_to_user_join.group_id = group_to_goal_tree_join.group_id and group_to_user_join.user_id = ? and group_to_goal_tree_join.goal_tree_id = goal_tree_node.goal_tree_id and " +
                        "goal_tree_node.goal_tree_node_id = ?");
                groupQueryStmt.setLong(1, userID);
                groupQueryStmt.setLong(2, goalID);
                if (rs.next()) {
                    role = Roles.SUBSCRIBER;
                } else {
                    role = Integer.MAX_VALUE;
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        if (role > requiredRole) {
            throw new SecurityException();
        }
    }

    private static long getKPITreeForKey(String urlKey) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT GOAL_TREE_ID FROM GOAL_TREE WHERE URL_KEY = ?");
            queryStmt.setString(1, urlKey);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                throw new SecurityException();
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private static int getRoleForGoalTree(long userID, long accountID, long goalTreeID) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement existingLinkQuery = conn.prepareStatement("SELECT USER_ROLE FROM USER_TO_GOAL_TREE WHERE " +
                    "USER_ID = ? AND GOAL_TREE_ID = ?");
            existingLinkQuery.setLong(1, userID);
            existingLinkQuery.setLong(2, goalTreeID);
            ResultSet rs = existingLinkQuery.executeQuery();
            if (rs.next()) {
                existingLinkQuery.close();
                return Roles.OWNER;
            } else {
                existingLinkQuery.close();
                PreparedStatement accountStmt = conn.prepareStatement("SELECT GOAL_TREE.GOAL_TREE_ID " +
                    "FROM GOAL_TREE, user_to_goal_tree, user WHERE " +
                    "user_to_goal_tree.user_id = user.user_id AND user_to_goal_tree.goal_tree_id = goal_tree.goal_tree_id and user.account_id = ? and goal_tree.account_visible = ? AND " +
                        "goal_tree.goal_tree_id = ?");
                accountStmt.setLong(1, accountID);
                accountStmt.setBoolean(2, true);
                accountStmt.setLong(3, goalTreeID);
                ResultSet accountRS = accountStmt.executeQuery();
                if (accountRS.next()) {
                    accountStmt.close();
                    return Roles.OWNER;
                } else {
                    accountStmt.close();
                    throw new SecurityException();
                }
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static int authorizeInsight(long insightID) {
        boolean feedVisibility = false;
        boolean accountVisibility = false;
        long dataFeedID = 0;
        boolean publicVisibility = false;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement authorizeStmt = conn.prepareStatement("SELECT FEED_VISIBILITY, ACCOUNT_VISIBLE, DATA_FEED_ID, PUBLICLY_VISIBLE FROM ANALYSIS WHERE ANALYSIS_ID = ?");
            authorizeStmt.setLong(1, insightID);
            ResultSet rs = authorizeStmt.executeQuery();
            if (rs.next()) {
                feedVisibility = rs.getBoolean(1);
                accountVisibility = rs.getBoolean(2);
                dataFeedID = rs.getLong(3);
                publicVisibility = rs.getBoolean(4);
            } else {
                throw new SecurityException();
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }

        UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
        if (userPrincipal == null) {
            userPrincipal = threadLocal.get();
            if(userPrincipal == null) {
                if (!publicVisibility) {
                    throw new SecurityException();
                } else {
                    return Roles.SUBSCRIBER;
                }
            }
        }

        if (accountVisibility) {
            conn = Database.instance().getConnection();
            try {
                PreparedStatement query = conn.prepareStatement("SELECT ACCOUNT_ID FROM USER, USER_TO_ANALYSIS WHERE USER.USER_ID = " +
                        "user_to_analysis.user_id AND user_to_analysis.analysis_id = ?");
                query.setLong(1, insightID);
                ResultSet rs = query.executeQuery();
                if (rs.next()) {
                    long accountID = rs.getLong(1);
                    if (accountID == userPrincipal.getAccountID()) {
                        return Roles.OWNER;
                        // all good
                    } else {
                        if (!publicVisibility) {
                            throw new SecurityException();
                        } else {
                            return Roles.SUBSCRIBER;
                        }
                    }
                } else {
                    if (!publicVisibility) {
                        throw new SecurityException();
                    } else {
                        return Roles.SUBSCRIBER;
                    }
                }
            } catch (SQLException e) {
                LogClass.error(e);
                throw new RuntimeException(e);
            } finally {
                Database.closeConnection(conn);
            }
        } else if (feedVisibility) {
            authorizeFeedAccess(dataFeedID);
            return Roles.OWNER;
        } else {
            int role = getInsightRole(userPrincipal.getUserID(), insightID);
            if (role != Roles.OWNER && role != Roles.SUBSCRIBER) {
                if (!publicVisibility) {
                    throw new SecurityException();
                } else {
                    return Roles.SUBSCRIBER;
                }
            }
            return role;
        }
    }

    public static void authorizeGoalTree(long goalTreeID, int requiredRole) {
        UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
        if (userPrincipal == null) {
            userPrincipal = threadLocal.get();
            if(userPrincipal == null) {
                throw new SecurityException();
            }
        }
        int role = getRoleForGoalTree(userPrincipal.getUserID(), userPrincipal.getAccountID(), goalTreeID);
        if (role > requiredRole) {
            throw new SecurityException();
        }
    }

    public static int authorizeDashboard(long dashboardID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DASHBOARD.ACCOUNT_VISIBLE, DASHBOARD.PUBLIC_VISIBLE FROM DASHBOARD where " +
                    "dashboard.dashboard_id = ?");
            queryStmt.setLong(1, dashboardID);

            boolean accountVisible;
            boolean publicVisible;

            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                accountVisible = rs.getBoolean(1);
                publicVisible = rs.getBoolean(2);
            } else {
                throw new SecurityException();
            }

            UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
            if (userPrincipal == null) {
                userPrincipal = threadLocal.get();
                if(userPrincipal == null) {
                    if (!publicVisible) {
                        throw new SecurityException();
                    } else {
                        return Roles.SUBSCRIBER;
                    }
                }
            }

            PreparedStatement userStmt = conn.prepareStatement("SELECT USER_TO_DASHBOARD.USER_ID, USER.ACCOUNT_ID FROM " +
                    "user_to_dashboard, user where user_to_dashboard.user_id = user.user_id and user_to_dashboard.dashboard_id = ?");
            userStmt.setLong(1, dashboardID);
            ResultSet userRS = userStmt.executeQuery();
            int role = Roles.NONE;
            while (userRS.next()) {
                long userID = userRS.getLong(1);
                long accountID = userRS.getLong(2);
                if (userID == SecurityUtil.getUserID()) {
                    role = Roles.OWNER;
                } else if (accountVisible && accountID == SecurityUtil.getAccountID()) {
                    role = Roles.SHARER;
                }
            }
            if (role == Roles.NONE) {
                throw new SecurityException();
            }
            return role;
        } catch (SQLException e) {
            LogClass.error(e);
            throw new SecurityException();
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static long authorizeGoalTree(String urlKey, int requiredRole) {
        UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
        if (userPrincipal == null) {
            userPrincipal = threadLocal.get();
            if(userPrincipal == null) {
                throw new SecurityException();
            }
        }
        long kpiTreeID = getKPITreeForKey(urlKey);
        int role = getRoleForGoalTree(userPrincipal.getUserID(), userPrincipal.getAccountID(), kpiTreeID);
        if (role > requiredRole) {
            throw new SecurityException();
        }
        return kpiTreeID;
    }

    public static void authorizeGoalTreeSolutionInstall(long goalTreeID) {
        UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
        if (userPrincipal == null) {
            userPrincipal = threadLocal.get();
            if(userPrincipal == null) {
                throw new SecurityException();
            }
        }
        int role = getRoleForGoalTree(userPrincipal.getUserID(), userPrincipal.getAccountID(), goalTreeID);
        boolean valid = false;
        if (role == Integer.MAX_VALUE) {
            Connection conn = Database.instance().getConnection();
            try {
                PreparedStatement queryStmt = conn.prepareStatement("SELECT SOLUTION_TIER FROM SOLUTION WHERE GOAL_TREE_ID = ?");
                queryStmt.setLong(1, goalTreeID);
                ResultSet rs = queryStmt.executeQuery();
                if (rs.next()) {
                    int solutionTier = rs.getInt(1);
                    valid = (solutionTier <= SecurityUtil.getAccountTier());
                }
            } catch (SQLException e) {
                LogClass.error(e);
                throw new RuntimeException(e);
            } finally {
                Database.closeConnection(conn);
            }
        } else {
            valid = true;
        }
        if (!valid) {
            throw new SecurityException();
        }
    }

    public static int authorizeGroup(long groupID, int requiredRole) {
        UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
        if (userPrincipal == null) {
            throw new SecurityException();
        }
        int role = getRoleForGroup(userPrincipal.getUserID(), groupID);
        if (role > requiredRole) {
            throw new SecurityException();
        }
        return role;
    }

    public static long authorizeGroupByKey(String urlKey, int requiredRole) {
        UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
        if (userPrincipal == null) {
            userPrincipal = threadLocal.get();
            if(userPrincipal == null) {
                throw new SecurityException();
            }
        }
        long groupID;
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement idStmt = conn.prepareStatement("SELECT COMMUNITY_GROUP_ID FROM COMMUNITY_GROUP WHERE URL_KEY = ?");
            idStmt.setString(1, urlKey);
            ResultSet rs = idStmt.executeQuery();
            if (rs.next()) {
                groupID = rs.getLong(1);
            } else {
                throw new SecurityException();
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        int role = getRoleForGroup(userPrincipal.getUserID(), groupID);
        if (role > requiredRole) {
            throw new SecurityException();
        }
        return groupID;
    }

    public static void authorizeFeedAccess(long dataFeed) {
        if (getAccountTier() == Account.ADMINISTRATOR) {
            return;
        }
        // retrieve feed policy
        boolean publiclyVisible = false;
        boolean accountVisible = false;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement authorizeStmt = conn.prepareStatement("SELECT PUBLICLY_VISIBLE, ACCOUNT_VISIBLE FROM DATA_FEED WHERE DATA_FEED_ID = ?");
            authorizeStmt.setLong(1, dataFeed);
            ResultSet rs = authorizeStmt.executeQuery();
            if (rs.next()) {
                publiclyVisible = rs.getBoolean(1);
                accountVisible = rs.getBoolean(2);
            } else {
                throw new SecurityException();
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        if (!publiclyVisible) {
            if (accountVisible) {
                conn = Database.instance().getConnection();
                try {
                    PreparedStatement query = conn.prepareStatement("SELECT ACCOUNT_ID FROM USER, UPLOAD_POLICY_USERS WHERE USER.USER_ID = " +
                            "UPLOAD_POLICY_USERS.user_id AND UPLOAD_POLICY_USERS.feed_id = ?");
                    query.setLong(1, dataFeed);
                    ResultSet rs = query.executeQuery();
                    if (rs.next()) {
                        long accountID = rs.getLong(1);
                        if (accountID == getAccountID()) {
                            // all good
                        } else {
                            throw new SecurityException();
                        }
                    } else {
                        throw new SecurityException();
                    }
                } catch (SQLException e) {
                    LogClass.error(e);
                    throw new RuntimeException(e);
                } finally {
                    Database.closeConnection(conn);
                }
            } else {
                UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
                if (userPrincipal == null) {
                    userPrincipal = threadLocal.get();
                    if(userPrincipal == null) {
                        throw new SecurityException(SecurityException.LOGIN_REQUIRED);
                    }
                }
                int role = getRole(userPrincipal.getUserID(), dataFeed);
                if (role > Roles.SUBSCRIBER) {
                    throw new SecurityException();
                }
            }
        }
    }

    public static long authorizeFeedAccess(String urlKey) {
        // retrieve feed policy
        boolean publiclyVisible = false;
        boolean accountVisible = false;
        long dataFeed;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement authorizeStmt = conn.prepareStatement("SELECT DATA_FEED_ID, PUBLICLY_VISIBLE, ACCOUNT_VISIBLE FROM DATA_FEED WHERE DATA_FEED.API_KEY = ?");
            authorizeStmt.setString(1, urlKey);
            ResultSet rs = authorizeStmt.executeQuery();
            if (rs.next()) {
                dataFeed = rs.getLong(1);
                publiclyVisible = rs.getBoolean(2);
                accountVisible = rs.getBoolean(3);
            } else {
                throw new SecurityException();
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        if (!publiclyVisible) {
            if (accountVisible) {
                conn = Database.instance().getConnection();
                try {
                    PreparedStatement query = conn.prepareStatement("SELECT ACCOUNT_ID FROM USER, UPLOAD_POLICY_USERS WHERE USER.USER_ID = " +
                            "UPLOAD_POLICY_USERS.user_id AND UPLOAD_POLICY_USERS.feed_id = ?");
                    query.setLong(1, dataFeed);
                    ResultSet rs = query.executeQuery();
                    if (rs.next()) {
                        long accountID = rs.getLong(1);
                        if (accountID == getAccountID()) {
                            // all good
                        } else {
                            throw new SecurityException();
                        }
                    } else {
                        throw new SecurityException();
                    }
                } catch (SQLException e) {
                    LogClass.error(e);
                    throw new RuntimeException(e);
                } finally {
                    Database.closeConnection(conn);
                }
            } else {
                UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
                if (userPrincipal == null) {
                    userPrincipal = threadLocal.get();
                    if(userPrincipal == null) {
                        throw new SecurityException(SecurityException.LOGIN_REQUIRED);
                    }
                }
                int role = getRole(userPrincipal.getUserID(), dataFeed);
                if (role > Roles.SUBSCRIBER) {
                    throw new SecurityException();
                }
            }
        }
        return dataFeed;
    }

    public static void clearThreadLocal() {
        threadLocal.remove();
    }
}
