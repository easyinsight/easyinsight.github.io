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
public class NamedBracketValueFunction extends Function {
    public Value evaluate() {
        if (params.size() < 2) {
            throw new FunctionException("namedbracketvalue() requires two parameters, such as namedbracketvalue(Deal Name, \"Type\").");
        }
        Value stringValue = params.get(0);
        String pairValue = minusQuotes(1);
        String string = stringValue.toString();
        boolean found = false;
        String result = null;
        int index = 0;
        try {
            while (!found) {
                int startIndex = string.indexOf("[", index);
                int endIndex = string.indexOf("]", index);
                if (startIndex > -1 && endIndex > -1 && startIndex < endIndex) {
                    String substring = string.substring(startIndex + 1, endIndex);
                    int colonIndex = substring.indexOf(":");
                    if (colonIndex > -1) {
                        String prefix = substring.substring(0, colonIndex).trim();
                        if (prefix.equals(pairValue)) {
                            result = substring.substring(colonIndex + 1).trim();
                            found = true;
                        }
                    }
                    index = endIndex + 1;
                } else {
                    found = true;
                }
            }
        } catch (StringIndexOutOfBoundsException e) {
            return new EmptyValue();
        }

        if (result == null) {
            return new EmptyValue();
        }
        return new StringValue(result);
    }

    public int getParameterCount() {
        return 2;
    }
}