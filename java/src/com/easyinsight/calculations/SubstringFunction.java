package com.easyinsight.calculations;


import com.easyinsight.core.StringValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: Jul 12, 2010
 * Time: 8:26:18 PM
 */
public class SubstringFunction extends Function {
    public Value evaluate() {
        Value value = params.get(0);
        String substringValue = value.toString();
        /*if (substringValue.length() > 2 && substringValue.charAt(1) != '0') {
            substringValue = substringValue.trim();
        }*/
        int startIndex = (int) ((Value) params.get(1)).toDouble().doubleValue();
        int endIndex = (int) ((Value) params.get(2)).toDouble().doubleValue();
        String string;
        try {
            string = substringValue.substring(startIndex, endIndex);
        } catch (StringIndexOutOfBoundsException e) {
            string = "";
        }
        return new StringValue(string);
    }

    public int getParameterCount() {
        return 3;
    }
}
