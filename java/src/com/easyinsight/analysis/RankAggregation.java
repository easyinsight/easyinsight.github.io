package com.easyinsight.analysis;

import com.easyinsight.core.Value;

/**
 * User: James Boe
 * Date: Oct 16, 2008
 * Time: 12:10:17 PM
 */
public class RankAggregation extends Aggregation {

    private RankAggregationState aggregationState;
    private Aggregation wrappedAggregation;

    public RankAggregation(IAggregationState aggregationState, Aggregation wrappedAggregation) {
        this.aggregationState = (RankAggregationState) aggregationState;
        this.wrappedAggregation = wrappedAggregation;
    }

    public void addValue(Value value) {
        wrappedAggregation.addValue(value);
    }

    public Value getWrappedValue() {
        return wrappedAggregation.getValue();
    }

    public Value getValue() {
        return aggregationState.getRank(getWrappedValue());
    }
}