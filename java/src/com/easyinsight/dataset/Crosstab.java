package com.easyinsight.dataset;

import com.easyinsight.AnalysisMeasure;
import com.easyinsight.Aggregation;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;

import java.util.*;

/**
 * User: jboe
* Date: Dec 22, 2007
* Time: 10:35:10 AM
*/
public class Crosstab {

    private Map<DataKey, Map<AnalysisMeasure, Collection<Value>>> resultValues = new HashMap<DataKey, Map<AnalysisMeasure, Collection<Value>>>();

    public void addData(DataKey dataKey, AnalysisMeasure measure, Value measureValue) {
        Map<AnalysisMeasure, Collection<Value>> cell = resultValues.get(dataKey);
        if (cell == null) {
            cell = new HashMap<AnalysisMeasure, Collection<Value>>();
            resultValues.put(dataKey, cell);
        }
        Collection<Value> values = cell.get(measure);
        if (values == null) {
            values = new ArrayList<Value>();
            cell.put(measure, values);
        }
        values.add(measureValue);
    }

    public Collection<Map<String, Value>> outputForm() {
        Collection<Map<String, Value>> objects = new ArrayList<Map<String, Value>>();
        for (Map.Entry<DataKey, Map<AnalysisMeasure, Collection<Value>>> entry : resultValues.entrySet()) {
            Map<String, Value> cell = new HashMap<String, Value>();

            DataKey dataKey = entry.getKey();

            for (Map.Entry<Key, Value> keyEntry : dataKey.getRowKey().entrySet()) {
                cell.put(keyEntry.getKey().toKeyString(), keyEntry.getValue());
            }
            //cell.putAll(dataKey.getRowKey());
            for (Map.Entry<Key, Value> keyEntry : dataKey.getColumnKey().entrySet()) {
                cell.put(keyEntry.getKey().toKeyString(), keyEntry.getValue());
            }
            //cell.putAll(dataKey.getColumnKey());
            for (Map.Entry<AnalysisMeasure, Collection<Value>> measureValues : entry.getValue().entrySet()) {
                AnalysisMeasure analysisMeasure = measureValues.getKey();
                Aggregation aggregation = Aggregation.valueOf(analysisMeasure.getAggregation());
                for (Value value : measureValues.getValue()) {
                    aggregation.addValue(value);
                }
                //cell.put(analysisMeasure.getKey(), aggregation.getValue());
                cell.put(analysisMeasure.getKey().toKeyString(), aggregation.getValue());
            }
            objects.add(cell);
        }
        return objects;
    }    
}
