package com.easyinsight.analysis;

import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;
import com.easyinsight.core.NumericValue;

/**
 * User: James Boe
 * Date: Jan 20, 2008
 * Time: 10:00:40 PM
 */
public class AverageAggregation extends Aggregation {

    private double total;
    private int points;

    public void addValue(Value value) {
        if (value.type() != Value.EMPTY) {
            Double valueObj = value.toDouble();
            if (valueObj != null) {
                total += valueObj;
                points++;
            }
        }
    }

    public Value getValue() {
        if (points == 0) {
            return new EmptyValue();
        }
        return new NumericValue(total / points);
    }
}
