package com.easyinsight.calculations;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.*;

/**
 * User: jamesboe
 * Date: 9/11/12
 * Time: 2:16 PM
 */
public class CumulativeCalculationCache implements ICalculationCache {

    private AnalysisItem dateField;
    private AnalysisMeasure measureField;
    private int interval;

    private Map<Value, Value> map;
    private List<Value> keys;

    public CumulativeCalculationCache(AnalysisItem dateField, AnalysisMeasure measureField, int interval) {
        this.dateField = dateField;
        this.measureField = measureField;
        this.interval = interval;
    }

    public void fromDataSet(DataSet dataSet) {
        Map<Value, Value> map = new HashMap<Value, Value>();
        List<Value> keys = new ArrayList<Value>();
        for (IRow row : dataSet.getRows()) {
            Value val1 = row.getValue(dateField);
            if (val1.type() == Value.DATE) {
                DateValue dateValue = (DateValue) val1;
                keys.add(dateValue);
                // this is giving us our list of dates...
                Value val2 = row.getValue(measureField);
                map.put(val1, val2);
            }
        }
        Collections.sort(keys);
        this.map = map;
        this.keys = keys;
    }

    public Value forValue(Value value) {
        Aggregation aggregation = new AggregationFactory(measureField, false).getAggregation();
        int startIndex = keys.indexOf(value);
        for (int i = 0; i < interval; i++) {
            int position = startIndex - i;
            if (position < 0) {
                continue;
            }
            Value date = keys.get(position);
            aggregation.addValue(map.get(date));
        }
        return aggregation.getValue();
    }
}
