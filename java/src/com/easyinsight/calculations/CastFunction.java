package com.easyinsight.calculations;

import com.easyinsight.analysis.AggregationTypes;
import com.easyinsight.analysis.FunctionExplanation;
import com.easyinsight.core.Value;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Nov 24, 2009
 * Time: 11:18:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class CastFunction extends Function {

    private int aggregationType;
    private String name;

    public CastFunction(int aggregationType, String name) {
        this.aggregationType = aggregationType;
        this.name = name;
    }

    public Value evaluate() {
        return params.get(0);
    }

    public FunctionExplanation explain() {
        String description;
        switch (aggregationType) {
            case AggregationTypes.SUM:
                description = "Forces Number to be calculated as a Sum.";
                break;
            case AggregationTypes.AVERAGE:
                description = "Forces Number to be calculated as an Average.";
                break;
            case AggregationTypes.MAX:
                description = "Forces Number to be calculated as a Max.";
                break;
            case AggregationTypes.MIN:
                description = "Forces Number to be calculated as a Min.";
                break;
            case AggregationTypes.COUNT:
                description = "Forces Number to be calculated as a Count.";
                break;
            case AggregationTypes.VARIANCE:
                description = "Forces Number to be calculated as Variance.";
                break;
            case AggregationTypes.MEDIAN:
                description = "Forces Number to be calculated as Median.";
                break;
            default:
                throw new RuntimeException();
        }
        return new FunctionExplanation(name + "(Number)", description);
    }

    public int getParameterCount() {
        return 1;
    }

    public int getAggregationType() {
        return aggregationType;
    }

    public void setAggregationType(int aggregationType) {
        this.aggregationType = aggregationType;
    }

}
