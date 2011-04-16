package com.easyinsight.analysis;

import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;
import org.apache.commons.math.stat.ranking.NaturalRanking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: May 9, 2010
 * Time: 12:58:01 PM
 */
public class PercentOfTotalAggregationState implements IAggregationState {
    private List<PercentOfValueAggregation> aggregationList = new ArrayList<PercentOfValueAggregation>();

    public void addAggregation(Aggregation normalsAggregation) {
        aggregationList.add((PercentOfValueAggregation) normalsAggregation);
    }

    private Map<Value, Double> rankMap;

    private void computeStatistics() {
        if (rankMap == null) {
            rankMap = new HashMap<Value, Double>();
            double sum = 0;
            for (PercentOfValueAggregation anAggregationList : aggregationList) {
                Value value = anAggregationList.getWrappedValue();
                Double doubleVal = value.toDouble();
                if (doubleVal == null) {
                    doubleVal = 0.;
                }
                sum += doubleVal;
            }
            for (PercentOfValueAggregation anAggregationList : aggregationList) {
                Value value = anAggregationList.getWrappedValue();
                Double doubleVal = value.toDouble();
                if (doubleVal == null) {
                    doubleVal = 0.;
                }
                double percent = doubleVal / sum * 100;
                rankMap.put(value, percent);
            }
        }
    }

    public Value getRank(Value value) {
        computeStatistics();
        Double rank = rankMap.get(value);
        if (rank == null) {
            return new EmptyValue();
        } else {
            return new NumericValue(rank);
        }
    }
}
