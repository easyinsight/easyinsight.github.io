package com.easyinsight.datafeeds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Jan 28, 2008
 * Time: 6:47:10 PM
 */
public class CompositeFeedNode {
    private long dataFeedID;

    public CompositeFeedNode() {
    }

    public CompositeFeedNode(long dataFeedID) {
        this.dataFeedID = dataFeedID;
    }

    public long getDataFeedID() {
        return dataFeedID;
    }

    public void setDataFeedID(long dataFeedID) {
        this.dataFeedID = dataFeedID;
    }

    public void store(Connection conn, Long compositeFeedID) throws SQLException {
        PreparedStatement insertNodeStmt = conn.prepareStatement("INSERT INTO COMPOSITE_NODE (DATA_FEED_ID, COMPOSITE_FEED_ID) " +
                "VALUES (?, ?)");
        insertNodeStmt.setLong(1, dataFeedID);
        insertNodeStmt.setLong(2, compositeFeedID);
        insertNodeStmt.execute();
        insertNodeStmt.close();
    }
}
