package com.easyinsight.datafeeds;

import com.easyinsight.core.Key;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Jan 28, 2008
 * Time: 6:47:05 PM
 */
public class CompositeFeedConnection {
    private Long sourceFeedID;
    private Long targetFeedID;
    private Key sourceJoin;
    private Key targetJoin;

    public CompositeFeedConnection() {
    }

    public CompositeFeedConnection(Long sourceFeedID, Long targetFeedID, Key sourceJoin, Key targetJoin) {
        this.sourceFeedID = sourceFeedID;
        this.targetFeedID = targetFeedID;
        this.sourceJoin = sourceJoin;
        this.targetJoin = targetJoin;
    }

    public Long getSourceFeedID() {
        return sourceFeedID;
    }

    public void setSourceFeedID(Long sourceFeedID) {
        this.sourceFeedID = sourceFeedID;
    }

    public Long getTargetFeedID() {
        return targetFeedID;
    }

    public void setTargetFeedID(Long targetFeedID) {
        this.targetFeedID = targetFeedID;
    }

    public Key getSourceJoin() {
        return sourceJoin;
    }

    public void setSourceJoin(Key sourceJoin) {
        this.sourceJoin = sourceJoin;
    }

    public Key getTargetJoin() {
        return targetJoin;
    }

    public void setTargetJoin(Key targetJoin) {
        this.targetJoin = targetJoin;
    }

    public void store(Connection conn, long feedID) throws SQLException {
        PreparedStatement connInsertStmt = conn.prepareStatement("INSERT INTO COMPOSITE_CONNECTION (" +
                "SOURCE_FEED_NODE_ID, TARGET_FEED_NODE_ID, SOURCE_JOIN, TARGET_JOIN, COMPOSITE_FEED_ID) VALUES (?, ?, ?, ?, ?)");
        connInsertStmt.setLong(1, sourceFeedID);
        connInsertStmt.setLong(2, targetFeedID);
        connInsertStmt.setLong(3, sourceJoin.getKeyID());
        connInsertStmt.setLong(4, targetJoin.getKeyID());
        connInsertStmt.setLong(5, feedID);
        connInsertStmt.execute();
        connInsertStmt.close();
    }
}
