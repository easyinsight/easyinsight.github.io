package com.easyinsight.datafeeds.test;

import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Key;
import com.easyinsight.analysis.*;
import com.easyinsight.storage.DataStorage;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.sql.Connection;

/**
 * User: James Boe
 * Date: Jun 16, 2009
 * Time: 10:40:45 AM
 */
public class TestAlphaDataSource extends ServerDataSourceDefinition {

    public static final String DIM = "AlphaDimension";
    public static final String TODO_DIM = "TodoDimension";
    public static final String PROJECT_DIM = "Project";
    public static final String MEASURE = "AlphaMeasure";

    public TestAlphaDataSource() {
        setFeedName("Alpha");
    }

    @NotNull
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(DIM, TODO_DIM, PROJECT_DIM, MEASURE);
    }

    public FeedType getFeedType() {
        return FeedType.TEST_ALPHA;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
        DataSet dataSet = new DataSet();
        IRow row1 = dataSet.createRow();
        row1.addValue(keys.get(DIM), "Alpha Row 1");
        row1.addValue(keys.get(TODO_DIM), "X");
        row1.addValue(keys.get(PROJECT_DIM), "Project1");
        row1.addValue(keys.get(MEASURE), 500);
        IRow row2 = dataSet.createRow();
        row2.addValue(keys.get(DIM), "Alpha Row 2");
        row2.addValue(keys.get(TODO_DIM), "Y");
        row2.addValue(keys.get(PROJECT_DIM), "Project2");
        row2.addValue(keys.get(MEASURE), 1000);
        IRow row3 = dataSet.createRow();
        row3.addValue(keys.get(DIM), "Alpha Row 3");
        row3.addValue(keys.get(PROJECT_DIM), "Project1");
        row3.addValue(keys.get(MEASURE), 500);
        return dataSet;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(DIM), true));
        analysisItems.add(new AnalysisDimension(keys.get(PROJECT_DIM), true));
        analysisItems.add(new AnalysisDimension(keys.get(TODO_DIM), true));
        analysisItems.add(new AnalysisMeasure(keys.get(MEASURE), AggregationTypes.SUM));
        return analysisItems;
    }
}
