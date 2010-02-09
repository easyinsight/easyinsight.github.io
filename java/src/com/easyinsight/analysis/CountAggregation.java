package com.easyinsight.analysis;

import com.easyinsight.core.Value;
import com.easyinsight.core.NumericValue;

/**
 * User: James Boe
 * Date: Jan 21, 2008
 * Time: 9:13:38 PM
 */
public class CountAggregation extends Aggregation {

    private double count;

    public void addValue(Value value) {
        double addValue;
        /*if (value.type() == Value.NUMBER) {
            NumericValue numericValue = (NumericValue) value;
            addValue = numericValue.toDouble();
        } else {
            addValue = 1;
        }*/
        addValue = 1;
        count += addValue;
    }

    public Value getValue() {
        return new NumericValue(count);
    }
}
