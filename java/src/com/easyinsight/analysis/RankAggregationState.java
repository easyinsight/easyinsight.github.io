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
public class RankAggregationState implements IAggregationState {
    private List<RankAggregation> aggregationList = new ArrayList<RankAggregation>();
    private DoubleArrayList data = new DoubleArrayList();

    private Double standardDeviation;
    private double mean;

    public void addAggregation(Aggregation normalsAggregation) {
        aggregationList.add((RankAggregation) normalsAggregation);
    }

    private NaturalRanking naturalRanking;

    private Map<Value, Double> rankMap;

    private void computeStatistics() {
        if (naturalRanking == null) {
            rankMap = new HashMap<Value, Double>();
            naturalRanking = new NaturalRanking(TiesStrategy.MINIMUM);
            double[] values = new double[aggregationList.size()];
            for (int i = 0; i < aggregationList.size(); i++) {
                Value value = aggregationList.get(i).getWrappedValue();
                Double doubleVal = value.toDouble();
                if (doubleVal == null) {
                    doubleVal = 0.;
                }
                values[i] = doubleVal;
            }
            double[] ranks = naturalRanking.rank(values);
            double max = new Max().evaluate(ranks);
            double[] reversedRanks = new double[ranks.length];
            for (int i = 0; i < ranks.length; i++) {
                reversedRanks[i] = max - ranks[i] + 1;
            }
            for (int i = 0; i < aggregationList.size(); i++) {
                Value value = aggregationList.get(i).getWrappedValue();
                rankMap.put(value, reversedRanks[i]);
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
