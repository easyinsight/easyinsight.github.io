package com.easyinsight.datafeeds.salesforce;

import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.ColumnSegmentFactory;
import com.easyinsight.dataset.PersistableDataSetForm;
import com.sforce.soap.enterprise.Soap;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * User: James Boe
 * Date: Jul 5, 2008
 * Time: 6:49:27 PM
 */
public class SalesforceSubFeedDefinition extends FeedDefinition {

    private int subQueryType;

    public SalesforceSubFeedDefinition() {
    }

    public SalesforceSubFeedDefinition(int subQueryType) {
        this.subQueryType = subQueryType;
    }

    public FeedType getFeedType() {
        return FeedType.SALESFORCE_SUB;
    }

    public int getSubQueryType() {
        return subQueryType;
    }

    public void setSubQueryType(int subQueryType) {
        this.subQueryType = subQueryType;
    }

    public void refresh(Soap port) {
        DataSet dataSet = new SalesforceDataRetrieval().getDataSet(subQueryType, port);
        ColumnSegmentFactory columnSegmentFactory = new ColumnSegmentFactory();
        PersistableDataSetForm persistable = columnSegmentFactory.createPersistableForm(dataSet);
    }

    public void customStorage(Connection conn) throws SQLException {
        PreparedStatement insertGoogleStmt = conn.prepareStatement("INSERT INTO SALESFORCE_SUBJECT_DEFINITION (DATA_FEED_ID, SUBJECT_TYPE) " +
                "VALUES (?, ?)");
        try {
            insertGoogleStmt.setLong(1, getDataFeedID());
            insertGoogleStmt.setInt(2, subQueryType);
            insertGoogleStmt.execute();
        } finally {
            insertGoogleStmt.close();
        }
    }

    public void customLoad(Connection conn) throws SQLException {
        PreparedStatement getAnalysisStmt = conn.prepareStatement("SELECT SUBJECT_TYPE FROM SALESFORCE_SUBJECT_DEFINITION WHERE " +
                "DATA_FEED_ID = ?");
        try {
            getAnalysisStmt.setLong(1, getDataFeedID());
            ResultSet rs = getAnalysisStmt.executeQuery();
            rs.next();
            this.subQueryType = rs.getInt(1);
        } finally {
            getAnalysisStmt.close();
        }
    }
}
