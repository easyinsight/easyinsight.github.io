package com.easyinsight.datafeeds.google;

import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.CredentialsDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.ColumnSegmentFactory;
import com.easyinsight.dataset.PersistableDataSetForm;
import com.easyinsight.users.Credentials;
import com.easyinsight.storage.DataRetrieval;
import com.easyinsight.storage.DataRetrievalManager;
import com.easyinsight.userupload.CredentialsResponse;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * User: James Boe
 * Date: May 14, 2008
 * Time: 2:28:56 PM
 */
public class GoogleFeedDefinition extends FeedDefinition {
    private String worksheetURL;

    public String getWorksheetURL() {
        return worksheetURL;
    }

    public void setWorksheetURL(String worksheetURL) {
        this.worksheetURL = worksheetURL;
    }

    public void customStorage(Connection conn) throws SQLException {
        PreparedStatement insertGoogleStmt = conn.prepareStatement("INSERT INTO GOOGLE_FEED (DATA_FEED_ID, WORKSHEETURL) " +
                "VALUES (?, ?)");
        try {
            insertGoogleStmt.setLong(1, getDataFeedID());
            insertGoogleStmt.setString(2, worksheetURL);
            insertGoogleStmt.execute();
        } finally {
            insertGoogleStmt.close();
        }
    }

    public void customLoad(Connection conn) throws SQLException {
        PreparedStatement getAnalysisStmt = conn.prepareStatement("SELECT WORKSHEETURL FROM GOOGLE_FEED WHERE " +
                "DATA_FEED_ID = ?");
        try {
            getAnalysisStmt.setLong(1, getDataFeedID());
            ResultSet rs = getAnalysisStmt.executeQuery();
            rs.next();
            this.worksheetURL = rs.getString(1);
        } finally {
            getAnalysisStmt.close();
        }
    }

    public FeedType getFeedType() {
        return FeedType.GOOGLE;
    }

    public Feed createFeedObject() {
        return new SpreadsheetStream(worksheetURL);
    }

    public int getCredentialsDefinition() {
        return CredentialsDefinition.STANDARD_USERNAME_PW;
    }

    public CredentialsResponse refresh(Credentials credentials) {
        DataSet dataSet = GoogleDataProvider.createDataSet(credentials, worksheetURL);
        ColumnSegmentFactory columnSegmentFactory = new ColumnSegmentFactory();
        PersistableDataSetForm persistable = columnSegmentFactory.createPersistableForm(dataSet, getFields());
        DataRetrievalManager.instance().storeData(getDataFeedID(), persistable);
        return new CredentialsResponse(true);
    }
}
