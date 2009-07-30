package com.easyinsight.datafeeds.test;

import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Key;
import com.easyinsight.analysis.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * User: James Boe
 * Date: Jun 16, 2009
 * Time: 10:40:45 AM
 */
public class TestGammaDataSource extends ServerDataSourceDefinition {

    public static final String DIM = "GammaDimension";
    public static final String PROJECT_DIM = "Project";
    public static final String PROJECT_NAME = "ProjectName";
    public static final String MEASURE = "GammaMeasure";

    public TestGammaDataSource() {
        setFeedName("Gamma");
    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(DIM, PROJECT_DIM, PROJECT_NAME, MEASURE);
    }

    public FeedType getFeedType() {
        return FeedType.TEST_GAMMA;
    }

    public DataSet getDataSet(com.easyinsight.users.Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition parentDefinition) {
        DataSet dataSet = new DataSet();
        IRow row1 = dataSet.createRow();
        row1.addValue(keys.get(DIM), "Gamma Row 1");
        row1.addValue(keys.get(PROJECT_DIM), "Project1");
        row1.addValue(keys.get(PROJECT_NAME), "Project Name 1");
        row1.addValue(keys.get(MEASURE), 10);
        IRow row2 = dataSet.createRow();
        row2.addValue(keys.get(DIM), "Gamma Row 2");
        row2.addValue(keys.get(PROJECT_DIM), "Project2");
        row2.addValue(keys.get(PROJECT_NAME), "Project Name 2");
        row2.addValue(keys.get(MEASURE), 20);
        return dataSet;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, com.easyinsight.users.Credentials credentials) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(DIM), true));
        analysisItems.add(new AnalysisDimension(keys.get(PROJECT_DIM), true));
        analysisItems.add(new AnalysisDimension(keys.get(PROJECT_NAME), true));
        analysisItems.add(new AnalysisMeasure(keys.get(MEASURE), AggregationTypes.SUM));
        return analysisItems;
    }
}