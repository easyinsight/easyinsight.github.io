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
public class InfusionsoftCampaignStep extends InfusionsoftTableSource {

    public static final String ID = "Id";
    public static final String CAMPAIGN_ID = "CampaignId";
    public static final String TEMPLATE_ID = "TemplateId";
    public static final String STEP_STATUS = "StepStatus";
    public static final String STEP_TITLE = "StepTitle";

    public InfusionsoftCampaignStep() {
        setFeedName("Campaign Steps");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_CAMPAIGN_STEP;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID, CAMPAIGN_ID, TEMPLATE_ID, STEP_STATUS, STEP_TITLE);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(CAMPAIGN_ID), "Campaign Id"));
        analysisitems.add(new AnalysisDimension(keys.get(ID), "Invoice ID"));
        analysisitems.add(new AnalysisDimension(keys.get(TEMPLATE_ID), "Template Id"));
        analysisitems.add(new AnalysisDimension(keys.get(STEP_STATUS), "Step Status"));
        analysisitems.add(new AnalysisDimension(keys.get(STEP_TITLE), "Step Title"));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return query("CampaignStep", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}