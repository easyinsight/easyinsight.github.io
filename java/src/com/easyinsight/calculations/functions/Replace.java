package com.easyinsight.calculations.functions;


import com.easyinsight.calculations.Function;
import com.easyinsight.calculations.FunctionException;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: Nov 5, 2010
 * Time: 2:16:12 PM
 */
public class Replace extends Function {
    public Value evaluate() {
        if (params.size() == 0) {
            throw new FunctionException("replace(Field, Regular Expression, String) cannot be applied to ()");
        } else if (params.size() > 3) {
            throw new FunctionException("replace(Field, Regular Expression, String) cannot be applied to these parameters");
        }
        Value field = params.get(0);
        String sourcePattern = minusQuotes(1);
        String targetPattern = minusQuotes(2);
        String string = field.toString();
        String result = string.replaceAll(sourcePattern, targetPattern);
        return new com.easyinsight.core.StringValue(result);
    }

    public int getParameterCount() {
        return 3;
    }
}
