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
        count++;
    }

    public Value getValue() {
        return new NumericValue(count);
    }
}
