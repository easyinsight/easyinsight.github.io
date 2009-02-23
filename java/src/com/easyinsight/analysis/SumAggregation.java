package com.easyinsight.analysis;

import com.easyinsight.core.Value;
import com.easyinsight.core.NumericValue;

/**
 * User: James Boe
 * Date: Jan 20, 2008
 * Time: 10:00:11 PM
 */
public class SumAggregation extends Aggregation {

    private double sum;

    public void addValue(Value value) {
        Double addValue = value.toDouble();
        if (addValue != null) {
            sum += addValue;
        }
    }

    public Value getValue() {
        return new NumericValue(sum);
    }
}
