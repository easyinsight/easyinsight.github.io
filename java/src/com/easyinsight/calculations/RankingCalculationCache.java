package com.easyinsight.calculations;

import com.easyinsight.analysis.*;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;
import org.apache.commons.math.stat.descriptive.rank.Max;
import org.apache.commons.math.stat.ranking.NaturalRanking;
import org.apache.commons.math.stat.ranking.TiesStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 9/11/12
 * Time: 2:16 PM
 */
public class RankingCalculationCache implements ICalculationCache {
    private AnalysisItem instanceID;

    private AnalysisMeasure metric;
    private boolean ascending;
    private List<AnalysisItem> additionals;
    private Map<List<Value>, Map<Value, Value>> rankMap;

    public RankingCalculationCache(AnalysisItem instanceID, AnalysisMeasure metric, boolean ascending, List<AnalysisItem> additionals) {
        this.instanceID = instanceID;
        this.metric = metric;
        this.additionals = additionals;
        this.ascending = ascending;
    }

    public void fromDataSet(DataSet dataSet) {
        NaturalRanking naturalRanking = new NaturalRanking(TiesStrategy.MINIMUM);

        Map<List<Value>, Map<Value, Value>> map = new HashMap<List<Value>, Map<Value, Value>>();

        for (IRow row : dataSet.getRows()) {

            // construct the key

            List<Value> keyList = new ArrayList<Value>(additionals.size());

            for (AnalysisItem additionalItem : additionals) {
                Value value = row.getValue(additionalItem);
                keyList.add(value);
            }

            Map<Value, Value> valueMap = map.get(keyList);

            if (valueMap == null) {
                valueMap = new HashMap<Value, Value>();
                map.put(keyList, valueMap);
            }

            Value metricValue = row.getValue(metric);
            Value dimensionValue = row.getValue(instanceID);

            valueMap.put(dimensionValue, metricValue);
        }

        this.rankMap = new HashMap<List<Value>, Map<Value, Value>>();

        for (Map.Entry<List<Value>, Map<Value, Value>> entry : map.entrySet()) {
            List<Value> values = entry.getKey();
            Map<Value, Value> rankMap = new HashMap<Value, Value>();
            this.rankMap.put(values, rankMap);
            double[] data = new double[entry.getValue().size()];
            Map<Integer, Value> positionMap = new HashMap<Integer, Value>();
            int i = 0;
            for (Map.Entry<Value, Value> rankEntry : entry.getValue().entrySet()) {
                Value result = rankEntry.getValue();
                data[i] = result.toDouble();
                positionMap.put(i, rankEntry.getKey());
                i++;
            }
            double[] ranks = naturalRanking.rank(data);
            if (!ascending) {
                double max = new Max().evaluate(ranks);
                double[] reversedRanks = new double[ranks.length];
                for (int j = 0; j < ranks.length; j++) {
                    reversedRanks[j] = max - ranks[j] + 1;
                }
                ranks = reversedRanks;
            }
            for (Map.Entry<Integer, Value> rankEntry : positionMap.entrySet()) {
                double rank = ranks[rankEntry.getKey()];
                if (ascending) {
                    rank = ranks.length - rank;
                }

                rankMap.put(rankEntry.getValue(), new NumericValue(rank));
            }
        }
    }

    public Value getRank(List<Value> keys, Value value) {
        return rankMap.get(keys).get(value);
    }

    /*public List<IRow> rowsForValue(Value value) {
        return rowMap.get(value);
    }*/
}
