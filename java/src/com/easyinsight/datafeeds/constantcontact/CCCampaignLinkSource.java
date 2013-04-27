package com.easyinsight.datafeeds.constantcontact;

import com.easyinsight.analysis.*;
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
public class CCCampaignLinkSource extends ConstantContactBaseSource {


    public static final String CAMPAIGN_ID = "Link Campaign ID";
    public static final String LINK_URL = "Link URL";
    public static final String LINK_ID = "Campaign Link ID";

    public CCCampaignLinkSource() {
        setFeedName("Campaign Links");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(CAMPAIGN_ID, LINK_URL, LINK_ID);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(CAMPAIGN_ID), true));
        items.add(new AnalysisDimension(keys.get(LINK_URL), true));
        items.add(new AnalysisDimension(keys.get(LINK_ID), true));
        return items;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.CONSTANT_CONTACT_LINKS;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            ConstantContactCompositeSource ccSource = (ConstantContactCompositeSource) parentDefinition;
            DataSet dataSet = new DataSet();
            List<Campaign> campaigns = ccSource.getOrCreateCampaignCache().getOrCreateCampaigns(ccSource);
            for (Campaign campaign : campaigns) {
                for (CCCampaignLink link : campaign.getLinks()) {
                    IRow row = dataSet.createRow();
                    row.addValue(CAMPAIGN_ID, campaign.getId());
                    row.addValue(LINK_ID, link.getId());
                    row.addValue(LINK_URL, link.getUrl());
                }
            }
            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
