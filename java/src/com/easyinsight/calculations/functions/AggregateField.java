package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 11/20/11
 * Time: 5:22 PM
 */
public class AggregateField extends Function {

    public Value evaluate() {
        return params.get(0);
    }

    public int getParameterCount() {
        return 1;
    }
}
