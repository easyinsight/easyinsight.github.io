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
        System.out.println("*** " + calculation.getCalculationString());
        List<AnalysisItem> items = ReportCalculation.getAnalysisItems(calculation.getCalculationString(), allItems, keyMap, displayMap, new ArrayList<AnalysisItem>(), true, true, CleanupComponent.AGGREGATE_CALCULATIONS);
        DataSet dataSet = new DataSet();
        IRow newRow = dataSet.createRow();
        Set<AnalysisItem> needToRetrieve = new HashSet<AnalysisItem>();
        AnalysisItem joinDim = null;
        Object providerID = null;
        for (AnalysisItem analysisItem : allItems) {
            if ("Providers - Record ID#".equals(analysisItem.toDisplay())) {
                joinDim = analysisItem;
            } else if ("Related Provider".equals(analysisItem.toDisplay())) {
                String string = analysisItem.getType() + "-" + analysisItem.getKey().toBaseKey().toKeyString();
                AnalysisItem lookup = baseMap.get(string);
                Value value = row.getValues().get(lookup.getKey());
                providerID = value.toString();
            }
        }
        System.out.println("related provider = " + providerID);

        for (AnalysisItem analysisItem : items) {
            System.out.println("Looking for " + analysisItem.toDisplay());
            Key key = analysisItem.getKey();
            long id = resolveToDataSource(key);

            if (id == baseSource.getDataFeedID()) {
                String string = analysisItem.getType() + "-" + analysisItem.getKey().toBaseKey().toKeyString();
                AnalysisItem lookup = baseMap.get(string);
                Value value = row.getValues().get(lookup.getKey());
                System.out.println(string + " = " + value);
                newRow.addValue(analysisItem.createAggregateKey(), value);
            } else {
                System.out.println("need to retrieve " + analysisItem.toDisplay());
                needToRetrieve.add(analysisItem);
            }
        }
        Feed feed = FeedRegistry.instance().getFeed(dataSource.getDataFeedID());
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        if (!needToRetrieve.isEmpty()) {
            FilterValueDefinition filter = new FilterValueDefinition(joinDim, true, Arrays.asList(providerID));
            filters.add(filter);

            DataSet otherSet = feed.getAggregateDataSet(needToRetrieve, filters, new InsightRequestMetadata(), feed.getFields(), false, conn);
            IRow otherRow = otherSet.getRow(0);
            for (AnalysisItem item : needToRetrieve) {
                System.out.println(item.toDisplay() + " = " + otherRow.getValue(item));
                newRow.addValue(item.createAggregateKey(), otherRow.getValue(item));
            }
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
        System.out.println("result value = " + targetValue + " for " + endField.getKey().toKeyString());
        row.addValue(endField.getKey(), targetValue);
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
