package com.easyinsight.datafeeds.google;

import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.CredentialRequirement;
import com.easyinsight.datafeeds.CredentialsDefinition;
import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.*;
import com.easyinsight.users.Credentials;
import com.easyinsight.logging.LogClass;
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

    public GoogleSpreadsheetFeed(String worksheetURL) {
        this.worksheetURL = worksheetURL;
    }

    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata) {
        try {
            AnalysisItemResultMetadata metadata = analysisItem.createResultMetadata();
            Credentials credentials = insightRequestMetadata.getCredentialForDataSource(getFeedID());
            SpreadsheetService myService = GoogleSpreadsheetAccess.getOrCreateSpreadsheetService(credentials);
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

    public Set<CredentialRequirement> getCredentialRequirement(boolean allSources) {
        Set<CredentialRequirement> credentials = super.getCredentialRequirement(allSources);
        CredentialRequirement requirement = new CredentialRequirement();
        requirement.setDataSourceID(getFeedID());
        requirement.setDataSourceName(getName());
        requirement.setCredentialsDefinition(CredentialsDefinition.STANDARD_USERNAME_PW);
        credentials.add(requirement);
        return credentials;
    }

    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, Collection<Key> additionalNeededKeys) {
        try {
            Credentials credentials = insightRequestMetadata.getCredentialForDataSource(getFeedID());
            SpreadsheetService myService = GoogleSpreadsheetAccess.getOrCreateSpreadsheetService(credentials);
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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
