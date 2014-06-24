package com.easyinsight.datafeeds.sample;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 6/18/14
 * Time: 4:47 PM
 */
public class SampleSalesHistorySource extends ServerDataSourceDefinition {
    public static final String OPPORTUNITY_ID = "Opportunity ID";
    public static final String STAGE = "Stage";
    public static final String STAGE_DATE = "Stage Date";

    public SampleSalesHistorySource() {
        setFeedName("Sales History");
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(OPPORTUNITY_ID, new AnalysisDimension());
        fieldBuilder.addField(STAGE, new AnalysisDimension());
        fieldBuilder.addField(STAGE_DATE, new AnalysisDateDimension());
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        try {
            SampleDataSource sampleDataSource = (SampleDataSource) parentDefinition;
            List<OpportunityData> opportunities = sampleDataSource.getOrCreateOpportunities();
            for (OpportunityData opportunityData : opportunities) {
                List<OpportunityData.StageHistory> histories = opportunityData.getHistory();
                for (OpportunityData.StageHistory history : histories) {
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(OPPORTUNITY_ID), opportunityData.getId());
                    row.addValue(keys.get(STAGE), history.stage);
                    row.addValue(keys.get(STAGE_DATE), history.date);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dataSet;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SALES_HISTORY;
    }
}
