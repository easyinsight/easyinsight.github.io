package com.easyinsight.analysis;

import com.easyinsight.analysis.Aggregation;

/**
 * User: James Boe
 * Date: Oct 17, 2008
 * Time: 11:28:26 AM
 */
public class CorrelationAggregationState implements IAggregationState {
    public void addAggregation(Aggregation aggregation) {
        // really, we want...
        // normality, correlation
        // trending, last value
        // delta
        // % change
        // delta's a nice simple case
        // must select a date aggregation to go with any temporal aggregation
        // must aggregate values within that date by some mechanism
        // must receive those aggregated values in an ordered fashion
    }
}
