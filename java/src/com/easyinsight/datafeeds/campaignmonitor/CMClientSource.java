package com.easyinsight.datafeeds.campaignmonitor;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import nu.xom.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 12/27/10
 * Time: 1:18 PM
 */
public class CMClientSource extends CampaignMonitorBaseSource {
    public static final String CLIENT_NAME = "Client Name";
    public static final String CLIENT_ID = "Client ID";
    public static final String CLIENT_COUNT = "Client Count";

    public CMClientSource() {
        setFeedName("Clients");
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        return Arrays.asList(CLIENT_NAME, CLIENT_ID, CLIENT_COUNT);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(CLIENT_NAME), true));
        items.add(new AnalysisDimension(keys.get(CLIENT_ID), true));
        items.add(new AnalysisMeasure(keys.get(CLIENT_COUNT), AggregationTypes.SUM));
        return items;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.CAMPAIGN_MONITOR_CLIENTS;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            CampaignMonitorDataSource ccSource = (CampaignMonitorDataSource) parentDefinition;
            DataSet dataSet = new DataSet();
            Document doc = query("clients.xml", ccSource.getCmApiKey(), parentDefinition);
            Nodes nodes = doc.query("/Clients/Client");
            for (int i = 0; i < nodes.size(); i++) {

                Node node = nodes.get(i);
                IRow row = dataSet.createRow();
                row.addValue(CLIENT_ID, queryField(node, "ClientID/text()"));
                row.addValue(CLIENT_NAME, queryField(node, "Name/text()"));
                row.addValue(CLIENT_COUNT, 1);
            }
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
