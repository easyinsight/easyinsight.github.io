package com.easyinsight.datafeeds;

import com.easyinsight.database.Database;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.logging.LogClass;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.analysis.AnalysisStorage;
import com.easyinsight.analysis.Tag;
import com.easyinsight.security.Roles;
import com.easyinsight.email.UserStub;
import com.easyinsight.groups.GroupDescriptor;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

import org.hibernate.Session;

/**
 * User: James Boe
 * Date: Oct 4, 2008
 * Time: 10:37:47 AM
 */
public class MarketplaceStorage {

    private static String defaultQueryStmt = "SELECT DATA_FEED_ID, FEED_NAME, FEED_TYPE, OWNER_NAME, DESCRIPTION, " +
            "ATTRIBUTION, ROLE FROM DATA_FEED LEFT OUTER JOIN UPLOAD_POLICY_USERS ON " +
            "DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID WHERE DATA_FEED.MARKETPLACE_VISIBLE = ? AND UPLOAD_POLICY_USERS.USER_ID = ? {0} {1}";

    private static String keywordQueryStmt = "SELECT DISTINCT DATA_FEED_ID, FEED_NAME, FEED_TYPE, OWNER_NAME, DESCRIPTION, " +
            "ATTRIBUTION, ROLE FROM FEED_TO_TAG, ANALYSIS_TAGS, DATA_FEED LEFT OUTER JOIN UPLOAD_POLICY_USERS ON DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID WHERE " +
            "((FEED_TO_TAG.FEED_ID = DATA_FEED.DATA_FEED_ID AND FEED_TO_TAG.ANALYSIS_TAGS_ID = ANALYSIS_TAGS.ANALYSIS_TAGS_ID AND ANALYSIS_TAGS.TAG LIKE ?) OR " +
            "FEED_NAME LIKE ?) AND " +
            "UPLOAD_POLICY_USERS.USER_ID = ? AND DATA_FEED.MARKETPLACE_VISIBLE = ? {0} {1}";

    // specific cases...
    // querying for marketplace
    // querying for my data

    private PreparedStatement getStatement(Connection conn, long userID, String keyword, int cutoff, String sortClause) throws SQLException {
        PreparedStatement preparedStatement;
        if (keyword == null) {
            String stmtString = MessageFormat.format(defaultQueryStmt, sortClause != null ? sortClause : "", cutoff > 0 ? "LIMIT " + cutoff : "");
            preparedStatement = conn.prepareStatement(stmtString);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setLong(2, userID);
        } else {
            String stmtString = MessageFormat.format(keywordQueryStmt, sortClause != null ? sortClause : "", cutoff > 0 ? "LIMIT " + cutoff : "");
            preparedStatement = conn.prepareStatement(stmtString);
            preparedStatement.setString(1, "%" + keyword + "%");
            preparedStatement.setString(2, "%" + keyword + "%");
            preparedStatement.setLong(3, userID);
            preparedStatement.setBoolean(4, true);
        }
        return preparedStatement;
    }

    private FeedDescriptor fromResultSet(ResultSet rs, Connection conn) throws SQLException {
        long dataFeedID = rs.getLong(1);
        String feedName = rs.getString(2);
        int feedType = rs.getInt(3);
        String ownerName = rs.getString(4);
        String description = rs.getString(5);
        String attribution = rs.getString(6);
        Integer role = rs.getInt(7);
        if (rs.wasNull()) {
            role = null;
        }
        return createDescriptor(dataFeedID, feedName, true, true, role, 0, feedType, ownerName, description, attribution, conn);
    }

    private List<FeedDescriptor> getFeeds(long accountID, String keyword, int cutoff, String sortClause) {
        List<FeedDescriptor> descriptorList = new ArrayList<FeedDescriptor>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = getStatement(conn, accountID, keyword, cutoff, sortClause);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                FeedDescriptor feedDescriptor = fromResultSet(rs, conn);
                descriptorList.add(feedDescriptor);
            }
            queryStmt.close();
        } catch (SQLException e) {
            LogClass.error(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return descriptorList;
    }

    public List<FeedDescriptor> getMostPopularFeeds(long accountID, String genreKey, int cutoff) {
        return getFeeds(accountID, genreKey, cutoff, "ORDER BY FEED_VIEWS DESC");
    }

    public List<FeedDescriptor> searchForAvailableFeeds(Long accountID, String keyword, String genreKey) {
        return getFeeds(accountID, keyword, 0, null);
    }

    private FeedDescriptor createDescriptor(long dataFeedID, String feedName, boolean publiclyVisible, boolean marketplaceVisible, Integer userRole,
                                            long size, int feedType, String ownerName, String description, String attribution, Connection conn) throws SQLException {
        UploadPolicy uploadPolicy = createUploadPolicy(conn, dataFeedID, publiclyVisible, marketplaceVisible);
        return new FeedDescriptor(feedName, dataFeedID, uploadPolicy, size, feedType, userRole != null ? userRole : 0, ownerName, description, attribution);
    }



    public Set<Tag> getTags(long feedID, Connection conn) throws SQLException {
        PreparedStatement queryTagsStmt = conn.prepareStatement("SELECT ANALYSIS_TAGS_ID FROM FEED_TO_TAG WHERE FEED_ID = ?");
        queryTagsStmt.setLong(1, feedID);
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
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return tags;
    }

    

    public List<WSAnalysisDefinition> getAnalysisDefinitionsForGenre(String genre) {
        return new AnalysisStorage().getMostPopularAnalyses(genre, 4);
    }

    private UploadPolicy createUploadPolicy(Connection conn, long feedID, boolean publiclyVisible, boolean marketplaceVisible) throws SQLException {
        UploadPolicy uploadPolicy = new UploadPolicy();
        uploadPolicy.setPubliclyVisible(publiclyVisible);
        uploadPolicy.setMarketplaceVisible(marketplaceVisible);
        List<FeedConsumer> owners = new ArrayList<FeedConsumer>();
        List<FeedConsumer> viewers = new ArrayList<FeedConsumer>();
        PreparedStatement policyUserStmt = conn.prepareStatement("SELECT USER.USER_ID, ROLE, USER.NAME, USER.USERNAME, USER.EMAIL FROM UPLOAD_POLICY_USERS, USER WHERE FEED_ID = ? AND " +
                "UPLOAD_POLICY_USERS.USER_ID = USER.USER_ID");
        policyUserStmt.setLong(1, feedID);
        ResultSet usersRS = policyUserStmt.executeQuery();
        while (usersRS.next()) {
            long userID = usersRS.getLong(1);
            int role = usersRS.getInt(2);
            String name = usersRS.getString(3);
            String userName = usersRS.getString(4);
            String email = usersRS.getString(5);
            UserStub userStub = new UserStub(userID, userName, email, name);
            if (role == Roles.OWNER) {
                owners.add(userStub);
            } else {
                viewers.add(userStub);
            }
        }
        PreparedStatement policyGroupsStmt = conn.prepareStatement("SELECT COMMUNITY_GROUP.NAME, COMMUNITY_GROUP.COMMUNITY_GROUP_ID, ROLE FROM UPLOAD_POLICY_GROUPS, COMMUNITY_GROUP WHERE FEED_ID = ? AND " +
                "UPLOAD_POLICY_GROUPS.GROUP_ID = COMMUNITY_GROUP.COMMUNITY_GROUP_ID");
        policyGroupsStmt.setLong(1, feedID);
        ResultSet groupsRS = policyGroupsStmt.executeQuery();
        while (groupsRS.next()) {
            String groupName = groupsRS.getString(1);
            long groupID = groupsRS.getLong(2);
            int role = groupsRS.getInt(3);
            GroupDescriptor groupDescriptor = new GroupDescriptor(groupName, groupID, 0, null);
            if (role == Roles.OWNER) {
                owners.add(groupDescriptor);
            } else {
                viewers.add(groupDescriptor);
            }
        }
        uploadPolicy.setOwners(owners);
        uploadPolicy.setViewers(viewers);
        return uploadPolicy;
    }

    public List<FeedDescriptor> getRecentFeeds(int cutoff, long accountID, String genreKey) {
        return getFeeds(accountID, genreKey, cutoff, "ORDER BY CREATE_DATE DESC");
    }

    public List<FeedDescriptor> getBestRatedFeeds(int cutoff, long accountID, String genreKey) {
        return getFeeds(accountID, genreKey, cutoff, "ORDER BY FEED_RATING_AVERAGE DESC");
    }
}
