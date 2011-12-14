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
        Value indexValue = params.get(1);
        
        String string;
        if (indexValue.type() == Value.NUMBER) {
            int startIndex = (int) (indexValue).toDouble().doubleValue();
            if (params.size() == 2) {
                try {
                    string = substringValue.substring(startIndex);
                } catch (StringIndexOutOfBoundsException e) {
                    string = "";
                }
            } else {
                int endIndex = (int) (params.get(2)).toDouble().doubleValue();
                try {
                    string = substringValue.substring(startIndex, endIndex);
                } catch (StringIndexOutOfBoundsException e) {
                    string = "";
                }
            }
        } else {
            string = "";
        }

        return new StringValue(string);
    }

    public int getParameterCount() {
        return -1;
    }
}
