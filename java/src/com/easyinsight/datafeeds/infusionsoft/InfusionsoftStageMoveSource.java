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
public class InfusionsoftStageMoveSource extends InfusionsoftTableSource {

    public static final String STAGE_MOVE_ID = "Id";
    public static final String OPPORTUNITY_ID = "OpportunityId";
    public static final String MOVE_DATE = "MoveDate";
    public static final String MOVE_TO_STAGE = "MoveToStage";
    public static final String MOVE_FROM_STAGE = "MoveFromStage";

    public InfusionsoftStageMoveSource() {
        setFeedName("Stage History");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_STAGE_HISTORY;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(STAGE_MOVE_ID, OPPORTUNITY_ID, MOVE_DATE, MOVE_TO_STAGE, MOVE_FROM_STAGE);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(STAGE_MOVE_ID), "Stage Move ID"));
        analysisitems.add(new AnalysisDimension(keys.get(OPPORTUNITY_ID), "Opportunity ID"));
        analysisitems.add(new AnalysisDimension(keys.get(MOVE_TO_STAGE), "Move to Stage"));
        analysisitems.add(new AnalysisDimension(keys.get(MOVE_FROM_STAGE), "Move from Stage"));
        analysisitems.add(new AnalysisDateDimension(keys.get(MOVE_DATE), "Move Date", AnalysisDateDimension.MINUTE_LEVEL));
        return analysisitems;
    }

    public List<AnalysisItem> createStageItems() {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(new NamedKey(InfusionsoftStageSource.STAGE_NAME), "Stage Name"));
        analysisitems.add(new AnalysisDimension(new NamedKey(InfusionsoftStageSource.STAGE_ID), "Stage ID"));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            DataSet stages = query("Stage", createStageItems(), (InfusionsoftCompositeSource) parentDefinition);
            Map<Value, Value> map = new HashMap<Value, Value>();
            for (IRow row : stages.getRows()) {
                Value stageID = row.getValue(new NamedKey(InfusionsoftStageSource.STAGE_ID));
                Value stageName = row.getValue(new NamedKey(InfusionsoftStageSource.STAGE_NAME));
                map.put(stageID, stageName);
            }
            DataSet stageMoveSet = query("StageMove", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
            for (IRow row : stageMoveSet.getRows()) {
                Value moveTo = row.getValue(keys.get(MOVE_TO_STAGE));
                Value moveToValue = map.get(moveTo);
                Value moveFrom = row.getValue(keys.get(MOVE_FROM_STAGE));
                Value moveFromValue = map.get(moveFrom);
                row.addValue(keys.get(MOVE_TO_STAGE), moveToValue);
                row.addValue(keys.get(MOVE_FROM_STAGE), moveFromValue);
            }
            return stageMoveSet;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
