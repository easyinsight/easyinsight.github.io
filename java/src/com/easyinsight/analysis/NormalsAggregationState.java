package com.easyinsight.analysis;

import com.easyinsight.core.Value;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.Aggregation;
import cern.jet.stat.Descriptive;
import cern.colt.list.DoubleArrayList;

import java.util.ArrayList;
import java.util.List;

/**
 * User: James Boe
 * Date: Oct 17, 2008
 * Time: 11:00:25 AM
 */
public class NormalsAggregationState implements IAggregationState {

    private List<NormalsAggregation> aggregationList = new ArrayList<NormalsAggregation>();
    private DoubleArrayList data = new DoubleArrayList();

    private Double standardDeviation;
    private double mean;

    public void addAggregation(Aggregation normalsAggregation) {
        aggregationList.add((NormalsAggregation) normalsAggregation);
    }

    private void computeStatistics() {
        if (standardDeviation == null) {
            for (NormalsAggregation normalsAggregation : aggregationList) {
                Value value = normalsAggregation.getWrappedValue();
                Double doubleValue = value.toDouble();
                if (doubleValue != null) {
                    data.add(doubleValue);
                }
            }
            mean = Descriptive.mean(data);
            double sumOfSquares = Descriptive.sumOfSquares(data);
            double sum = Descriptive.sum(data);
            double variance = Descriptive.variance(data.size(), sum, sumOfSquares);
            standardDeviation = Descriptive.standardDeviation(variance);
        }
    }

    public Value getNormals(Value value) {
        computeStatistics();
        Double doubleValue = value.toDouble();
        if (doubleValue == null) {
            return new EmptyValue();
        } else {
            return new NumericValue((doubleValue - mean) / standardDeviation);
        }
    }
}
