package com.easyinsight.calculations.functions;


import com.easyinsight.calculations.Function;
import com.easyinsight.calculations.FunctionException;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.StringValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: Jul 12, 2010
 * Time: 8:56:49 PM
 */
public class BracketValueFunction extends Function {
    public Value evaluate() {
        if (params.size() == 0) {
            throw new FunctionException("bracketvalue(String) cannot be applied to ()");
        } else if (params.size() > 1) {
            throw new FunctionException("bracketvalue(String) cannot be applied to these parameters");
        }
        Value stringValue = params.get(0);
        String string = stringValue.toString();
        int startIndex = string.indexOf("[");
        int endIndex = string.indexOf("]");
        if (startIndex == -1 || endIndex == -1) {
            return new EmptyValue();
        }
        return new StringValue(string.substring(startIndex + 1, endIndex));
    }

    public int getParameterCount() {
        return 1;
    }
}
