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
public class TestBetaDataSource extends ServerDataSourceDefinition {

    public static final String DIM = "BetaDimension";
    public static final String JOIN_DIM = "JoinDimension";
    public static final String MEASURE = "BetaMeasure";

    public TestBetaDataSource() {
        setFeedName("Beta");
    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(DIM, JOIN_DIM, MEASURE);
    }

    public FeedType getFeedType() {
        return FeedType.TEST_BETA;
    }

    public DataSet getDataSet(com.easyinsight.users.Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition parentDefinition) {
        DataSet dataSet = new DataSet();
        IRow row1 = dataSet.createRow();
        row1.addValue(keys.get(DIM), "Beta Row 1");
        row1.addValue(keys.get(JOIN_DIM), "X");
        row1.addValue(keys.get(MEASURE), 100);
        IRow row2 = dataSet.createRow();
        row2.addValue(keys.get(DIM), "Beta Row 2");
        row2.addValue(keys.get(JOIN_DIM), "Y");
        row2.addValue(keys.get(MEASURE), 200);
        return dataSet;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, com.easyinsight.users.Credentials credentials) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(DIM), true));
        analysisItems.add(new AnalysisDimension(keys.get(JOIN_DIM), true));
        analysisItems.add(new AnalysisMeasure(keys.get(MEASURE), AggregationTypes.SUM));
        return analysisItems;
    }
}