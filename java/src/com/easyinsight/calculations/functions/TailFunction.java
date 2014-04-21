package com.easyinsight.calculations.functions;


import com.easyinsight.calculations.Function;
import com.easyinsight.core.StringValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: Jul 12, 2010
 * Time: 8:26:18 PM
 */
public class TailFunction extends Function {
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
            int x = substringValue.length() - startIndex;
            if (x < 0) {
                x = 0;
            }
            return new StringValue(substringValue.substring(x));
        } else {
            string = "";
        }

        return new StringValue(string);
    }

    public int getParameterCount() {
        return 1;
    }
}
