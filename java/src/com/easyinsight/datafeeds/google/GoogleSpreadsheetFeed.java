package com.easyinsight.datafeeds.google;

import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.*;
import com.easyinsight.logging.LogClass;
import com.google.gdata.client.authn.oauth.GoogleOAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer;
import com.google.gdata.client.authn.oauth.OAuthSigner;
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
    private String tokenKey;
    private String tokenSecret;

    private SpreadsheetService as;

    public GoogleSpreadsheetFeed(String worksheetURL, String tokenKey, String tokenSecret) {
        this.worksheetURL = worksheetURL;
        this.tokenKey = tokenKey;
        this.tokenSecret = tokenSecret;
    }

    private SpreadsheetService getService() throws OAuthException {
        if (as == null) {
            as = new SpreadsheetService("easyinsight_eidocs_v1.0");
            GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
            oauthParameters.setOAuthConsumerKey("www.easy-insight.com");
            oauthParameters.setOAuthConsumerSecret("OG0zlkZFPIe7JdHfLB8qXXYv");
            oauthParameters.setOAuthToken(tokenKey);
            oauthParameters.setOAuthTokenSecret(tokenSecret);
            oauthParameters.setScope("https://docs.google.com/feeds/");
            OAuthSigner signer = new OAuthHmacSha1Signer();
            as.setOAuthCredentials(oauthParameters, signer);
        }
        return as;
    }

    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws ReportException {
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

    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
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
                                string = string.trim();
                                if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                                    value = new NumericValue(NumericValue.produceDoubleValue(string));
                                } else if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                                    AnalysisDateDimension date = (AnalysisDateDimension) analysisItem;
                                    value = date.renameMeLater(new StringValue(string));
                                } else {
                                    value = new StringValue(string);
                                }
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
}
