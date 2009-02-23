package com.easyinsight.analysis;

import com.easyinsight.analysis.ListDataResults;
import com.easyinsight.analysis.ListRow;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.dataset.DataSet;

import java.util.*;

/**
 * User: James Boe
 * Date: Jan 9, 2008
 * Time: 10:35:37 PM
 */
public class ListTransform {

    private Map<Map<Key, Value>, Map<AnalysisMeasure, Aggregation>> keyMap = new HashMap<Map<Key, Value>, Map<AnalysisMeasure, Aggregation>>();
    private Map<Map<Key, Value>, Map<AnalysisDimension, Value>> dimensionMap = new HashMap<Map<Key, Value>, Map<AnalysisDimension, Value>>();
    private Set<Map<Key, Value>> compositeKeys = new HashSet<Map<Key, Value>>();
    private Map<AnalysisMeasure, AggregationFactory> factoryMap = new HashMap<AnalysisMeasure, AggregationFactory>();
    private List<TemporalAnalysisMeasure> temporalAnalysisMeasures = new ArrayList<TemporalAnalysisMeasure>();

    public ListTransform(List<AnalysisItem> analysisItems) {
        for (AnalysisItem analysisItem : analysisItems) {
            if (analysisItem.hasType(AnalysisItemTypes.TEMPORAL_MEASURE)) {
                temporalAnalysisMeasures.add((TemporalAnalysisMeasure) analysisItem);
            }
        }
    }

    public void groupData(Map<Key, Value> compositeDimensionKey, AnalysisMeasure measure, Value value) {
        compositeKeys.add(compositeDimensionKey);
        Map<AnalysisMeasure, Aggregation> values = keyMap.get(compositeDimensionKey);
        if (values == null) {
            values = new HashMap<AnalysisMeasure, Aggregation>();
            keyMap.put(compositeDimensionKey, values);
        }
        AggregationFactory aggregationFactory = factoryMap.get(measure);
        if (aggregationFactory == null) {
            aggregationFactory = new AggregationFactory(measure);
            factoryMap.put(measure, aggregationFactory);
        }
        Aggregation aggregation = values.get(measure);
        if (aggregation == null) {
            aggregation = aggregationFactory.getAggregation();
            values.put(measure, aggregation);
        }
        aggregation.addValue(value);
    }

    public void groupData(Map<Key, Value> compositeDimensionKey, AnalysisDimension dimension, Value value) {
        compositeKeys.add(compositeDimensionKey);
        Map<AnalysisDimension, Value> values = dimensionMap.get(compositeDimensionKey);
        if (values == null) {
            values = new HashMap<AnalysisDimension, Value>();
            dimensionMap.put(compositeDimensionKey, values);
        }
        values.put(dimension, value);
    }

    public DataSet aggregate(List<AnalysisItem> columns, List<AnalysisItem> derivedItems, List<AnalysisItem> allRequestedAnalysisItems) {
        DataSet dataSet = new DataSet();
        for (Map<Key, Value> compositeKey : compositeKeys) {
            IRow row = dataSet.createRow();
            Map<AnalysisDimension, Value> dimensions = dimensionMap.get(compositeKey);
            Map<AnalysisMeasure, Aggregation> measures = keyMap.get(compositeKey);
            Map<AnalysisItem, Value> superSet = new HashMap<AnalysisItem, Value>();
            if (dimensions != null) superSet.putAll(dimensions);
            if (measures != null) superSet.putAll(measures);
            for (AnalysisItem column : columns) {
                if (column.hasType(AnalysisItemTypes.CALCULATION)) {
                    continue;
                }
                Value obj = superSet.get(column);
                if (obj == null) { obj = new EmptyValue(); }
                if (obj instanceof Aggregation) {
                    Aggregation aggregation = (Aggregation) obj;
                    obj = aggregation.getValue();
                }
                row.addValue(new AggregateKey(column.getKey(), column.getType()), obj);
            }
        }

        // for each post processor, run the values through again...
        for (TemporalAnalysisMeasure temporalAnalysisMeasure : temporalAnalysisMeasures) {
            dataSet = new TemporalChangeTransform(temporalAnalysisMeasure, allRequestedAnalysisItems).blah(dataSet);
        }

        if (derivedItems != null) {
            for (IRow row : dataSet.getRows()) {
                for (AnalysisItem analysisItem : derivedItems) {
                    if (analysisItem.hasType(AnalysisItemTypes.CALCULATION)) {
                        AnalysisCalculation analysisCalculation = (AnalysisCalculation) analysisItem;
                        Value value = analysisCalculation.calculate(dataSet, row);
                        row.addValue(new AggregateKey(analysisCalculation.getKey(), analysisCalculation.getType()), value);
                    } else if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
                        AnalysisHierarchyItem analysisHierarchy = (AnalysisHierarchyItem) analysisItem;
                        AggregateKey aggregateKey = new AggregateKey(analysisHierarchy.getHierarchyLevel().getAnalysisItem().getKey(), AnalysisItemTypes.DIMENSION);
                        row.addValue(new AggregateKey(analysisHierarchy.getKey(), analysisHierarchy.getType()), row.getValue(aggregateKey));
                    }
                }
            }
        }

        // now...
        // measure filtering should be applied AFTER aggregation, not before...
        return dataSet;
    }

    public ListDataResults toListDataResults(List<AnalysisItem> columns, DataSet dataSet) {
        ListRow[] listRows = new ListRow[dataSet.getRows().size()];
        int rowCount = 0;
        for (IRow row : dataSet.getRows()) {
            int columnCount = 0;
            listRows[rowCount] = new ListRow();
            Value[] values = new Value[row.getKeys().size()];
            listRows[rowCount].setValues(values);
            for (AnalysisItem analysisItem : columns) {
                Key key = new AggregateKey(analysisItem.getKey(), analysisItem.getType());
                listRows[rowCount].getValues()[columnCount] = row.getValue(key);
                columnCount++;
            }
            rowCount++;
        }
        ListDataResults listDataResults = new ListDataResults();
        List<AnalysisItem> allColumns = new ArrayList<AnalysisItem>(columns);
        AnalysisItem[] headers = new AnalysisItem[allColumns.size()];
        allColumns.toArray(headers);
        listDataResults.setHeaders(headers);
        listDataResults.setRows(listRows);
        return listDataResults;
    }
}
