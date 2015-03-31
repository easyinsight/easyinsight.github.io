package com.easyinsight.userupload;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedStorage;
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
    private GoogleFeedDefinition feedDefinition;


    public GoogleFeedDefinition getFeedDefinition() {
        return feedDefinition;
    }

    public void setFeedDefinition(GoogleFeedDefinition feedDefinition) {
        this.feedDefinition = feedDefinition;
    }

    private transient UploadFormat uploadFormat;
    private transient UserUploadService.RawUploadData rawUploadData;

    @Override
    public String validateUpload(EIConnection conn) throws SQLException {
        return null;
    }

    private Map<Key, Set<String>> sampleMap;

    @Override
    public List<AnalysisItem> guessFields(EIConnection conn, byte[] bytes) throws Exception {

        SpreadsheetService myService = GoogleSpreadsheetAccess.getOrCreateSpreadsheetService(feedDefinition.getTokenKey(), feedDefinition.getTokenSecret());
        URL listFeedUrl = new URL(feedDefinition.getWorksheetURL());
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
    public long createDataSource(String name, List<AnalysisItem> analysisItems, EIConnection conn, boolean accountVisible, byte[] bytes) throws Exception {
        feedDefinition.setFields(analysisItems);
        feedDefinition.setVisible(true);
        feedDefinition.setFeedName(name);
        new FeedStorage().updateDataFeedConfiguration(feedDefinition, conn);
        return feedDefinition.getDataFeedID();
    }

    @Override
    public List<String> getSampleValues(Key key) {
        return new ArrayList<String>(sampleMap.get(key));
    }
}