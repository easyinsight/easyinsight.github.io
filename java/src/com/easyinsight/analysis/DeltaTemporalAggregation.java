package com.easyinsight.analysis;

import com.easyinsight.core.Value;
import com.easyinsight.core.NumericValue;

import java.util.HashMap;
import java.util.Map;

/**
 * User: James Boe
 * Date: Oct 17, 2008
 * Time: 12:14:11 PM
 */
public class DeltaTemporalAggregation extends TemporalAggregation implements ITemporalAggregation {

    private Double previousValue;
    private Map<Value, Value> values = new HashMap<Value, Value>();

    public DeltaTemporalAggregation(AnalysisDimension sortDate, AnalysisMeasure wrappedMeasure, int newAggregation, boolean requiresReAggregation) {
        super(sortDate, wrappedMeasure, newAggregation, requiresReAggregation);
    }

    public void addValue(Value value, Value dateValue) {
        if (previousValue == null) {
            previousValue = value.toDouble();
        } else {
            Double newValue = value.toDouble();
            if (newValue != null) {
                Double delta = newValue - previousValue;
                //System.out.println("compared " + newValue + " to " + previousValue);
                values.put(dateValue, new NumericValue(delta));
                previousValue = newValue;
            }
        }
    }

    public Value getValue(Value dateValue) {
        return values.get(dateValue);
    }
}
