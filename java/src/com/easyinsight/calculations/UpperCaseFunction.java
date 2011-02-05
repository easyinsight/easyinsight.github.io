package com.easyinsight.calculations;


import com.easyinsight.core.StringValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: Jul 12, 2010
 * Time: 8:26:18 PM
 */
public class UpperCaseFunction extends Function {
    public Value evaluate() {
        Value value = params.get(0);
        String substringValue = value.toString();
        return new StringValue(substringValue.toUpperCase());
    }

    public int getParameterCount() {
        return 1;
    }
}