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
 * Date: 4/24/13
 * Time: 11:30 AM
 */
public class InfusionsoftStageSource extends InfusionsoftTableSource {

    public static final String STAGE_ID = "Id";
    public static final String STAGE_NAME = "StageName";
    public static final String STAGE_ORDER = "StageOrder";

    public InfusionsoftStageSource() {
        setFeedName("Stages");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_STAGE;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(STAGE_ID, STAGE_NAME, STAGE_ORDER);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(STAGE_NAME), "Stage Name"));
        analysisitems.add(new AnalysisDimension(keys.get(STAGE_ORDER), "Stage Order"));
        analysisitems.add(new AnalysisDimension(keys.get(STAGE_ID), "Stage ID"));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return query("Stage", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
