package com.easyinsight.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.easyinsight.database.Database;
import com.easyinsight.users.UserService;
import com.easyinsight.users.UserServiceResponse;
import com.easyinsight.logging.LogClass;

/**
 * User: James Boe
 * Date: Aug 12, 2008
 * Time: 10:26:07 AM
 */
public class SecurityUtil {

    private static ISecurityProvider securityProvider;

    public static void setSecurityProvider(ISecurityProvider securityProvider) {
        SecurityUtil.securityProvider = securityProvider;
    }

    public static ISecurityProvider getSecurityProvider(){
        return SecurityUtil.securityProvider;
    }

    public static long authenticate(String userName, String password){
        UserServiceResponse userServiceResponse = new UserService().authenticateWithEncrypted(userName, password);
        if (userServiceResponse.isSuccessful()) {
            return userServiceResponse.getUserID();
        } else {
            Thread.dumpStack();
            throw new SecurityException();
        }
    }

    public static UserServiceResponse authenticateToResponse(String userName, String password) {
        UserServiceResponse userServiceResponse = new UserService().authenticateWithEncrypted(userName, password);
        if (userServiceResponse.isSuccessful()) {
            return userServiceResponse;
        } else {
            Thread.dumpStack();
            throw new SecurityException();
        }
    }

    public static long getUserID() {
        return getUserID(true);
    }

    public static long getUserID(boolean required) {
        UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
        if (userPrincipal == null) {
            if (required) {
                throw new SecurityException();
            }
            else
                return 0;
        } else {
            return userPrincipal.getUserID();
        }
    }

    public static void authorizeAccountTier(int requiredTier) {
        UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
        if (userPrincipal == null) {
            throw new SecurityException();
        } else {
            if (userPrincipal.getAccountType() < requiredTier) {
                throw new SecurityException();
            }
        }
    }

    public static int getAccountTier() {
        UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
        if (userPrincipal == null) {
            Thread.dumpStack();
            throw new SecurityException();
        } else {
            return userPrincipal.getAccountType();
        }
    }

    public static long getAccountID() {
        UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
        if (userPrincipal == null) {
            throw new SecurityException();
        } else {
            return userPrincipal.getAccountID();
        }
    }

    public static void authorizeFeed(long feedID, int requestedRole) {
        long userID = getUserID();
        int existingRole = getRole(userID, feedID);
        if (existingRole > requestedRole) {
            Thread.dumpStack();
            throw new SecurityException();
        }
    }

    public static int getUserRoleToFeed(long feedID) {
        long userID = getUserID();
        return getRole(userID, feedID);
    }

    private static int getInsightRole(long userID, long insightID) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement existingLinkQuery = conn.prepareStatement("SELECT RELATIONSHIP_TYPE FROM USER_TO_ANALYSIS WHERE " +
                    "USER_ID = ? AND ANALYSIS_ID = ?");
            existingLinkQuery.setLong(1, userID);
            existingLinkQuery.setLong(2, insightID);
            ResultSet rs = existingLinkQuery.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                PreparedStatement groupQueryStmt = conn.prepareStatement("select role from group_to_insight, group_to_user_join where " +
                        "group_to_user_join.group_id = group_to_insight.group_id and group_to_user_join.user_id = ? and group_to_insight.insight_id = ?");
                groupQueryStmt.setLong(1, userID);
                groupQueryStmt.setLong(2, insightID);
                ResultSet groupRS = groupQueryStmt.executeQuery();
                if (groupRS.next()) {
                    return groupRS.getInt(1);
                } else {
                    return Integer.MAX_VALUE;
                }
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
    }

    private static int getRole(long userID, long feedID) {
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
                PreparedStatement groupQueryStmt = conn.prepareStatement("select role from upload_policy_groups, group_to_user_join where " +
                        "group_to_user_join.group_id = upload_policy_groups.group_id and group_to_user_join.user_id = ? and upload_policy_groups.feed_id = ?");
                groupQueryStmt.setLong(1, userID);
                groupQueryStmt.setLong(2, feedID);
                ResultSet groupRS = groupQueryStmt.executeQuery();
                if (groupRS.next()) {
                    return groupRS.getInt(1);
                } else {
                    return Integer.MAX_VALUE;
                }
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
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
            Database.instance().closeConnection(conn);
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
                PreparedStatement groupQueryStmt = conn.prepareStatement("select role from group_to_goal_tree_join, group_to_user_join, GOAL_TREE_NODE where " +
                        "group_to_user_join.group_id = group_to_goal_tree_join.group_id and group_to_user_join.user_id = ? and group_to_goal_tree_join.goal_tree_id = goal_tree_node.goal_tree_id and " +
                        "goal_tree_node.goal_tree_node_id = ?");
                groupQueryStmt.setLong(1, userID);
                groupQueryStmt.setLong(2, goalID);
                if (rs.next()) {
                    role = rs.getInt(1);
                } else {
                    role = Integer.MAX_VALUE;
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        if (role > requiredRole) {
            throw new SecurityException();
        }
    }

    private static int getRoleForGoalTree(long userID, long goalTreeID) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement existingLinkQuery = conn.prepareStatement("SELECT USER_ROLE FROM USER_TO_GOAL_TREE WHERE " +
                    "USER_ID = ? AND GOAL_TREE_ID = ?");
            existingLinkQuery.setLong(1, userID);
            existingLinkQuery.setLong(2, goalTreeID);
            ResultSet rs = existingLinkQuery.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                PreparedStatement groupQueryStmt = conn.prepareStatement("select role from group_to_goal_tree_join, group_to_user_join where " +
                        "group_to_user_join.group_id = group_to_goal_tree_join.group_id and group_to_user_join.user_id = ? and group_to_goal_tree_join.goal_tree_id = ?");
                groupQueryStmt.setLong(1, userID);
                groupQueryStmt.setLong(2, goalTreeID);
                ResultSet groupRS = groupQueryStmt.executeQuery();
                if (groupRS.next()) {
                    return groupRS.getInt(1);
                } else {
                    return Integer.MAX_VALUE;
                }
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
    }

    public static void authorizeInsight(long insightID) {
        boolean publiclyVisible = false;
        boolean feedVisibility = false;
        long dataFeedID = 0;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement authorizeStmt = conn.prepareStatement("SELECT PUBLICLY_VISIBLE, FEED_VISIBILITY, DATA_FEED_ID FROM ANALYSIS WHERE ANALYSIS_ID = ?");
            authorizeStmt.setLong(1, insightID);
            ResultSet rs = authorizeStmt.executeQuery();
            if (rs.next()) {
                publiclyVisible = rs.getBoolean(1);
                feedVisibility = rs.getBoolean(2);
                dataFeedID = rs.getLong(3);
            } else {
                throw new SecurityException();
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }

        if (publiclyVisible) {
            // we're okay
        } else if (feedVisibility) {
            authorizeFeedAccess(dataFeedID);
        } else {
            UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
            if (userPrincipal == null) {
                Thread.dumpStack();
                throw new SecurityException();
            }
            int role = getInsightRole(userPrincipal.getUserID(), insightID);
            if (role != Roles.OWNER && role != Roles.SUBSCRIBER) {
                Thread.dumpStack();
                throw new SecurityException();
            }
        }
    }

    public static void authorizeGoalTree(long goalTreeID, int requiredRole) {
        UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
        if (userPrincipal == null) {
            throw new SecurityException();
        }
        int role = getRoleForGoalTree(userPrincipal.getUserID(), goalTreeID);
        if (role > requiredRole) {
            throw new SecurityException();
        }
    }

    public static void authorizeGroup(long groupID, int requiredRole) {
        UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
        if (userPrincipal == null) {
            throw new SecurityException();
        }
        int role = getRoleForGroup(userPrincipal.getUserID(), groupID);
        if (role > requiredRole) {
            throw new SecurityException();
        }
    }

    public static void authorizeFeedAccess(long dataFeed) {
        // retrieve feed policy
        boolean publiclyVisible = false;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement authorizeStmt = conn.prepareStatement("SELECT PUBLICLY_VISIBLE FROM DATA_FEED WHERE DATA_FEED_ID = ?");
            authorizeStmt.setLong(1, dataFeed);
            ResultSet rs = authorizeStmt.executeQuery();
            if (rs.next()) {
                publiclyVisible = rs.getBoolean(1);
            } else {
                Thread.dumpStack();
                throw new SecurityException();
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        if (!publiclyVisible) {
            UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
            if (userPrincipal == null) {
                throw new SecurityException(SecurityException.LOGIN_REQUIRED);
            }
            int role = getRole(userPrincipal.getUserID(), dataFeed);
            if (role > Roles.SUBSCRIBER) {
                throw new SecurityException();
            }
        }
    }
}
