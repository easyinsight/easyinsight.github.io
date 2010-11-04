package com.easyinsight.calculations;

import com.easyinsight.analysis.FunctionExplanation;
import com.easyinsight.core.Value;
import com.easyinsight.core.NumericValue;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Sep 24, 2009
 * Time: 11:00:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class Maximum extends Function {
    public Value evaluate() {
        Double max = null;
        for(Value v : params) {
            if(max == null || v.toDouble() > max) {
                max = v.toDouble();
            }
        }
        return new NumericValue(max);
    }

    public int getParameterCount() {
        return -1;
    }

    public FunctionExplanation explain() {
        return new FunctionExplanation("", "");
    }
}
