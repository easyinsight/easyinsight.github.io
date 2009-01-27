package com.easyinsight.analysis;

import com.easyinsight.core.Value;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.AnalysisDateDimension;
import com.easyinsight.AnalysisMeasure;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * User: James Boe
 * Date: Oct 17, 2008
 * Time: 12:14:11 PM
 */
public class DeltaTemporalAggregation extends TemporalAggregation implements ITemporalAggregation {

    private Double previousValue;
    private Map<Integer, Value> values = new HashMap<Integer, Value>();

    public DeltaTemporalAggregation(AnalysisDateDimension sortDate, AnalysisMeasure wrappedMeasure, int newAggregation) {
        super(sortDate, wrappedMeasure, newAggregation);
    }

    public void addValue(Value value, int position) {
        if (previousValue == null) {
            previousValue = value.toDouble();
            values.put(position, new EmptyValue());
        } else {
            Double newValue = value.toDouble();
            if (newValue != null) {
                Double delta = newValue - previousValue;
                values.put(position, new NumericValue(delta));
                previousValue = newValue;
            }
        }
    }

    public Value getValue(int i) {
        return values.get(i);
    }
}
