package com.easyinsight.analysis;

import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.*;

/**
 * User: James Boe
 * Date: Jan 9, 2008
 * Time: 10:35:37 PM
 */
public class TemporalTransform {

    private Map<Map<Key, Value>, Map<Key, Value>> dimensionMap = new HashMap<Map<Key, Value>, Map<Key, Value>>();
    private Set<Map<Key, Value>> compositeKeys = new LinkedHashSet<Map<Key, Value>>();

    private Map<Map<Key, Value>, ITemporalAggregation> map = new HashMap<Map<Key, Value>, ITemporalAggregation>();

    private Set<Value> timeValues = new LinkedHashSet<Value>();

    private TemporalAnalysisMeasure measure;

    public TemporalTransform(TemporalAnalysisMeasure measure) {
        this.measure = measure;
    }

    public void temporalMeasure(Map<Key, Value> compositeDimensionKey, Value temporalAggregationDate, Value value) {
        compositeKeys.add(compositeDimensionKey);

        ITemporalAggregation temporalAggregation = map.get(compositeDimensionKey);
        if (temporalAggregation == null) {
            temporalAggregation = measure.createAggregation();
            map.put(compositeDimensionKey, temporalAggregation);
        }
        timeValues.add(temporalAggregationDate);
        temporalAggregation.addValue(value, temporalAggregationDate);
    }

    public void groupData(Map<Key, Value> compositeDimensionKey, Key key, Value value) {
        compositeKeys.add(compositeDimensionKey);
        Map<Key, Value> values = dimensionMap.get(compositeDimensionKey);
        if (values == null) {
            values = new HashMap<Key, Value>();
            dimensionMap.put(compositeDimensionKey, values);
        }
        values.put(key, value);
    }

    public DataSet aggregate() {
        DataSet dataSet = new DataSet();
        for (Map<Key, Value> compositeKey : compositeKeys) {
            IRow row = dataSet.createRow();
            Map<Key, Value> fields = dimensionMap.get(compositeKey);
            ITemporalAggregation temporalAggregation = map.get(compositeKey);
            for (Value value : timeValues) {
                Value temporalValue = temporalAggregation.getValue(value);
                if (temporalValue != null) {
                    row.removeValue(measure.getAggregateKey());
                    row.addValue(measure.createAggregateKey(), temporalValue);
                    row.addValue(measure.getAnalysisDimension().createAggregateKey(), value);
                    for (Map.Entry<Key, Value> entry : fields.entrySet()) {
                        row.addValue(entry.getKey(), entry.getValue());
                    }
                }
            }
        }

        return dataSet;
    }
}