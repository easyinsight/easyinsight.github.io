package com.easyinsight.datafeeds.google;

import com.easyinsight.datafeeds.Feed;
import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.*;
import com.easyinsight.users.TokenStorage;
import com.easyinsight.users.Token;
import com.easyinsight.users.Utility;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.config.ConfigLoader;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.AuthenticationException;

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

    private SpreadsheetService as;

    public GoogleSpreadsheetFeed(String worksheetURL) {
        this.worksheetURL = worksheetURL;
    }

    private String getToken() throws TokenMissingException {
        if (token == null) {
            Token tokenObject = new TokenStorage().getToken(SecurityUtil.getUserID(false), TokenStorage.GOOGLE_DOCS_TOKEN, getFeedID(), true);
            if (tokenObject == null) {
                throw new TokenMissingException();
            }
            token = tokenObject.getTokenValue();
        }
        return token;
    }

    private SpreadsheetService getService() throws AuthenticationException, TokenMissingException {
        if (as == null) {
            as = new SpreadsheetService("easyinsight_eidocs_v1.0");
            try {
                String token = getToken();
                as.setAuthSubToken(token, Utility.getPrivateKey());
            } catch (TokenMissingException e) {
                if (ConfigLoader.instance().getGoogleUserName() != null && !"".equals(ConfigLoader.instance().getGoogleUserName())) {
                    as.setUserCredentials(ConfigLoader.instance().getGoogleUserName(), ConfigLoader.instance().getGooglePassword());
                } else {
                    throw e;
                }
            }
        }
        return as;
    }

    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata) {
        try {
            AnalysisItemResultMetadata metadata = analysisItem.createResultMetadata();
            SpreadsheetService myService = getService();
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

    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode) {
        try {
            SpreadsheetService myService = getService();
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
                            row.addValue(analysisItem.createAggregateKey(), value);
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
