package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AggregationTypes;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.Value;

/**
 * User: abaldwin
 * Date: Nov 24, 2009
 * Time: 11:18:52 AM
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
