package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 4/2/14
 * Time: 1:46 PM
 */
public class Null extends Function {
    public Value evaluate() {
        return new EmptyValue();
    }

    public int getParameterCount() {
        return 0;
    }
}
