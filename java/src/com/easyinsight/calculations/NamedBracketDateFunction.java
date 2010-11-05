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
        String pairValue = minusQuotes(1);
        String formatString = minusQuotes(2);        
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
        return new FunctionExplanation("namedbracketdate(Field, Name, Date Pattern)", "Returns the named bracketed value found in String. For example, namedbracketvalue(Deal Description, \"Target Close\", \"MM-dd-YYYY\") will construct a date out of a sample value of [Target Close: 12-15-2010] in the Deal Description field.");
    }
}