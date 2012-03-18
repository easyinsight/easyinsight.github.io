package com.easyinsight.analysis;

import com.easyinsight.analysis.AggregationTypes;
import com.easyinsight.core.Value;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * User: James Boe
 * Date: Jan 20, 2008
 * Time: 9:56:09 PM
 */
public abstract class Aggregation extends Value {
    
    public Set<Value> keyDimensions = new HashSet<Value>();
    
    public abstract void addValue(Value value);

    public abstract Value getValue();

    public static Aggregation valueOf(int value) {
        Aggregation aggregation;
        switch(value) {
            case AggregationTypes.SUM:
                aggregation = new SumAggregation();
                break;
            case AggregationTypes.AVERAGE:
                aggregation = new AverageAggregation();
                break;
            case AggregationTypes.MAX:
                aggregation = new MaxAggregation();
                break;
            case AggregationTypes.MIN:
                aggregation = new MinAggregation();
                break;
            case AggregationTypes.COUNT:
                aggregation = new CountAggregation();
                break;
            default:
                throw new RuntimeException("unknown value " + value);
        }
        return aggregation;
    }

    public String toString() {
        return String.valueOf(getValue());
    }

    public int type() {
        return Value.AGGREGATION;
    }

    @Nullable
    public Double toDouble() {
        return getValue().toDouble();
    }

    public int compareTo(Value value) {
        return 0;
    }
}
