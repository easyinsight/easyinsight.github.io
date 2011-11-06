package com.easyinsight.analysis;

import com.easyinsight.core.Value;
import com.easyinsight.core.NumericValue;

import java.util.HashSet;
import java.util.Set;

/**
 * User: James Boe
 * Date: Jan 21, 2008
 * Time: 9:13:38 PM
 */
public class CountAggregation extends Aggregation {

    private double count;

    private Set<Value> distinctSet = new HashSet<Value>();

    private boolean usingDistinctSet;

    private Double distinctValue;

    public void setCount(double count) {
        this.count = count;
    }

    public void addValue(Value value) {
        double addValue;
        if (value.type() != Value.EMPTY) {
            if (value.type() == Value.NUMBER) {
                NumericValue numericValue = (NumericValue) value;
                Aggregation aggregation = numericValue.getAggregation();
                if (aggregation == null) {
                    addValue = 1;
                } else if (aggregation instanceof CountAggregation) {
                    CountAggregation countAggregation = (CountAggregation) aggregation;
                    if (countAggregation.usingDistinctSet) {
                        usingDistinctSet = true;
                        distinctSet.addAll(countAggregation.distinctSet);
                        addValue = 1;
                    } else {
                        addValue = countAggregation.count;
                    }
                } else {
                    addValue = 1;
                }
            } else {
                usingDistinctSet = true;
                distinctSet.add(value);
                addValue = 1;
            }
            //addValue = 1;
            count += addValue;
        }
    }

    public Value getValue() {
        if (usingDistinctSet) {
            return new NumericValue((double) distinctSet.size(), this);
        } else {
            /*if (distinctValue != null) {
                return new NumericValue(distinctValue, this);
            } else {*/
                return new NumericValue(count, this);
            //}
        }
    }
}
