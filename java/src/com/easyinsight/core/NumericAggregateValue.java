package com.easyinsight.core;

import com.easyinsight.analysis.Aggregation;

/**
 * User: jamesboe
 * Date: Feb 9, 2010
 * Time: 1:45:09 PM
 */
public class NumericAggregateValue extends NumericValue {
    private Aggregation aggregation;

    public Aggregation getAggregation() {
        return aggregation;
    }

    public void setAggregation(Aggregation aggregation) {
        this.aggregation = aggregation;
    }
}
