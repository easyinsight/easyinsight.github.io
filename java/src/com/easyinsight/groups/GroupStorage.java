package com.easyinsight.groups;

import com.easyinsight.util.RandomTextGenerator;
import org.hibernate.Session;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.analysis.Tag;
import com.easyinsight.logging.LogClass;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.audit.AuditMessage;

import java.util.*;
import java.util.Date;
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

    public long addGroupComment(GroupComment groupComment) throws SQLException {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement insertCommentStmt = conn.prepareStatement("INSERT INTO group_comment (group_id, user_id, comment, time_created) values (?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            insertCommentStmt.setLong(1, groupComment.getGroupID());
            insertCommentStmt.setLong(2, groupComment.getUserID());
            insertCommentStmt.setString(3, groupComment.getMessage());
            insertCommentStmt.setTimestamp(4, new java.sql.Timestamp(new Date().getTime()));
            insertCommentStmt.execute();
            return Database.instance().getAutoGenKey(insertCommentStmt);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void addGroupAudit(GroupAuditMessage groupAuditMessage, Connection conn) throws SQLException {
            PreparedStatement insertCommentStmt = conn.prepareStatement("INSERT INTO group_audit_message (group_id, user_id, comment, audit_time) values (?, ?, ?, ?)");
            insertCommentStmt.setLong(1, groupAuditMessage.getGroupID());
            insertCommentStmt.setLong(2, groupAuditMessage.getUserID());
            insertCommentStmt.setString(3, groupAuditMessage.getMessage());
            insertCommentStmt.setTimestamp(4, new java.sql.Timestamp(new Date().getTime()));
            insertCommentStmt.execute();
    }

    public long addGroup(Group group, long userID, Connection conn) throws SQLException {
        PreparedStatement insertGroupStmt = conn.prepareStatement("INSERT INTO COMMUNITY_GROUP " +
                    "(NAME, DESCRIPTION, URL_KEY)" +
                    "VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        insertGroupStmt.setString(1, group.getName());
        insertGroupStmt.setString(2, group.getDescription());
        insertGroupStmt.setString(3, group.getUrlKey());
        insertGroupStmt.execute();
        long groupID = Database.instance().getAutoGenKey(insertGroupStmt);
        addUserToGroup(userID, groupID, GroupToUserBinding.OWNER, conn);
        saveTags(group.getTags(), groupID, conn);
        return groupID;
    }

    public Group addGroup(Group group, long userID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            group.setUrlKey(RandomTextGenerator.generateText(20));
            long groupID = addGroup(group, userID, conn);
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
                group.setTags(new ArrayList<Tag>(getTags(groupID, conn)));
            }
        } finally {
            Database.closeConnection(conn);
        }
        return group;
    }

    public void updateGroup(Group group, Connection conn) {
        try {
            PreparedStatement updateGroupStmt = conn.prepareStatement("UPDATE COMMUNITY_GROUP " +
                    "SET NAME = ?, DESCRIPTION = ? WHERE COMMUNITY_GROUP_ID = ?");
            updateGroupStmt.setString(1, group.getName());
            updateGroupStmt.setString(2, group.getDescription());
            updateGroupStmt.setLong(3, group.getGroupID());
            int rows = updateGroupStmt.executeUpdate();
            if (rows != 1) {
                throw new RuntimeException("Update failed");
            }
            saveTags(group.getTags(), group.getGroupID(), conn);
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            throw new RuntimeException(e);
        }
    }

    public Set<Tag> getTags(long groupID, Connection conn) throws SQLException {
        PreparedStatement queryTagsStmt = conn.prepareStatement("SELECT ANALYSIS_TAGS_ID FROM GROUP_TO_TAG WHERE GROUP_ID = ?");
        queryTagsStmt.setLong(1, groupID);
        Set<Long> tagIDs = new HashSet<Long>();
        ResultSet rs = queryTagsStmt.executeQuery();
        while (rs.next()) {
            tagIDs.add(rs.getLong(1));
        }
        queryTagsStmt.close();
        Set<Tag> tags = new HashSet<Tag>();
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            for (Long tagID : tagIDs) {
                List items = session.createQuery("from Tag where tagID = ?").setLong(0, tagID).list();
                if (items.size() > 0) {
                    Tag tag = (Tag) items.get(0);
                    tags.add(tag);
                }
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return tags;
    }

    private void saveTags(List<Tag> tags, long groupID, Connection conn) throws SQLException {
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM GROUP_TO_TAG WHERE GROUP_ID = ?");
        deleteStmt.setLong(1, groupID);
        deleteStmt.executeUpdate();
        deleteStmt.close();
        if (tags != null) {
            Session session = Database.instance().createSession(conn);
            try {
                for (Tag tag : tags) {
                    
                    session.saveOrUpdate(tag);
                }
                session.flush();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                session.close();
            }
            PreparedStatement insertLinkStmt = conn.prepareStatement("INSERT INTO GROUP_TO_TAG (GROUP_ID, ANALYSIS_TAGS_ID) " +
                    "VALUES (?, ?)");
            for (Tag tag : tags) {
                insertLinkStmt.setLong(1, groupID);
                insertLinkStmt.setLong(2, tag.getTagID());
                insertLinkStmt.execute();
            }
            insertLinkStmt.close();
        }
    }

    public List<GroupDescriptor> getAllPublicGroups() throws SQLException {
        List<GroupDescriptor> descriptors = new ArrayList<GroupDescriptor>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT COMMUNITY_GROUP_ID, NAME, DESCRIPTION, COUNT(GROUP_TO_USER_JOIN_ID) FROM COMMUNITY_GROUP, GROUP_TO_USER_JOIN WHERE " +
                    "GROUP_TO_USER_JOIN.GROUP_ID = COMMUNITY_GROUP.COMMUNITY_GROUP_ID AND (COMMUNITY_GROUP.publicly_joinable = ? OR COMMUNITY_GROUP.publicly_visible = ?) " +
                    "GROUP BY COMMUNITY_GROUP_ID, NAME, DESCRIPTION");
            queryStmt.setBoolean(1, true);
            queryStmt.setBoolean(2, true);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                descriptors.add(new GroupDescriptor(rs.getString(2), rs.getLong(1), rs.getInt(4), rs.getString(3)));
            }
        } finally {
            Database.closeConnection(conn);
        }
        return descriptors;
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

    public String inviteNewUserToGroup(long groupID) throws SQLException {
        Connection conn = Database.instance().getConnection();
        try {
            char[] activationBuf = new char[30];
            for (int i = 0; i < 30; i++) {
                char c;
                int randVal = (int) (Math.random() * 36);
                if (randVal < 26) {
                    c = (char) ('a' + randVal);
                } else {
                    c = (char) (randVal - 26);
                }
                activationBuf[i] = c;
            }
            PreparedStatement addActivationStmt = conn.prepareStatement("INSERT INTO GROUP_NEW_USER_INVITE (GROUP_ID, ACTIVATION_STRING) VALUES (?, ?)");
            addActivationStmt.setLong(1, groupID);
            String activationString = new String(activationBuf);
            addActivationStmt.setString(2, activationString);
            addActivationStmt.execute();
            return activationString; 
        } finally {
            Database.closeConnection(conn);
        }
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
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void removeGoalTreeFromGroup(long dataSourceID, long groupID) throws SQLException {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM GROUP_TO_GOAL_TREE_JOIN WHERE GOAL_TREE_ID = ? AND GROUP_ID = ?");
            deleteStmt.setLong(1, dataSourceID);
            deleteStmt.setLong(2, groupID);
            deleteStmt.executeUpdate();
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void addReportToGroup(long reportID, long groupID) throws SQLException {
        EIConnection conn = Database.instance().getConnection();
        Session s = Database.instance().createSession(conn);
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
            s.close();
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void addGoalTreeToGroup(long goalTreeID, long groupID) throws SQLException {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement existingLinkQuery = conn.prepareStatement("SELECT GROUP_TO_GOAL_TREE_JOIN_ID FROM GROUP_TO_GOAL_TREE_JOIN WHERE " +
                    "GROUP_ID = ? AND GOAL_TREE_ID = ?");
            existingLinkQuery.setLong(1, groupID);
            existingLinkQuery.setLong(2, goalTreeID);
            ResultSet existingRS = existingLinkQuery.executeQuery();
            if (!existingRS.next()) {
                PreparedStatement insertFeedStmt = conn.prepareStatement("INSERT INTO GROUP_TO_GOAL_TREE_JOIN (GROUP_ID, GOAL_TREE_ID) " +
                        "VALUES (?, ?)");
                insertFeedStmt.setLong(1, groupID);
                insertFeedStmt.setLong(2, goalTreeID);
                insertFeedStmt.execute();
            }
        } finally {
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
            addGroupAudit(new GroupAuditMessage(SecurityUtil.getUserID(), new Date(), "Added data source", groupID, null), conn);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public List<AuditMessage> getGroupMessages(long groupID, Date startDate, Date endDate, int limit) throws SQLException {
        List<AuditMessage> groupComments = new ArrayList<AuditMessage>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT comment, username, group_comment.user_id, time_created, group_comment_id, community_group.name " +
                    "FROM group_comment, user, community_group WHERE group_id = ? and group_comment.group_id = community_group.community_group_id AND " +
                    "group_comment.user_id = user.user_id AND time_created >= ? AND time_created <= ? ORDER BY time_created desc");
            if (limit > 0) {
                queryStmt.setMaxRows(limit);
            }
            queryStmt.setLong(1, groupID);
            queryStmt.setTimestamp(2, new Timestamp(startDate.getTime()));
            queryStmt.setTimestamp(3, new Timestamp(endDate.getTime()));
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                String comment = rs.getString(1);
                String userName = rs.getString(2);
                long userID = rs.getLong(3);
                Date timeCreated = new Date(rs.getTimestamp(4).getTime());
                long groupCommentID = rs.getLong(5);
                String groupName = rs.getString(6);
                GroupComment groupComment = new GroupComment();
                groupComment.setAudit(false);
                groupComment.setMessage(comment);
                groupComment.setUserID(userID);
                groupComment.setUserName(userName);
                groupComment.setGroupCommentID(groupCommentID);
                groupComment.setTimestamp(timeCreated);
                groupComment.setGroupName(groupName);
                groupComments.add(groupComment);
            }
            PreparedStatement queryAuditStmt = conn.prepareStatement("SELECT comment, username, group_audit_message.user_id, audit_time, community_group.name " +
                    "FROM group_audit_message, user, community_group WHERE group_id = ? and group_audit_message.group_id = community_group.community_group_id AND " +
                    "group_audit_message.user_id = user.user_id AND audit_time >= ? AND audit_time <= ?  ORDER BY audit_time desc");
            queryAuditStmt.setLong(1, groupID);
            if (limit > 0) {
                queryAuditStmt.setMaxRows(limit);
            }
            queryAuditStmt.setTimestamp(2, new Timestamp(startDate.getTime()));
            queryAuditStmt.setTimestamp(3, new Timestamp(endDate.getTime()));
            ResultSet auditRS = queryAuditStmt.executeQuery();
            while (auditRS.next()) {
                String comment = auditRS.getString(1);
                String userName = auditRS.getString(2);
                long userID = auditRS.getLong(3);
                Date timeCreated = new Date(auditRS.getTimestamp(4).getTime());
                String groupName = auditRS.getString(5);
                GroupAuditMessage groupAuditMessage = new GroupAuditMessage();
                groupAuditMessage.setAudit(true);
                groupAuditMessage.setMessage(comment);
                groupAuditMessage.setUserID(userID);
                groupAuditMessage.setUserName(userName);
                groupAuditMessage.setTimestamp(timeCreated);
                groupAuditMessage.setGroupName(groupName);
                groupComments.add(groupAuditMessage);
            }
        } finally {
            Database.closeConnection(conn);
        }
        return groupComments;
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
