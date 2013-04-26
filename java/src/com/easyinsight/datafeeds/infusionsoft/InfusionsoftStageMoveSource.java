package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.ReportException;
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
        analysisitems.add(new AnalysisDimension(keys.get(OPPORTUNITY_ID), "Stage Order"));
        analysisitems.add(new AnalysisDimension(keys.get(MOVE_TO_STAGE), "Move to Stage"));
        analysisitems.add(new AnalysisDimension(keys.get(MOVE_FROM_STAGE), "Move from Stage"));
        analysisitems.add(new AnalysisDateDimension(keys.get(MOVE_DATE), "Move Date", AnalysisDateDimension.MINUTE_LEVEL));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return query("StageMove", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
