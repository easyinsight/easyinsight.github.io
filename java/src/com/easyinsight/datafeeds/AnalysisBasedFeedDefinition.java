package com.easyinsight.datafeeds;

import com.easyinsight.analysis.AnalysisStorage;
import com.easyinsight.analysis.WSAnalysisDefinition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: May 5, 2008
 * Time: 2:01:56 PM
 */

public class AnalysisBasedFeedDefinition extends FeedDefinition {

    private long reportID;

    public long getReportID() {
        return reportID;
    }

    public void setReportID(long reportID) {
        this.reportID = reportID;
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        PreparedStatement wipeStmt = conn.prepareStatement("DELETE FROM REPORT_BASED_DATA_SOURCE WHERE DATA_FEED_ID = ?");
        wipeStmt.setLong(1, getDataFeedID());
        wipeStmt.executeUpdate();
        PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO REPORT_BASED_DATA_SOURCE (DATA_FEED_ID, REPORT_ID) VALUES (?, ?)");
        saveStmt.setLong(1, getDataFeedID());
        saveStmt.setLong(2, reportID);
        saveStmt.execute();
    }

    public FeedType getFeedType() {
        return FeedType.ANALYSIS_BASED;
    }

    public Feed createFeedObject(FeedDefinition parent) {
        AnalysisBasedFeed feed = new AnalysisBasedFeed();
        feed.setAnalysisDefinition(new AnalysisStorage().getAnalysisDefinition(reportID));
        return feed;
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        PreparedStatement retrieveStmt = conn.prepareStatement("SELECT REPORT_ID FROM REPORT_BASED_DATA_SOURCE WHERE DATA_FEED_ID = ?");
        retrieveStmt.setLong(1, getDataFeedID());
        ResultSet rs = retrieveStmt.executeQuery();
        rs.next();
        reportID = rs.getLong(1);
        WSAnalysisDefinition def = new AnalysisStorage().getAnalysisDefinition(reportID, conn);
        if (def != null) {
            setFields(new FeedStorage().retrieveFields(def.getDataFeedID(), conn));
        }
    }
}
