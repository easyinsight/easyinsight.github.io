package com.easyinsight.calculations;

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

    public CastFunction(int aggregationType) {
        this.aggregationType = aggregationType;
    }

    public Value evaluate() {
        return params.get(0);
    }

    public int getAggregationType() {
        return aggregationType;
    }

    public void setAggregationType(int aggregationType) {
        this.aggregationType = aggregationType;
    }

}
