package com.easyinsight.analysis;

import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;
import org.apache.commons.math.stat.StatUtils;
import org.apache.commons.math.stat.descriptive.rank.Median;

import java.util.ArrayList;
import java.util.List;

/**
 * User: James Boe
 * Date: Jan 20, 2008
 * Time: 10:00:40 PM
 */
public class VarianceAggregation extends Aggregation {

    private double total;
    private List<Double> values = new ArrayList<Double>();
    private int points;

    public void addValue(Value value) {
        Double valueObj = value.toDouble();
        if (valueObj != null) {
            values.add(valueObj);
        }
    }

    public Value getValue() {
        if (values.size() > 0) {
            double[] valueArray = new double[values.size()];
            for (int i = 0; i < valueArray.length; i++) {
                valueArray[i] = values.get(i);
            }
            return new NumericValue(StatUtils.variance(valueArray));
        } else {
            return new NumericValue(0);
        }
    }
}