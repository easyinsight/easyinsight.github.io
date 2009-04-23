package com.easyinsight.datafeeds;

import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;
import com.easyinsight.analysis.AnalysisStorage;
import com.easyinsight.analysis.Tag;
import com.easyinsight.core.InsightDescriptor;

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

    private FeedDescriptor fromResultSet(ResultSet rs) throws SQLException {
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
        return createDescriptor(dataFeedID, feedName, role, 0, feedType, ownerName, description, attribution, null);
    }

    private List<FeedDescriptor> getFeeds(long accountID, String keyword, int cutoff, String sortClause) {
        List<FeedDescriptor> descriptorList = new ArrayList<FeedDescriptor>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = getStatement(conn, accountID, keyword, cutoff, sortClause);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                FeedDescriptor feedDescriptor = fromResultSet(rs);
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

    private FeedDescriptor createDescriptor(long dataFeedID, String feedName, Integer userRole,
                                            long size, int feedType, String ownerName, String description, String attribution, Date lastDataTime) throws SQLException {
        return new FeedDescriptor(feedName, dataFeedID, size, feedType, userRole != null ? userRole : 0, ownerName, description, attribution, lastDataTime);
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

    

    public List<InsightDescriptor> getAnalysisDefinitionsForGenre(String genre) {
        return new AnalysisStorage().getMostPopularAnalyses(genre, 4);
    }

    public List<FeedDescriptor> getRecentFeeds(int cutoff, long accountID, String genreKey) {
        return getFeeds(accountID, genreKey, cutoff, "ORDER BY CREATE_DATE DESC");
    }

    public List<FeedDescriptor> getBestRatedFeeds(int cutoff, long accountID, String genreKey) {
        return getFeeds(accountID, genreKey, cutoff, "ORDER BY FEED_RATING_AVERAGE DESC");
    }
}
