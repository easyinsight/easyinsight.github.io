package com.easyinsight.userupload;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.google.GoogleFeedDefinition;
import com.easyinsight.datafeeds.google.GoogleSpreadsheetAccess;
import com.easyinsight.security.SecurityUtil;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
* User: jamesboe
* Date: Mar 27, 2010
* Time: 3:21:36 PM
*/
public class GoogleSpreadsheetUploadContext extends UploadContext {
    private String worksheetURL;

    public String getWorksheetURL() {
        return worksheetURL;
    }

    public void setWorksheetURL(String worksheetURL) {
        this.worksheetURL = worksheetURL;
    }

    private transient UploadFormat uploadFormat;
    private transient UserUploadService.RawUploadData rawUploadData;

    @Override
    public String validateUpload(EIConnection conn) throws SQLException {
        return null;
    }

    private Map<Key, Set<String>> sampleMap;

    @Override
    public List<AnalysisItem> guessFields(EIConnection conn) throws Exception {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT GOOGLE_DOCS_TOKEN.token_key, GOOGLE_DOCS_TOKEN.token_secret FROM " +
                "GOOGLE_DOCS_TOKEN WHERE GOOGLE_DOCS_TOKEN.user_id = ?");
        queryStmt.setLong(1, SecurityUtil.getUserID());
        ResultSet rs = queryStmt.executeQuery();
        rs.next();
        String tokenKey = rs.getString(1);
        String tokenSecret = rs.getString(2);
        SpreadsheetService myService = GoogleSpreadsheetAccess.getOrCreateSpreadsheetService(tokenKey, tokenSecret);
        URL listFeedUrl = new URL(worksheetURL);
        ListFeed feed = myService.getFeed(listFeedUrl, ListFeed.class);
        DataTypeGuesser guesser = new DataTypeGuesser();
        for (ListEntry listEntry : feed.getEntries()) {
            for (String tag : listEntry.getCustomElements().getTags()) {
                Value value;
                String string = listEntry.getCustomElements().getValue(tag);
                if (string == null) {
                    value = new EmptyValue();
                } else {
                    value = new StringValue(string);
                }
                guesser.addValue(new NamedKey(tag), value);
            }
        }
        sampleMap = guesser.getGuessesMap();
        return guesser.createFeedItems();
    }

    @Override
    public long createDataSource(String name, List<AnalysisItem> analysisItems, EIConnection conn, boolean accountVisible) throws Exception {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT GOOGLE_DOCS_TOKEN.token_key, GOOGLE_DOCS_TOKEN.token_secret FROM " +
                "GOOGLE_DOCS_TOKEN WHERE GOOGLE_DOCS_TOKEN.user_id = ?");
        queryStmt.setLong(1, SecurityUtil.getUserID());
        ResultSet rs = queryStmt.executeQuery();
        rs.next();
        String tokenKey = rs.getString(1);
        String tokenSecret = rs.getString(2);
        GoogleFeedDefinition googleFeedDefinition = new GoogleFeedDefinition();
        googleFeedDefinition.setFeedName(name);
        googleFeedDefinition.setAccountVisible(accountVisible);
        googleFeedDefinition.setWorksheetURL(worksheetURL);
        googleFeedDefinition.setTokenKey(tokenKey);
        googleFeedDefinition.setTokenSecret(tokenSecret);
        return googleFeedDefinition.create(conn, analysisItems, null);
    }

    @Override
    public List<String> getSampleValues(Key key) {
        return new ArrayList<String>(sampleMap.get(key));
    }
}