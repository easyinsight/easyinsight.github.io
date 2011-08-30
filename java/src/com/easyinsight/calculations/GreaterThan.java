package com.easyinsight.calculations;

import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 8/29/11
 * Time: 7:00 PM
 */
public class GreaterThan extends Function {
    public Value evaluate() {
        Value compare1 = params.get(0);
        Value compare2 = params.get(1);
        if (compare1.toDouble() > compare2.toDouble()) {
            return params.get(2);
        } else {
            return params.get(3);
        }
    }

    public int getParameterCount() {
        return 4;
    }
}
