package com.easyinsight.calculations.functions;


import com.easyinsight.calculations.Function;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: Jul 21, 2010
 * Time: 10:00:11 AM
 */
public class Now extends Function {
    public Value evaluate() {
        return new NumericValue(System.currentTimeMillis());
    }

    public int getParameterCount() {
        return 0;
    }
}
