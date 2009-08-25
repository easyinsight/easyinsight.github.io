package com.easyinsight.datafeeds.google;

import com.easyinsight.datafeeds.Feed;
import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.*;
import com.easyinsight.users.TokenStorage;
import com.easyinsight.users.Token;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;

import java.util.Set;
import java.util.Collection;
import java.util.List;
import java.net.URL;

/**
 * User: jamesboe
 * Date: Jul 27, 2009
 * Time: 2:51:27 PM
 */
public class GoogleSpreadsheetFeed extends Feed {

    private String worksheetURL;
    private String token;

    public GoogleSpreadsheetFeed(String worksheetURL) {
        this.worksheetURL = worksheetURL;
    }

    private String getToken() {
        if (token == null) {
            Token tokenObject = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.GOOGLE_DOCS_TOKEN);
            if (tokenObject == null) {
                throw new RuntimeException("Token access revoked?");
            }
            token = tokenObject.getTokenValue();
        }
        return token;
    }

    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata) {
        try {
            AnalysisItemResultMetadata metadata = analysisItem.createResultMetadata();
            SpreadsheetService myService = GoogleSpreadsheetAccess.getOrCreateSpreadsheetService(getToken());
            URL listFeedUrl = new URL(worksheetURL);
            ListFeed feed = myService.getFeed(listFeedUrl, ListFeed.class);
            for (ListEntry listEntry : feed.getEntries()) {
                for (String tag : listEntry.getCustomElements().getTags()) {
                    if (analysisItem.getKey().toKeyString().equals(tag)) {
                        String string = listEntry.getCustomElements().getValue(tag);
                        Value value;
                        if (string == null) {
                            value = new EmptyValue();
                        } else {
                            value = new StringValue(string);
                        }
                        metadata.addValue(analysisItem, value, insightRequestMetadata);
                    }
                }
            }
            return metadata;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, Collection<Key> additionalNeededKeys) {
        try {
            SpreadsheetService myService = GoogleSpreadsheetAccess.getOrCreateSpreadsheetService(getToken());
            URL listFeedUrl = new URL(worksheetURL);
            ListFeed feed = myService.getFeed(listFeedUrl, ListFeed.class);
            DataSet dataSet = new DataSet();
            for (ListEntry listEntry : feed.getEntries()) {
                IRow row = dataSet.createRow();
                for (AnalysisItem analysisItem : analysisItems) {
                    Key key = analysisItem.getKey();
                    for (String tag : listEntry.getCustomElements().getTags()) {
                        if (key.toKeyString().equals(tag)) {
                            Value value;
                            String string = listEntry.getCustomElements().getValue(tag);
                            if (string == null) {
                                value = new EmptyValue();
                            } else {
                                value = new StringValue(string);
                            }
                            row.addValue(new NamedKey(tag), value);
                        }
                    }
                }
            }
            return dataSet;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public DataSet getDetails(Collection<FilterDefinition> filters) {
        throw new UnsupportedOperationException();
    }
}
