package com.easyinsight.datafeeds.google;

import com.easyinsight.datafeeds.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.users.Credentials;
import com.easyinsight.users.Account;
import com.easyinsight.core.*;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.userupload.IDataTypeGuesser;
import com.easyinsight.userupload.DataTypeGuesser;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.util.ServiceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Date;
import java.net.URL;
import java.io.IOException;

import org.jetbrains.annotations.NotNull;

/**
 * User: James Boe
 * Date: May 14, 2008
 * Time: 2:28:56 PM
 */
public class GoogleFeedDefinition extends ServerDataSourceDefinition {
    private String worksheetURL;

    public String getWorksheetURL() {
        return worksheetURL;
    }

    public void setWorksheetURL(String worksheetURL) {
        this.worksheetURL = worksheetURL;
    }

    public void customStorage(Connection conn) throws SQLException {
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM GOOGLE_FEED WHERE DATA_FEED_ID = ? ");
        PreparedStatement insertGoogleStmt = conn.prepareStatement("INSERT INTO GOOGLE_FEED (DATA_FEED_ID, WORKSHEETURL) " +
                "VALUES (?, ?)");
        try {
            clearStmt.setLong(1, getDataFeedID());
            clearStmt.executeUpdate();
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

    @NotNull
    protected List<String> getKeys() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Key> newDataSourceFields(Credentials credentials) {
        Map<String, Key> keyMap = new HashMap<String, Key>();
        if (getDataFeedID() == 0) {
        } else {
            for (AnalysisItem field : getFields()) {
                keyMap.put(field.getKey().toKeyString(), field.getKey());
            }
        }
        return keyMap;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.FREE;
    }

    private List<AnalysisItem> populateFields(DataSet dataSet) {
        IDataTypeGuesser guesser = new DataTypeGuesser();
        for (IRow row : dataSet.getRows()) {
            for (Key key : row.getKeys()) {
                Value value = row.getValue(key);
                if (value == null) {
                    value = new EmptyValue();
                }
                guesser.addValue(key, value);
            }
        }
        return guesser.createFeedItems();
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet) {
        return populateFields(dataSet);
    }

    @Override
    public DataSet getDataSet(Credentials credentials, Map<String, Key> keys, Date now) {
        DataSet dataSet;
        try {
            SpreadsheetService myService = GoogleSpreadsheetAccess.getOrCreateSpreadsheetService(credentials);
            URL listFeedUrl = new URL(worksheetURL);
            ListFeed feed = myService.getFeed(listFeedUrl, ListFeed.class);
            dataSet = new DataSet();
            for (ListEntry listEntry : feed.getEntries()) {
                IRow row = dataSet.createRow();
                boolean atLeastOneValue = false;
                for (String tag : listEntry.getCustomElements().getTags()) {
                    Value value;
                    String string = listEntry.getCustomElements().getValue(tag);
                    if (string == null) {
                        value = new EmptyValue();
                    } else {
                        if (string.length() > 0) {
                            atLeastOneValue = true;
                        }
                        value = new StringValue(string);
                    }
                    NamedKey key = (NamedKey) keys.get(tag);
                    if (key == null) {
                        key = new NamedKey(tag);
                        keys.put(tag, key);
                    }
                    row.addValue(new NamedKey(tag), value);
                }
                if (!atLeastOneValue) {
                    dataSet.removeRow(row);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        return dataSet;
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

    public String validateCredentials(Credentials credentials) {
        return null;
    }
}
