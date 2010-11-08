package com.easyinsight.calculations;

import com.easyinsight.analysis.FunctionExplanation;
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
        return new StringValue(string.substring(startIndex, endIndex));
    }

    public int getParameterCount() {
        return 1;
    }

    public FunctionExplanation explain() {
        return new FunctionExplanation("bracketvalue(String)", "Returns the first bracketed value found in String. For example, bracketvalue(Deal [John]) would produce a value of John.");
    }
}
