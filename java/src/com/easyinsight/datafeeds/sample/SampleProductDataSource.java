package com.easyinsight.datafeeds.sample;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 6/8/11
 * Time: 10:02 PM
 */
public class SampleProductDataSource extends ServerDataSourceDefinition {
    public static final String PRODUCT_NAME = "Product Name";
    public static final String PRODUCT_COST = "Product Cost";

    private static final String[] productNames = { "Widget1", "Widget2", "Widget3", "Widget4", "Widget5"};
    private static final int[] costs = { 50, 80, 120, 140, 180 };

    public SampleProductDataSource() {
        setFeedName("Product");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SAMPLE_PRODUCT;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(PRODUCT_NAME, PRODUCT_COST);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(PRODUCT_NAME)));
        items.add(new AnalysisMeasure(keys.get(PRODUCT_COST), "Product Cost", AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        return items;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        for (int i = 0; i < 5; i++) {
            IRow row = dataSet.createRow();
            row.addValue(keys.get(PRODUCT_NAME), productNames[i]);
            row.addValue(keys.get(PRODUCT_COST), costs[i]);
        }
        return dataSet;
    }
}
