package com.easyinsight.groups;

import com.easyinsight.util.RandomTextGenerator;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;

import java.util.*;
import java.sql.*;

/**
 * User: James Boe
 * Date: Aug 28, 2008
 * Time: 10:50:24 AM
 */
public class GroupStorage {

    public void deleteGroup(long groupID, EIConnection conn) throws SQLException {
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM COMMUNITY_GROUP WHERE COMMUNITY_GROUP_ID = ?");
        deleteStmt.setLong(1, groupID);
        deleteStmt.executeUpdate();
    }

    public long addGroup(Group group, long userID, long accountID, Connection conn) throws SQLException {
        PreparedStatement insertGroupStmt = conn.prepareStatement("INSERT INTO COMMUNITY_GROUP " +
                    "(NAME, DESCRIPTION, URL_KEY, GROUP_ACCOUNT_ID)" +
                    "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        insertGroupStmt.setString(1, group.getName());
        insertGroupStmt.setString(2, group.getDescription());
        insertGroupStmt.setString(3, group.getUrlKey());
        insertGroupStmt.setLong(4, accountID);
        insertGroupStmt.execute();
        long groupID = Database.instance().getAutoGenKey(insertGroupStmt);
        addUserToGroup(userID, groupID, GroupToUserBinding.OWNER, conn);
        return groupID;
    }

    public Group addGroup(Group group, long userID, long accountID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            group.setUrlKey(RandomTextGenerator.generateText(20));
            long groupID = addGroup(group, userID, accountID, conn);
            group.setGroupID(groupID);
            conn.commit();
            return group;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public Group getGroup(long groupID) throws SQLException {
        Group group = null;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT NAME, DESCRIPTION, URL_KEY FROM " +
                    "COMMUNITY_GROUP WHERE COMMUNITY_GROUP_ID = ?");
            queryStmt.setLong(1, groupID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString(1);
                String description = rs.getString(2);
                String urlKey = rs.getString(3);
                group = new Group();
                group.setName(name);
                group.setGroupID(groupID);
                group.setDescription(description);
                group.setUrlKey(urlKey);
            }
        } finally {
            Database.closeConnection(conn);
        }
        return group;
    }

    public void updateGroup(Group group, Connection conn) {
        try {
            PreparedStatement updateGroupStmt = conn.prepareStatement("UPDATE COMMUNITY_GROUP " +
                    "SET NAME = ?, DESCRIPTION = ?, GROUP_ACCOUNT_ID = ? WHERE COMMUNITY_GROUP_ID = ?");
            updateGroupStmt.setString(1, group.getName());
            updateGroupStmt.setString(2, group.getDescription());
            updateGroupStmt.setLong(3, group.getGroupID());
            updateGroupStmt.setLong(4, SecurityUtil.getAccountID());
            int rows = updateGroupStmt.executeUpdate();
            if (rows != 1) {
                throw new RuntimeException("Update failed");
            }
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            throw new RuntimeException(e);
        }
    }

    public List<GroupUser> getUsersForGroup(long groupID) throws SQLException {
        List<GroupUser> users = new ArrayList<GroupUser>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryUsersStmt = conn.prepareStatement("SELECT USERNAME, NAME, EMAIL, USER.USER_ID, BINDING_TYPE, FIRST_NAME FROM USER, GROUP_TO_USER_JOIN WHERE " +
                    "GROUP_TO_USER_JOIN.USER_ID = USER.USER_ID AND GROUP_TO_USER_JOIN.GROUP_ID = ?");
            queryUsersStmt.setLong(1, groupID);
            ResultSet rs = queryUsersStmt.executeQuery();
            while (rs.next()) {
                String userName = rs.getString(1);
                String name = rs.getString(2);
                String email = rs.getString(3);
                long userID = rs.getLong(4);
                int role = rs.getInt(5);
                String firstName = rs.getString(6);
                users.add(new GroupUser(userID, userName, email, name, role, firstName));
            }
        } finally {
            Database.closeConnection(conn);
        }
        return users;
    }

    public List<GroupDescriptor> getGroupsForUser(long userID) throws SQLException {
        List<GroupDescriptor> descriptors = new ArrayList<GroupDescriptor>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT COMMUNITY_GROUP.COMMUNITY_GROUP_ID, NAME, DESCRIPTION FROM COMMUNITY_GROUP, GROUP_TO_USER_JOIN WHERE " +
                    "USER_ID = ? AND GROUP_TO_USER_JOIN.GROUP_ID = COMMUNITY_GROUP.COMMUNITY_GROUP_ID");
            queryStmt.setLong(1, userID);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                descriptors.add(new GroupDescriptor(rs.getString(2), rs.getLong(1), 0, rs.getString(3)));
            }
        } finally {
            Database.closeConnection(conn);
        }
        return descriptors;
    }

    public List<GroupDescriptor> getGroupsForAccount(long accountID) throws SQLException {
        Set<GroupDescriptor> descriptors = new HashSet<GroupDescriptor>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT COMMUNITY_GROUP.COMMUNITY_GROUP_ID, COMMUNITY_GROUP.NAME, COMMUNITY_GROUP.DESCRIPTION FROM COMMUNITY_GROUP, GROUP_TO_USER_JOIN, USER WHERE " +
                    "USER.ACCOUNT_ID = ? AND GROUP_TO_USER_JOIN.GROUP_ID = COMMUNITY_GROUP.COMMUNITY_GROUP_ID AND " +
                    "GROUP_TO_USER_JOIN.USER_ID = USER.USER_ID");
            queryStmt.setLong(1, accountID);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                descriptors.add(new GroupDescriptor(rs.getString(2), rs.getLong(1), 0, rs.getString(3)));
            }
            queryStmt.close();
            PreparedStatement accountStmt = conn.prepareStatement("SELECT COMMUNITY_GROUP.COMMUNITY_GROUP_ID, COMMUNITY_GROUP.NAME, COMMUNITY_GROUP.DESCRIPTION FROM COMMUNITY_GROUP WHERE " +
                    "COMMUNITY_GROUP.GROUP_ACCOUNT_ID = ?");
            accountStmt.setLong(1, accountID);
            rs = accountStmt.executeQuery();
            while (rs.next()) {
                GroupDescriptor gd = new GroupDescriptor(rs.getString(2), rs.getLong(1), 0, rs.getString(3));
                descriptors.add(gd);
            }
            accountStmt.close();
        } finally {
            Database.closeConnection(conn);
        }
        return new ArrayList<GroupDescriptor>(descriptors);
    }

    public void addUserToGroup(long userID, long groupID, int userRole) throws SQLException {
        Connection conn = Database.instance().getConnection();
        try {
            addUserToGroup(userID, groupID, userRole, conn);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void addUserToGroup(long userID, long groupID, int userRole, Connection conn) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT GROUP_TO_USER_JOIN_ID FROM GROUP_TO_USER_JOIN WHERE " +
                "GROUP_ID = ? AND USER_ID = ?");
        queryStmt.setLong(1, groupID);
        queryStmt.setLong(2, userID);
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            PreparedStatement updateUserStmt = conn.prepareStatement("UPDATE GROUP_TO_USER_JOIN SET BINDING_TYPE = ? WHERE USER_ID = ? AND GROUP_ID = ?");
            updateUserStmt.setLong(1, userRole);
            updateUserStmt.setLong(2, userID);
            updateUserStmt.setLong(3, groupID);
            updateUserStmt.executeUpdate();
        } else {
            PreparedStatement addUserStmt = conn.prepareStatement("INSERT INTO GROUP_TO_USER_JOIN (GROUP_ID, USER_ID, BINDING_TYPE) " +
                    "VALUES (?, ?, ?)");
            addUserStmt.setLong(1, groupID);
            addUserStmt.setLong(2, userID);
            addUserStmt.setInt(3, userRole);
            addUserStmt.execute();
        }
    }

    public void removeUserFromGroup(long userID, long groupID, Connection conn) throws SQLException {
        PreparedStatement deleteUserStmt = conn.prepareStatement("DELETE FROM GROUP_TO_USER_JOIN WHERE USER_ID = ? AND GROUP_ID = ?");
        deleteUserStmt.setLong(1, userID);
        deleteUserStmt.setLong(2, groupID);
        deleteUserStmt.executeUpdate();
    }

    public void removeDataSourceFromGroup(long dataSourceID, long groupID) throws SQLException {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM UPLOAD_POLICY_GROUPS WHERE FEED_ID = ? AND GROUP_ID = ?");
            deleteStmt.setLong(1, dataSourceID);
            deleteStmt.setLong(2, groupID);
            deleteStmt.executeUpdate();
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void removeReportFromGroup(long dataSourceID, long groupID) throws SQLException {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM GROUP_TO_INSIGHT WHERE INSIGHT_ID = ? AND GROUP_ID = ?");
            deleteStmt.setLong(1, dataSourceID);
            deleteStmt.setLong(2, groupID);
            deleteStmt.executeUpdate();
            deleteStmt.close();
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void removeTagFromGroup(long tagID, long groupID) throws SQLException {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM GROUP_TO_TAG WHERE TAG_ID = ? AND GROUP_ID = ?");
            deleteStmt.setLong(1, tagID);
            deleteStmt.setLong(2, groupID);
            deleteStmt.executeUpdate();
            deleteStmt.close();
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void removeDashboardFromGroup(long dashboardID, long groupID) throws SQLException {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM group_to_dashboard WHERE dashboard_id = ? AND GROUP_ID = ?");
            deleteStmt.setLong(1, dashboardID);
            deleteStmt.setLong(2, groupID);
            deleteStmt.executeUpdate();
            deleteStmt.close();
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void removeScorecardFromGroup(long scorecardID, long groupID) throws SQLException {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM group_to_scorecard WHERE scorecard_id = ? AND GROUP_ID = ?");
            deleteStmt.setLong(1, scorecardID);
            deleteStmt.setLong(2, groupID);
            deleteStmt.executeUpdate();
            deleteStmt.close();
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void addReportToGroup(long reportID, long groupID) throws SQLException {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement existingLinkQuery = conn.prepareStatement("SELECT GROUP_TO_INSIGHT_ID FROM GROUP_TO_INSIGHT WHERE " +
                    "GROUP_ID = ? AND INSIGHT_ID = ?");
            existingLinkQuery.setLong(1, groupID);
            existingLinkQuery.setLong(2, reportID);
            ResultSet existingRS = existingLinkQuery.executeQuery();
            if (existingRS.next()) {
                long existingID = existingRS.getLong(1);
                PreparedStatement updateLinkStmt = conn.prepareStatement("UPDATE GROUP_TO_INSIGHT SET ROLE = ? WHERE " +
                        "GROUP_TO_INSIGHT_ID = ?");
                updateLinkStmt.setLong(1, Roles.OWNER);
                updateLinkStmt.setLong(2, existingID);
                updateLinkStmt.executeUpdate();
                updateLinkStmt.close();
            } else {
                PreparedStatement insightReportStmt = conn.prepareStatement("INSERT INTO GROUP_TO_INSIGHT (GROUP_ID, INSIGHT_ID, ROLE) " +
                        "VALUES (?, ?, ?)");
                insightReportStmt.setLong(1, groupID);
                insightReportStmt.setLong(2, reportID);
                insightReportStmt.setLong(3, Roles.OWNER);
                insightReportStmt.execute();
                insightReportStmt.close();
            }
            existingLinkQuery.close();
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void addTagToGroup(long tagID, long groupID) throws SQLException {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement existingLinkQuery = conn.prepareStatement("SELECT GROUP_TO_TAG_ID FROM GROUP_TO_TAG WHERE " +
                    "GROUP_ID = ? AND ACCOUNT_TAG_ID = ?");
            existingLinkQuery.setLong(1, groupID);
            existingLinkQuery.setLong(2, tagID);
            ResultSet existingRS = existingLinkQuery.executeQuery();
            if (existingRS.next()) {

            } else {
                PreparedStatement insightReportStmt = conn.prepareStatement("INSERT INTO GROUP_TO_TAG (GROUP_ID, ACCOUNT_TAG_ID) " +
                        "VALUES (?, ?)");
                insightReportStmt.setLong(1, groupID);
                insightReportStmt.setLong(2, tagID);
                insightReportStmt.execute();
                insightReportStmt.close();
            }
            existingLinkQuery.close();
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void addDashboardToGroup(long dashboardID, long groupID) throws SQLException {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement existingLinkQuery = conn.prepareStatement("SELECT group_to_dashboard_id FROM group_to_dashboard WHERE " +
                    "GROUP_ID = ? AND dashboard_id = ?");
            existingLinkQuery.setLong(1, groupID);
            existingLinkQuery.setLong(2, dashboardID);
            ResultSet existingRS = existingLinkQuery.executeQuery();
            if (existingRS.next()) {
                long existingID = existingRS.getLong(1);
                PreparedStatement updateLinkStmt = conn.prepareStatement("UPDATE group_to_dashboard SET ROLE = ? WHERE " +
                        "group_to_dashboard_id = ?");
                updateLinkStmt.setLong(1, Roles.OWNER);
                updateLinkStmt.setLong(2, existingID);
                updateLinkStmt.executeUpdate();
                updateLinkStmt.close();
            } else {
                PreparedStatement insightReportStmt = conn.prepareStatement("INSERT INTO group_to_dashboard (GROUP_ID, dashboard_id, ROLE) " +
                        "VALUES (?, ?, ?)");
                insightReportStmt.setLong(1, groupID);
                insightReportStmt.setLong(2, dashboardID);
                insightReportStmt.setLong(3, Roles.OWNER);
                insightReportStmt.execute();
                insightReportStmt.close();
            }
            existingLinkQuery.close();
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void addScorecardToGroup(long scorecardID, long groupID) throws SQLException {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement existingLinkQuery = conn.prepareStatement("SELECT group_to_scorecard_id FROM group_to_scorecard WHERE " +
                    "GROUP_ID = ? AND scorecard_id = ?");
            existingLinkQuery.setLong(1, groupID);
            existingLinkQuery.setLong(2, scorecardID);
            ResultSet existingRS = existingLinkQuery.executeQuery();
            if (existingRS.next()) {
                long existingID = existingRS.getLong(1);
                PreparedStatement updateLinkStmt = conn.prepareStatement("UPDATE group_to_scorecard SET ROLE = ? WHERE " +
                        "group_to_scorecard_id = ?");
                updateLinkStmt.setLong(1, Roles.OWNER);
                updateLinkStmt.setLong(2, existingID);
                updateLinkStmt.executeUpdate();
                updateLinkStmt.close();
            } else {
                PreparedStatement insightReportStmt = conn.prepareStatement("INSERT INTO group_to_scorecard (GROUP_ID, scorecard_id, ROLE) " +
                        "VALUES (?, ?, ?)");
                insightReportStmt.setLong(1, groupID);
                insightReportStmt.setLong(2, scorecardID);
                insightReportStmt.setLong(3, Roles.OWNER);
                insightReportStmt.execute();
                insightReportStmt.close();
            }
            existingLinkQuery.close();
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void addFeedToGroup(long feedID, long groupID, int owner) throws SQLException {
        Connection conn = Database.instance().getConnection();
        new FeedStorage().removeFeed(feedID);
        try {
            PreparedStatement existingLinkQuery = conn.prepareStatement("SELECT UPLOAD_POLICY_GROUPS_ID FROM UPLOAD_POLICY_GROUPS WHERE " +
                    "GROUP_ID = ? AND FEED_ID = ?");
            existingLinkQuery.setLong(1, groupID);
            existingLinkQuery.setLong(2, feedID);
            ResultSet existingRS = existingLinkQuery.executeQuery();
            if (existingRS.next()) {
                long existingID = existingRS.getLong(1);
                PreparedStatement updateLinkStmt = conn.prepareStatement("UPDATE UPLOAD_POLICY_GROUPS SET ROLE = ? WHERE " +
                        "UPLOAD_POLICY_GROUPS_ID = ?");
                updateLinkStmt.setLong(1, owner);
                updateLinkStmt.setLong(2, existingID);
                updateLinkStmt.executeUpdate();
            } else {
                PreparedStatement insertFeedStmt = conn.prepareStatement("INSERT INTO UPLOAD_POLICY_GROUPS (GROUP_ID, FEED_ID, ROLE) " +
                        "VALUES (?, ?, ?)");
                insertFeedStmt.setLong(1, groupID);
                insertFeedStmt.setLong(2, feedID);
                insertFeedStmt.setLong(3, owner);
                insertFeedStmt.execute();
            }
        } finally {
            Database.closeConnection(conn);
        }
    }

    public List<GroupDescriptor> getGroupsForDataSource(long dataSourceID) throws SQLException {
        List<GroupDescriptor> groups = new ArrayList<GroupDescriptor>();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement groupStmt = conn.prepareStatement("SELECT group_id, community_group.name FROM upload_policy_groups, community_group where feed_id = ? and " +
                    "community_group.community_group_id = upload_policy_groups.group_id");
            groupStmt.setLong(1, dataSourceID);
            ResultSet rs = groupStmt.executeQuery();
            while (rs.next()) {
                long groupID = rs.getLong(1);
                String name = rs.getString(2);
                groups.add(new GroupDescriptor(name, groupID, 0, null));
            }
        } finally {
            Database.closeConnection(conn);
        }
        return groups;
    }

    public void updateGroupsForDataSource(long dataSourceID, List<GroupDescriptor> groups) throws SQLException {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM upload_policy_groups WHERE FEED_ID = ?");
            clearStmt.setLong(1, dataSourceID);
            clearStmt.executeUpdate();
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO upload_policy_groups (FEED_ID, GROUP_ID, ROLE) VALUES (?, ?, ?)");
            for (GroupDescriptor desc : groups) {
                insertStmt.setLong(1, dataSourceID);
                insertStmt.setLong(2, desc.getGroupID());
                insertStmt.setInt(3, Roles.OWNER);
                insertStmt.execute();
            }
        } finally {
            Database.closeConnection(conn);
        }
    }
}
