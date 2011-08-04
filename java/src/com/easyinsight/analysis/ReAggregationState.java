package com.easyinsight.analysis;

import cern.colt.list.DoubleArrayList;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;
import org.apache.commons.math.stat.descriptive.rank.Max;
import org.apache.commons.math.stat.ranking.NaturalRanking;
import org.apache.commons.math.stat.ranking.TiesStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: May 9, 2010
 * Time: 12:58:01 PM
 */
public class ReAggregationState implements IAggregationState {
    private List<Aggregation> aggregationList = new ArrayList<Aggregation>();
    private AnalysisMeasure measure;

    private Value value;

    public ReAggregationState(AnalysisMeasure measure) {
        this.measure = measure;
    }

    public void addAggregation(Aggregation normalsAggregation) {
        aggregationList.add( normalsAggregation);
    }

    private void computeStatistics() {
        if (value == null) {
            AggregationFactory factory = new AggregationFactory(measure, false);
            for (Aggregation aggregation : aggregationList) {
                factory.getAggregation().addValue(aggregation.getValue());
            }
            value = factory.getAggregation().getValue();
        }
    }

    public Value getValue(Value value) {
        computeStatistics();
        return value;
    }
}
