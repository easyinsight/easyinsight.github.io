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

    public void setCount(double count) {
        this.count = count;
    }

    public void addValue(Value value) {
        double addValue;
        if (value.type() != Value.EMPTY) {
            if (value.type() == Value.NUMBER) {
                NumericValue numericValue = (NumericValue) value;
                Aggregation aggregation = numericValue.getAggregation();
                if (aggregation == null) {
                    addValue = 1;
                } else if (aggregation instanceof CountAggregation) {
                    CountAggregation countAggregation = (CountAggregation) aggregation;
                    addValue = countAggregation.count;
                } else {
                    addValue = 1;
                }
            } else {
                addValue = 1;
            }
            //addValue = 1;
            count += addValue;
        }
    }

    public Value getValue() {
        return new NumericValue(count, this);
    }
}
