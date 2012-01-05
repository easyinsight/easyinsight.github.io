package com.easyinsight.datafeeds.constantcontact;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: Nov 7, 2010
 * Time: 10:56:37 AM
 */
public class CCCampaignSource extends ConstantContactBaseSource {

    public static final String CAMPAIGN_NAME = "Campaign Name";
    public static final String CAMPAIGN_ID = "Campaign ID";
    public static final String CAMPAIGN_STATUS = "Campaign Status";
    public static final String CAMPAIGN_DATE = "Campaign Date";
    public static final String CAMPAIGN_URL = "Campaign URL";
    public static final String CAMPAIGN_COUNT = "Campaign Count";

    public CCCampaignSource() {
        setFeedName("Campaigns");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(CAMPAIGN_NAME, CAMPAIGN_ID, CAMPAIGN_STATUS, CAMPAIGN_DATE, CAMPAIGN_COUNT,
                CAMPAIGN_URL);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(CAMPAIGN_NAME), true));
        items.add(new AnalysisDimension(keys.get(CAMPAIGN_ID), true));
        items.add(new AnalysisDimension(keys.get(CAMPAIGN_STATUS), true));
        items.add(new AnalysisDimension(keys.get(CAMPAIGN_URL), true));
        items.add(new AnalysisDateDimension(keys.get(CAMPAIGN_DATE), true, AnalysisDateDimension.DAY_LEVEL));
        items.add(new AnalysisMeasure(keys.get(CAMPAIGN_COUNT), AggregationTypes.SUM));
        return items;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.CONSTANT_CONTACT_CAMPAIGN;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            ConstantContactCompositeSource ccSource = (ConstantContactCompositeSource) parentDefinition;
            DataSet dataSet = new DataSet();
            List<Campaign> campaigns = ccSource.getOrCreateCampaignCache().getOrCreateCampaigns(ccSource);
            for (Campaign campaign : campaigns) {
                IRow row = dataSet.createRow();
                row.addValue(CAMPAIGN_ID, campaign.getId());
                row.addValue(CAMPAIGN_NAME, campaign.getName());
                row.addValue(CAMPAIGN_STATUS, campaign.getStatus());
                row.addValue(CAMPAIGN_DATE, new DateValue(campaign.getDate()));
                row.addValue(CAMPAIGN_COUNT, 1);
            }
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
