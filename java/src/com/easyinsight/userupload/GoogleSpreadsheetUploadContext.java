package com.easyinsight.userupload;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.config.ConfigLoader;
import com.easyinsight.core.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.google.GoogleFeedDefinition;
import com.easyinsight.datafeeds.google.GoogleSpreadsheetAccess;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;

import java.net.URL;
import java.sql.Connection;
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

    private String getToken(Connection conn) {
        Token tokenObject = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.GOOGLE_DOCS_TOKEN, conn);
        if (tokenObject == null) {
            if (ConfigLoader.instance().getGoogleUserName() != null && !"".equals(ConfigLoader.instance().getGoogleUserName())) {
                return null;
            }
            throw new RuntimeException("Token access revoked?");
        }
        return tokenObject.getTokenValue();
    }

    @Override
    public List<AnalysisItem> guessFields(EIConnection conn) throws Exception {
        SpreadsheetService myService = GoogleSpreadsheetAccess.getOrCreateSpreadsheetService(getToken(conn));
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
    public long createDataSource(String name, List<AnalysisItem> analysisItems, EIConnection conn) throws Exception {
        GoogleFeedDefinition googleFeedDefinition = new GoogleFeedDefinition();
        googleFeedDefinition.setFeedName(name);
        googleFeedDefinition.setWorksheetURL(worksheetURL);
        return googleFeedDefinition.create(null, conn, analysisItems);
    }

    @Override
    public List<String> getSampleValues(Key key) {
        return new ArrayList<String>(sampleMap.get(key));
    }
}