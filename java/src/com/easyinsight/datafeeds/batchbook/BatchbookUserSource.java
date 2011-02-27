package com.easyinsight.datafeeds.batchbook;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 1/18/11
 * Time: 10:07 AM
 */
public class BatchbookUserSource extends BatchbookBaseSource {
    public static final String USERNAME = "User Name";
    public static final String EMAIL = "Email Address";

    public BatchbookUserSource() {
        setFeedName("Users");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BATCHBOOK_USERS;
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        return Arrays.asList(USERNAME, EMAIL);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Connection conn) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(USERNAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(EMAIL), true));
        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        BatchbookCompositeSource batchbookCompositeSource = (BatchbookCompositeSource) parentDefinition;
        HttpClient httpClient = getHttpClient(batchbookCompositeSource.getBbApiKey(), "");
        try {
            Document deals = runRestRequest("/service/users.xml", httpClient, new Builder(), batchbookCompositeSource.getUrl(), parentDefinition);
            Nodes dealNodes = deals.query("/users/user");
            for (int i = 0; i < dealNodes.size(); i++) {
                Node dealNode = dealNodes.get(i);
                IRow row = dataSet.createRow();
                row.addValue(keys.get(USERNAME), queryField(dealNode, "name/text()"));
                row.addValue(keys.get(EMAIL), queryField(dealNode, "email/text()"));
            }
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dataSet;
    }
}
