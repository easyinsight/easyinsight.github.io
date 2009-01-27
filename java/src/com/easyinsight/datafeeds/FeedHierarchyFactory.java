package com.easyinsight.datafeeds;

import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * User: James Boe
 * Date: Sep 14, 2008
 * Time: 12:33:24 PM
 */
public class FeedHierarchyFactory {

    private FeedStorage feedStorage = new FeedStorage();

    public List<FeedHierarchyNode> createNodes(long feedID) {
        FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(feedID);
        Connection conn = Database.instance().getConnection();
        try {
            List<FeedDefinition> parentFeeds = getParentFeeds(feedDefinition, conn);
            List<FeedDefinition> derivedFeeds = getDerivedFeeds(feedDefinition, conn);
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.instance().closeConnection(conn);
        }

        return null;
    }

    private List<FeedDefinition> getDerivedFeeds(FeedDefinition feedDefinition, Connection conn) throws SQLException {
        List<FeedDefinition> compositeFeeds = findCompositeFeedsUsing(feedDefinition, conn);
        List<FeedDefinition> analysisFeeds = findAnalysisFeedsUsing(feedDefinition, conn);
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    private List<FeedDefinition> findAnalysisFeedsUsing(FeedDefinition feedDefinition, Connection conn) throws SQLException {
        PreparedStatement analysisStmt = conn.prepareStatement("SELECT ANALYSIS_ID FROM ANALYSIS WHERE DATA_FEED = ? AND ROOT_DEFINITION = ?");
        analysisStmt.setLong(1, feedDefinition.getDataFeedID());
        analysisStmt.setBoolean(2, false);
        ResultSet analysisRS = analysisStmt.executeQuery();
        while (analysisRS.next()) {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_FEED.DATA_FEED_ID FROM DATA_FEED WHERE DATA_FEED.ANALYSIS_ID = ?");
            queryStmt.setLong(1, feedDefinition.getAnalysisDefinitionID());
            ResultSet queryRS = queryStmt.executeQuery();
            while (queryRS.next()) {
                long feedID = queryRS.getLong(1);
                createNodes(feedID);
            }
        }

        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    private List<FeedDefinition> findCompositeFeedsUsing(FeedDefinition feedDefinition, Connection conn) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_FEED.DATA_FEED_ID FROM DATA_FEED WHERE DATA_FEED.ANALYSIS_ID = ?");
        queryStmt.setLong(1, feedDefinition.getDataFeedID());
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    private List<FeedDefinition> getParentFeeds(FeedDefinition feedDefinition, Connection conn) {
        if (feedDefinition.getFeedType() == FeedType.COMPOSITE) {

        } else if (feedDefinition.getFeedType() == FeedType.ANALYSIS_BASED) {

        }
        return null;  //To change body of created methods use File | Settings | File Templates.
    }
}
