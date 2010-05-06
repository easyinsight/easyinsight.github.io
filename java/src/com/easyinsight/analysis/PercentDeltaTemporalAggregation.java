package com.easyinsight.analysis;

import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisMeasure;

import java.util.HashMap;
import java.util.Map;

/**
 * User: James Boe
 * Date: Oct 17, 2008
 * Time: 12:14:11 PM
 */
public class PercentDeltaTemporalAggregation extends TemporalAggregation implements ITemporalAggregation {

    private Double previousValue;
    private Map<Value, Value> values = new HashMap<Value, Value>();

    public PercentDeltaTemporalAggregation(AnalysisDimension sortDate, AnalysisMeasure wrappedMeasure, int newAggregation, boolean requiresReAggregation) {
        super(sortDate, wrappedMeasure, newAggregation, requiresReAggregation);
    }

    public void addValue(Value value, Value dateValue) {
        if (previousValue == null) {
            previousValue = value.toDouble();
        } else {
            Double newValue = value.toDouble();
            if (newValue != null) {
                Double delta = newValue - previousValue;
                Double changed = delta / previousValue * 100;
                values.put(dateValue, new NumericValue(changed));
                previousValue = newValue;
            }
        }
    }

    public Value getValue(Value i) {
        return values.get(i);
    }
}