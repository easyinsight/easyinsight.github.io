package com.easyinsight.analysis;

import com.easyinsight.analysis.Aggregation;
import com.easyinsight.core.Value;

/**
 * User: James Boe
 * Date: Oct 16, 2008
 * Time: 12:10:17 PM
 */
public class NormalsAggregation extends Aggregation {

    private NormalsAggregationState aggregationState;
    private Aggregation wrappedAggregation;

    public NormalsAggregation(IAggregationState aggregationState, Aggregation wrappedAggregation) {
        this.aggregationState = (NormalsAggregationState) aggregationState;
        this.wrappedAggregation = wrappedAggregation;
    }

    public void addValue(Value value) {
        wrappedAggregation.addValue(value);
    }

    public Value getWrappedValue() {
        return wrappedAggregation.getValue();
    }

    public Value getValue() {
        return aggregationState.getNormals(getWrappedValue());
    }
}
