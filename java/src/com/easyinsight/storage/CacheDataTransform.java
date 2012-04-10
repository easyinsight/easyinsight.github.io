package com.easyinsight.storage;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DerivedKey;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.pipeline.CleanupComponent;

import java.util.*;

/**
 * User: jamesboe
 * Date: 4/3/12
 * Time: 11:36 AM
 */
public class CacheDataTransform implements IDataTransform {

    private AnalysisCalculation calculation;
    private AnalysisItem endField;
    private FeedDefinition dataSource;
    private FeedDefinition baseSource;

    // cache("ACS2", "Calc Weighted Procedures", "Weighted Procedures")


    public CacheDataTransform(AnalysisCalculation calculation, AnalysisItem endField, FeedDefinition dataSource, FeedDefinition baseSource) {
        this.calculation = calculation;
        this.endField = endField;
        this.dataSource = dataSource;
        this.baseSource = baseSource;
    }

    public void handle(EIConnection conn, IRow row) {
        List<AnalysisItem> allItems = dataSource.getFields();
        Map<String, AnalysisItem> baseMap = new HashMap<String, AnalysisItem>();
        for (AnalysisItem analysisItem : baseSource.getFields()) {
            String string = analysisItem.getType() + "-" + analysisItem.getKey().toBaseKey().toKeyString();
            baseMap.put(string, analysisItem);
        }
        Map<String, List<AnalysisItem>> keyMap = new HashMap<String, List<AnalysisItem>>();
        Map<String, List<AnalysisItem>> displayMap = new HashMap<String, List<AnalysisItem>>();
        for (AnalysisItem analysisItem : allItems) {
            List<AnalysisItem> items = keyMap.get(analysisItem.getKey().toKeyString());
            if (items == null) {
                items = new ArrayList<AnalysisItem>(1);
                keyMap.put(analysisItem.getKey().toKeyString(), items);
            }
            items.add(analysisItem);
        }
        for (AnalysisItem analysisItem : allItems) {
            List<AnalysisItem> items = displayMap.get(analysisItem.toDisplay());
            if (items == null) {
                items = new ArrayList<AnalysisItem>(1);
                displayMap.put(analysisItem.toDisplay(), items);
            }
            items.add(analysisItem);
        }
        List<AnalysisItem> items = ReportCalculation.getAnalysisItems(calculation.getCalculationString(), allItems, keyMap, displayMap, new ArrayList<AnalysisItem>(), true, true, CleanupComponent.AGGREGATE_CALCULATIONS);
        DataSet dataSet = new DataSet();
        IRow newRow = dataSet.createRow();
        Set<AnalysisItem> needToRetrieve = new HashSet<AnalysisItem>();
        for (AnalysisItem analysisItem : items) {
            Key key = analysisItem.getKey();
            long id = resolveToDataSource(key);
            if (id == baseSource.getDataFeedID()) {
                String string = analysisItem.getType() + "-" + analysisItem.getKey().toBaseKey().toKeyString();
                AnalysisItem lookup = baseMap.get(string);
                Value value = row.getValues().get(lookup.getKey());
                newRow.addValue(analysisItem.createAggregateKey(), value);
            } else {
                needToRetrieve.add(analysisItem);    
            }
        }
        Feed feed = FeedRegistry.instance().getFeed(dataSource.getDataFeedID());
        DataSet otherSet = feed.getAggregateDataSet(needToRetrieve, new ArrayList<FilterDefinition>(), new InsightRequestMetadata(), feed.getFields(), false, conn);
        IRow otherRow = otherSet.getRow(0);
        for (AnalysisItem item : needToRetrieve) {
            newRow.addValue(item.createAggregateKey(), otherRow.getValue(item));
        }
        WSListDefinition blah = new WSListDefinition();
        blah.setDataFeedID(dataSource.getDataFeedID());
        blah.setFilterDefinitions(new ArrayList<FilterDefinition>());
        blah.setColumns(Arrays.asList((AnalysisItem) calculation));
        CacheCalculationPipeline pipeline = new CacheCalculationPipeline(Arrays.asList(calculation));
        pipeline.setup(blah, feed, new InsightRequestMetadata());
        DataSet resultSet = pipeline.toDataSet(dataSet);
        IRow result = resultSet.getRow(0);
        Value targetValue = result.getValue(calculation);
        row.getValues().put(endField.getKey(), targetValue);
    }

    private long resolveToDataSource(Key key) {
        if (key instanceof DerivedKey) {
            DerivedKey derivedKey = (DerivedKey) key;
            if (derivedKey.getParentKey() instanceof NamedKey) {
                return derivedKey.getFeedID();
            }
            return resolveToDataSource(derivedKey.getParentKey());
        }
        return 0;
    }
}
