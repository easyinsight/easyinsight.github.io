package com.easyinsight.datafeeds;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.Serializable;
import java.util.List;

/**
 * User: James Boe
 * Date: Jan 28, 2008
 * Time: 6:47:10 PM
 */
public class CompositeFeedNode implements Serializable {
    private long dataFeedID;
    private int x;
    private int y;
    private String dataSourceName;
    private int dataSourceType;
    private int refreshBehavior;

    public CompositeFeedNode() {
    }

    public CompositeFeedNode(long dataFeedID, int x, int y, String dataSourceName, int dataSourceType, int refreshBehavior) {
        this.dataFeedID = dataFeedID;
        this.refreshBehavior = refreshBehavior;
        this.x = x;
        this.y = y;
        this.dataSourceName = dataSourceName;
        this.dataSourceType = dataSourceType;
    }

    public int getRefreshBehavior() {
        return refreshBehavior;
    }

    public void setRefreshBehavior(int refreshBehavior) {
        this.refreshBehavior = refreshBehavior;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public int getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(int dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public long getDataFeedID() {
        return dataFeedID;
    }

    public void setDataFeedID(long dataFeedID) {
        this.dataFeedID = dataFeedID;
    }

    public void store(Connection conn, Long compositeFeedID) throws SQLException {
        PreparedStatement insertNodeStmt = conn.prepareStatement("INSERT INTO COMPOSITE_NODE (DATA_FEED_ID, COMPOSITE_FEED_ID, X, Y) " +
                "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        insertNodeStmt.setLong(1, dataFeedID);
        insertNodeStmt.setLong(2, compositeFeedID);
        insertNodeStmt.setInt(3, x);
        insertNodeStmt.setInt(4, y);
        insertNodeStmt.execute();
        long id = Database.instance().getAutoGenKey(insertNodeStmt);
        insertNodeStmt.close();

    }

    /*public QueryStateNode createQueryStateNode(EIConnection conn, List<AnalysisItem> analysisItems, InsightRequestMetadata insightRequestMetadata) {
        return new QueryStateNode(getDataFeedID(), FeedRegistry.instance().getFeed(getDataFeedID(), conn), conn, analysisItems, insightRequestMetadata);
    }*/
}
