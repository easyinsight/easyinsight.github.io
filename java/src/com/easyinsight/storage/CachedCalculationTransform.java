package com.easyinsight.storage;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.dataset.DataSet;

import java.util.*;

/**
 * User: jamesboe
 * Date: 4/3/12
 * Time: 11:36 AM
 */
public class CachedCalculationTransform implements IDataTransform {

    private List<AnalysisItem> calculations;
    private FeedDefinition dataSource;


    public CachedCalculationTransform(FeedDefinition dataSource) {
        this.dataSource = dataSource;
        calculations = new ArrayList<AnalysisItem>();
        for (AnalysisItem analysisItem : dataSource.getFields()) {
            if (analysisItem.hasType(AnalysisItemTypes.CALCULATION)) {
                AnalysisCalculation analysisCalculation = (AnalysisCalculation) analysisItem;
                if (analysisCalculation.isCachedCalculation()) {
                    calculations.add(analysisCalculation);
                }
            }
        }
    }

    public void handle(EIConnection conn, IRow row) {
        if (calculations.size() == 0) {
            return;
        }
        List<AnalysisItem> allItems = dataSource.getFields();
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
        //List<AnalysisItem> items = ReportCalculation.getAnalysisItems(calculation.getCalculationString(), allItems, keyMap, displayMap, new ArrayList<AnalysisItem>(), true, true, CleanupComponent.AGGREGATE_CALCULATIONS);
        DataSet dataSet = new DataSet();
        IRow newRow = dataSet.createRow();

        for (AnalysisItem analysisItem : allItems) {
            Value value = row.getValue(analysisItem.getKey());
            if (value != null && value.toDouble() != null && value.toDouble() != 0) {

            }
            newRow.addValue(analysisItem.createAggregateKey(), row.getValue(analysisItem.getKey()));
        }

        Feed feed = FeedRegistry.instance().getFeed(dataSource.getDataFeedID());
        WSListDefinition blah = new WSListDefinition();
        blah.setDataFeedID(dataSource.getDataFeedID());
        blah.setFilterDefinitions(new ArrayList<FilterDefinition>());
        blah.setColumns(calculations);
        List<AnalysisCalculation> calculationlist = new ArrayList<AnalysisCalculation>();
        for (AnalysisItem item : calculations) {
            calculationlist.add((AnalysisCalculation) item);
        }
        CacheCalculationPipeline pipeline = new CacheCalculationPipeline(calculationlist);
        pipeline.setup(blah, feed, new InsightRequestMetadata());
        DataSet resultSet = pipeline.toDataSet(dataSet);
        IRow result = resultSet.getRow(0);
        for (AnalysisItem calculation : calculations) {
            Value targetValue = result.getValue(calculation);

            row.addValue(calculation.getKey(), targetValue);
        }
    }
}
