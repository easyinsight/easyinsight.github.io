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

    public static long authenticate(String userName, String password){
        UserServiceResponse userServiceResponse = new UserService().authenticateWithEncrypted(userName, password);
        if (userServiceResponse.isSuccessful()) {
            return userServiceResponse.getUserID();
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
            Thread.dumpStack();
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
                return Integer.MAX_VALUE;
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
                return Integer.MAX_VALUE;
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

    public static void authorizeFeedAccess(long dataFeed) {
        authorizeFeedAccess(dataFeed, false);
    }

    public static void authorizeFeedAccess(long dataFeed, boolean preview) {
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
                throw new SecurityException();
            }
            int role = getRole(userPrincipal.getUserID(), dataFeed);
            if (role > Roles.SUBSCRIBER) {
                throw new SecurityException();
            }
        }
    }
}
