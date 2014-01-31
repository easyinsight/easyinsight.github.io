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

    private Map<String, Aggregation[]> keyMap = new HashMap<String, Aggregation[]>();
    private Map<String, Value[]> dimensionMap = new HashMap<String, Value[]>();

    private Set<String> compositeKeys;

    private Map<AnalysisMeasure, AggregationFactory> factoryMap = new HashMap<AnalysisMeasure, AggregationFactory>();

    private Map<AnalysisItem, Integer> dimensionIndexMap = new HashMap<AnalysisItem, Integer>();
    private Map<AnalysisItem, Integer> measureIndexMap = new HashMap<AnalysisItem, Integer>();

    private Map<String, List<Map<String, Set<Value>>>> passThroughMap = new HashMap<String, List<Map<String, Set<Value>>>>();

    private int measureIndexSize;
    private int dimensionIndexSize;

    private Set<Integer> skipAggregations;

    public ListTransform(Set<Integer> skipAggregations) {
        this.skipAggregations = skipAggregations;
    }

    public void add(String key, Map<String, Set<Value>> map) {
        List<Map<String, Set<Value>>> list = passThroughMap.get(key);
        if (list == null) {
            list = new ArrayList<Map<String, Set<Value>>>();
            passThroughMap.put(key, list);
        }
        list.add(map);
    }

    public void addCompositeKeys(Collection<String> keys) {
        compositeKeys = new HashSet<String>(keys);
    }

    public void addColumns(Collection<AnalysisItem> paredDownColumns) {
        for (AnalysisItem column : paredDownColumns) {
            if (column.hasType(AnalysisItemTypes.MEASURE)) {
                measureIndexMap.put(column, measureIndexMap.size());
            } else {
                dimensionIndexMap.put(column, dimensionIndexMap.size());
            }
        }
        dimensionIndexSize = dimensionIndexMap.size();
        measureIndexSize = measureIndexMap.size();
    }

    public void groupData(String compositeDimensionKey, AnalysisMeasure measure, Value value, Value keyValue) {

        Integer position = measureIndexMap.get(measure);
        Aggregation[] values = keyMap.get(compositeDimensionKey);

        if (values == null) {
            values = new Aggregation[measureIndexSize];
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
        if (keyValue == null || !aggregation.keyDimensions.contains(keyValue)) {
            if (keyValue != null) {
                aggregation.keyDimensions.add(keyValue);
            }
            aggregation.addValue(value);
            if (value.getDrillThroughs() != null && value.getDrillThroughs().size() > 0) {
                aggregation.addDrillThroughs(value.getDrillThroughs());
            }
        }
    }

    public void groupData(String compositeDimensionKey, AnalysisDimension dimension, Value value) {

        Integer position = dimensionIndexMap.get(dimension);

        Value[] values = dimensionMap.get(compositeDimensionKey);
        if (values == null) {
            values = new Value[dimensionIndexSize];
            dimensionMap.put(compositeDimensionKey, values);
        }

        values[position] = value;
    }

    public DataSet aggregate(List<AnalysisItem> derivedItems) {
        DataSet dataSet = new DataSet();
        for (String compositeKey : compositeKeys) {
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
                        if (aggregation.getDrills() != null) {
                            List<Map<String, List<Value>>> drills = aggregation.getDrills();
                            Map<String, List<Value>> target;
                            target = new HashMap<String, List<Value>>();
                            for (Map<String, List<Value>> d : drills) {
                                for (Map.Entry<String, List<Value>> drillEntry : d.entrySet()) {
                                    List<Value> values = target.get(drillEntry.getKey());
                                    if (values == null) {
                                        values = new ArrayList<Value>();
                                        target.put(drillEntry.getKey(), values);
                                    }
                                    values.addAll(drillEntry.getValue());
                                }
                            }
                            obj.setDrillThroughs(target);
                        }
                    }
                    row.addValue(entry.getKey().createAggregateKey(), obj);
                }
            }
            List<Map<String, Set<Value>>> list = passThroughMap.get(compositeKey);
            if (list != null) {
                Map<String, Set<Value>> end = new HashMap<String, Set<Value>>();
                for (Map<String, Set<Value>> map : list) {
                    for (Map.Entry<String, Set<Value>> entry : map.entrySet()) {
                        Set<Value> set = end.get(entry.getKey());
                        if (set == null) {
                            set = new HashSet<Value>();
                            end.put(entry.getKey(), set);
                        }
                        set.addAll(entry.getValue());
                    }
                }
                row.setPassthroughRow(end);
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
