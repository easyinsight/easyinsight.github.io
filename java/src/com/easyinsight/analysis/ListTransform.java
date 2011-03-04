package com.easyinsight.analysis;

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

    private Map<Map<Key, Value>, Aggregation[]> keyMap = new HashMap<Map<Key, Value>, Aggregation[]>();
    private Map<Map<Key, Value>, Value[]> dimensionMap = new HashMap<Map<Key, Value>, Value[]>();

    private Set<Map<Key, Value>> compositeKeys = new HashSet<Map<Key, Value>>();

    private Map<AnalysisMeasure, AggregationFactory> factoryMap = new HashMap<AnalysisMeasure, AggregationFactory>();

    private Map<AnalysisItem, Integer> dimensionIndexMap = new HashMap<AnalysisItem, Integer>();
    private Map<AnalysisItem, Integer> measureIndexMap = new HashMap<AnalysisItem, Integer>();

    private Set<Integer> skipAggregations;

    public ListTransform(Set<Integer> skipAggregations) {
        this.skipAggregations = skipAggregations;
    }

    public void groupData(Map<Key, Value> compositeDimensionKey, AnalysisMeasure measure, Value value, IRow row, int measures) {
        if (!compositeKeys.contains(compositeDimensionKey)) {
            compositeKeys.add(compositeDimensionKey);
        }
        Integer position = measureIndexMap.get(measure);
        if (position == null) {
            position = measureIndexMap.size();
            measureIndexMap.put(measure, position);
        }
        Aggregation[] values = keyMap.get(compositeDimensionKey);

        if (values == null) {
            values = new Aggregation[position + 1];
            keyMap.put(compositeDimensionKey, values);
        } else if (position >= values.length) {
            Aggregation[] copyTarget = new Aggregation[position + 1];
            System.arraycopy(values, 0, copyTarget, 0, values.length);
            values = copyTarget;
            keyMap.put(compositeDimensionKey, values);
        }

        AggregationFactory aggregationFactory = factoryMap.get(measure);
        if (aggregationFactory == null) {
            aggregationFactory = new AggregationFactory(measure, skipAggregations.contains(measure.getAggregation()));
            factoryMap.put(measure, aggregationFactory);
        }

        Aggregation aggregation = values[position];
        if (aggregation == null) {
            aggregation = aggregationFactory.getAggregation();
            values[position] = aggregation;
        }
        aggregation.addValue(value);
    }

    public void groupData(Map<Key, Value> compositeDimensionKey, AnalysisDimension dimension, Value value, int dimensions) {
        if (!compositeKeys.contains(compositeDimensionKey)) {
            compositeKeys.add(compositeDimensionKey);
        }
        Integer position = dimensionIndexMap.get(dimension);
        if (position == null) {
            position = dimensionIndexMap.size();
            dimensionIndexMap.put(dimension, position);
        }

        Value[] values = dimensionMap.get(compositeDimensionKey);
        if (values == null) {
            values = new Value[position + 1];
            dimensionMap.put(compositeDimensionKey, values);
        } if (position >= values.length) {
            Value[] copyTarget = new Value[position + 1];
            System.arraycopy(values, 0, copyTarget, 0, values.length);
            values = copyTarget;
            dimensionMap.put(compositeDimensionKey, values);
        }

        values[position] = value;
    }

    public DataSet aggregate(List<AnalysisItem> columns, List<AnalysisItem> derivedItems) {
        DataSet dataSet = new DataSet();
        for (Map<Key, Value> compositeKey : compositeKeys) {
            IRow row = dataSet.createRow();
            Value[] dimensions = dimensionMap.get(compositeKey);
            Value[] measures = keyMap.get(compositeKey);
            for (Map.Entry<AnalysisItem, Integer> entry : dimensionIndexMap.entrySet()) {
                if (entry.getValue() < dimensions.length) {
                    Value obj = dimensions[entry.getValue()];
                    if (obj == null) {
                        obj = new EmptyValue();
                    }
                    row.addValue(entry.getKey().createAggregateKey(), obj);
                }
            }
            for (Map.Entry<AnalysisItem, Integer> entry : measureIndexMap.entrySet()) {
                if (entry.getValue() < measures.length) {
                    Value obj = measures[entry.getValue()];
                    if (obj == null) {
                        obj = new EmptyValue();
                    } else if (obj instanceof Aggregation) {
                        Aggregation aggregation = (Aggregation) obj;
                        obj = aggregation.getValue();
                    }
                    row.addValue(entry.getKey().createAggregateKey(), obj);
                }
            }
        }

        if (derivedItems != null) {
            for (IRow row : dataSet.getRows()) {
                for (AnalysisItem analysisItem : derivedItems) {
                    if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
                        AnalysisHierarchyItem analysisHierarchy = (AnalysisHierarchyItem) analysisItem;
                        AggregateKey aggregateKey = analysisHierarchy.getHierarchyLevel().getAnalysisItem().createAggregateKey();
                        row.addValue(new AggregateKey(analysisHierarchy.getKey(), analysisHierarchy.getType(), analysisHierarchy.getFilters()), row.getValue(aggregateKey));
                    }
                }
            }
        }
        
        return dataSet;
    }    
}
