package com.easyinsight.analysis;

import com.easyinsight.core.Value;
import com.easyinsight.core.NumericValue;

/**
 * User: James Boe
 * Date: Jan 20, 2008
 * Time: 10:01:16 PM
 */
public class MaxAggregation extends Aggregation {

    private double max = Double.MIN_VALUE;

    public void addValue(Value value) {
        Double valueObj = value.toDouble();
        if (valueObj != null) {
            max = Math.max(max, valueObj);
        }
    }

    public Value getValue() {
        return new NumericValue(max);
    }
}
