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

    private Map<Value, List<IRow>> rowMap = new HashMap<Value, List<IRow>>();
    private AnalysisMeasure metric;
    private Map<Value, Value> rankMap = new HashMap<Value, Value>();
    private boolean ascending;

    public RankingCalculationCache(AnalysisItem instanceID, AnalysisMeasure metric, boolean ascending) {
        this.instanceID = instanceID;
        this.metric = metric;
    }

    public void fromDataSet(DataSet dataSet) {
        NaturalRanking naturalRanking = new NaturalRanking(TiesStrategy.MINIMUM);
        for (IRow row : dataSet.getRows()) {
            Value instanceIDValue = row.getValue(instanceID);
            List<IRow> rows = rowMap.get(instanceIDValue);
            if (rows == null) {
                rows = new ArrayList<IRow>();
                rowMap.put(instanceIDValue, rows);
            }
            rows.add(row);
        }
        double[] data = new double[rowMap.entrySet().size()];
        Map<Integer, Value> map = new HashMap<Integer, Value>();
        int i = 0;
        for (Map.Entry<Value, List<IRow>> entry : rowMap.entrySet()) {
            List<IRow> rows = entry.getValue();
            Aggregation aggregation = new AggregationFactory(metric, false).getAggregation();
            for (IRow row : rows) {
                Value value = row.getValue(metric);
                aggregation.addValue(value);
            }
            Value result = aggregation.getValue();
            data[i] = result.toDouble();
            map.put(i, entry.getKey());
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
        for (Map.Entry<Integer, Value> entry : map.entrySet()) {
            double rank = ranks[entry.getKey()];
            if (ascending) {
                rank = ranks.length - rank;
            }
            rankMap.put(entry.getValue(), new NumericValue(rank));
        }
    }

    public Value getRank(Value value) {
        return rankMap.get(value);
    }

    public List<IRow> rowsForValue(Value value) {
        return rowMap.get(value);
    }
}
