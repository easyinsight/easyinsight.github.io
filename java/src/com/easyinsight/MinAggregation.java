package com.easyinsight;

import com.easyinsight.core.Value;
import com.easyinsight.core.NumericValue;

/**
 * User: James Boe
 * Date: Jan 20, 2008
 * Time: 10:01:16 PM
 */
public class MinAggregation extends Aggregation {

    private double min = Double.MAX_VALUE;

    public void addValue(Value value) {
        Double valueObj = value.toDouble();
        if (valueObj != null) {
            min = Math.min(min, valueObj);
        }
    }

    public Value getValue() {
        return new NumericValue(min);
    }
}