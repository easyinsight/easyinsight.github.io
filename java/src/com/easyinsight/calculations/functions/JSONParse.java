package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.*;
import com.jayway.jsonpath.JsonPath;

import java.util.List;

/**
 * User: jamesboe
 * Date: 2/3/14
 * Time: 2:28 PM
 */
public class JSONParse extends Function {

    public Value evaluate() {
        String jsonString = params.get(0).toString();
        String pattern = minusQuotes(1);
        if (jsonString.trim().length() == 0) {
            return new EmptyValue();
        }
        try {
            int base = jsonString.indexOf("{");
            if (base > -1) {
                String string = jsonString.substring(base).trim();
                Object obj = JsonPath.read(string, pattern);
                if (obj instanceof List) {
                    List coreArray = (List) obj;
                    if (coreArray.size() > 0) {
                        return new StringValue(coreArray.get(0).toString());
                    }
                } else if (obj instanceof String) {
                    return new StringValue(obj.toString());
                }
            }
            return new EmptyValue();
        } catch (Exception e) {
            return new EmptyValue();
        }
    }

    public int getParameterCount() {
        return 2;
    }
}
