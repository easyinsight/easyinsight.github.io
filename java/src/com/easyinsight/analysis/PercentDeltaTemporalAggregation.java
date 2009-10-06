package com.easyinsight.analysis;

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
    private Map<Integer, Value> values = new HashMap<Integer, Value>();

    public PercentDeltaTemporalAggregation(AnalysisDimension sortDate, AnalysisMeasure wrappedMeasure, int newAggregation, boolean requiresReAggregation) {
        super(sortDate, wrappedMeasure, newAggregation, requiresReAggregation);
    }

    public void addValue(Value value, int position) {
        if (previousValue == null) {
            previousValue = value.toDouble();
            values.put(position, new EmptyValue());
        } else {
            Double newValue = value.toDouble();
            if (newValue != null) {
                Double delta = newValue - previousValue;
                Double changed = delta / previousValue * 100;
                values.put(position, new NumericValue(changed));
                previousValue = newValue;
            }
        }
    }

    public Value getValue(int i) {
        return values.get(i);
    }
}