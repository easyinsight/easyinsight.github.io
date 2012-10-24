package com.easyinsight.calculations.functions;


import com.easyinsight.calculations.Function;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

/**
 * Created by IntelliJ IDEA.
 * User: Alan
 * Date: Jul 11, 2008
 * Time: 8:57:49 PM
 */
public class Round extends Function {

    public Value evaluate() {
        return new NumericValue(Math.round(params.get(0).toDouble()));
    }

    public int getParameterCount() {
        return 1;
    }
}