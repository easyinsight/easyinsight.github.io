package com.easyinsight.datafeeds.campaignmonitor;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 12/28/10
 * Time: 2:00 PM
 */
public class CMCampaignSource extends CampaignMonitorBaseSource {
    public static final String CAMPAIGN_NAME = "Client Name";
    public static final String CAMPAIGN_SUBJECT = "Client ID";
    public static final String CAMPAIGN_ID = "Client Count";
    public static final String CAMPAIGN_SENT_DATE = "Client Count";

    public CMCampaignSource() {
        setFeedName("Campaigns");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(CAMPAIGN_NAME, CAMPAIGN_SUBJECT, CAMPAIGN_ID, CAMPAIGN_SENT_DATE);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(CAMPAIGN_NAME), true));
        items.add(new AnalysisDimension(keys.get(CAMPAIGN_SUBJECT), true));
        items.add(new AnalysisDimension(keys.get(CAMPAIGN_ID), true));
        items.add(new AnalysisDateDimension(keys.get(CAMPAIGN_SENT_DATE), true, AnalysisDateDimension.DAY_LEVEL));
        return items;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.CAMPAIGN_MONITOR_CLIENTS;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            CampaignMonitorDataSource ccSource = (CampaignMonitorDataSource) parentDefinition;
            DataSet dataSet = new DataSet();
            Document doc = query("clients.xml", ccSource.getCmApiKey(), parentDefinition);
            Nodes nodes = doc.query("/Clients/Client");
            for (int i = 0; i < nodes.size(); i++) {

                Node node = nodes.get(i);
                String clientID = queryField(node, "ClientID/text()");
                Document campaigns = query("clients/" + clientID + "/campaigns.xml", ccSource.getCmApiKey(), parentDefinition);
                /*row.addValue(CLIENT_ID, queryField(node, "ClientID/text()"));
                row.addValue(CLIENT_NAME, queryField(node, "Name/text()"));
                row.addValue(CLIENT_COUNT, 1);*/
            }
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
