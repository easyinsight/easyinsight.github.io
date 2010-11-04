package com.easyinsight.calculations;

import com.easyinsight.analysis.FunctionExplanation;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.StringValue;
import com.easyinsight.core.Value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: jamesboe
 * Date: Jul 12, 2010
 * Time: 8:56:49 PM
 */
public class NamedBracketDateFunction extends Function {
    public Value evaluate() {
        Value stringValue = params.get(0);
        Value pair = params.get(1);
        Value format = params.get(3);
        String pairValue = pair.toString();
        pairValue = pairValue.substring(1, pairValue.length() - 1);
        String string = stringValue.toString();
        boolean found = false;
        String result = null;
        int index = 0;
        while (!found) {
            int startIndex = string.indexOf("[", index);
            int endIndex = string.indexOf("]", index);
            if (startIndex != -1 && endIndex != -1) {
                String substring = string.substring(startIndex + 1, endIndex);
                int colonIndex = substring.indexOf(":");
                if (colonIndex != -1) {
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

        if (result == null) {
            return new EmptyValue();
        }
        String formatString = format.toString();
        Value returnValue;
        try {
            Date date = new SimpleDateFormat(formatString).parse(result);
            returnValue = new DateValue(date);
        } catch (ParseException e) {
            returnValue = new EmptyValue();
        }
        return returnValue;
    }

    public int getParameterCount() {
        return 3;
    }

    public FunctionExplanation explain() {
        return new FunctionExplanation("namedbracketdate(String, String, String)", "Returns the named bracketed value found in String. For example, namedbracketvalue(Deal Name, \"Type\").");
    }
}