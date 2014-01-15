package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 12/30/13
 * Time: 5:08 PM
 */
public class InfusionsoftCampaigneeSource extends InfusionsoftTableSource {

    public static final String CAMPAIGN_ID = "CampaignId";
    public static final String STATUS = "Status";
    public static final String CONTACT_ID = "ContactId";

    public InfusionsoftCampaigneeSource() {
        setFeedName("Campaignees");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_CAMPAIGNEE;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(CAMPAIGN_ID, STATUS, CONTACT_ID);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(CONTACT_ID), "Contact ID"));
        analysisitems.add(new AnalysisDimension(keys.get(CAMPAIGN_ID), "Campaign ID"));
        analysisitems.add(new AnalysisDimension(keys.get(STATUS), "Status"));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return query("Campaignee", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}