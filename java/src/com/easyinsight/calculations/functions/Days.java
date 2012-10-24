package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 1/27/12
 * Time: 9:40 AM
 */
public class Days extends Function {
    public Value evaluate() {
        long days = params.get(0).toDouble().longValue();
        long ms = days * (1000 * 60 * 60 * 24);
        return new NumericValue(ms);
    }

    public int getParameterCount() {
        return 1;
    }
}
