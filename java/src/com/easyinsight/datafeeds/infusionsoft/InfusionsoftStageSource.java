package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.Value;
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
            DataSet dataSet = query("Stage", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
            Map<String, String> map = new HashMap<>();
            for (IRow row : dataSet.getRows()) {
                String stageID = row.getValue(new NamedKey(InfusionsoftStageSource.STAGE_ID)).toString();
                String stageName = row.getValue(new NamedKey(InfusionsoftStageSource.STAGE_NAME)).toString();
                map.put(stageID, stageName);
            }
            ((InfusionsoftCompositeSource) parentDefinition).setLeadStageCache(map);
            return dataSet;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
