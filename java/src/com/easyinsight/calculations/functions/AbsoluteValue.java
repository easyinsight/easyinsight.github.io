package com.easyinsight.calculations.functions;


import com.easyinsight.calculations.Function;
import com.easyinsight.core.Value;
import com.easyinsight.core.NumericValue;

/**
 * User: abaldwin
 * Date: Sep 24, 2009
 * Time: 10:55:45 PM
 */
public class AbsoluteValue extends Function {
    public Value evaluate() {
        return new NumericValue(Math.abs(this.params.get(0).toDouble()));
    }

    public int getParameterCount() {
        return 1;
    }
}
