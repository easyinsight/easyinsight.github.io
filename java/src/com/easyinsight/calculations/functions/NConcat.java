package com.easyinsight.calculations.functions;


import com.easyinsight.calculations.Function;
import com.easyinsight.core.Value;
import com.easyinsight.core.NumericValue;

/**
 * Created by IntelliJ IDEA.
 * User: Alan
 * Date: Jul 11, 2008
 * Time: 9:31:22 PM
 */
public class NConcat extends Function {
    public Value evaluate() {
        StringBuilder sb = new StringBuilder();
        for(Value v : params) {
            sb.append(v.toDouble().longValue());
        }
        return new NumericValue(Double.parseDouble(sb.toString()));
    }

    public int getParameterCount() {
        return -1;
    }
}
