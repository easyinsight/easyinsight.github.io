package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.*;

/**
 * User: jamesboe
 * Date: 2/18/14
 * Time: 3:56 PM
 */
public class TagBlah extends Function {
    public Value evaluate() {
        try {
            String string = params.get(0).toString();
            String token = minusQuotes(1);
            String component = minusQuotes(2);
            String[] parts = string.split(",");
            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];
                if (part.contains(token)) {
                    if (part.startsWith(component)) {
                        String end = part.substring(part.indexOf(token) + 1).trim();
                        return new StringValue(end);
                    }
                }
            }
            return new EmptyValue();
        } catch (Exception e) {
            e.printStackTrace();
            return new EmptyValue();
        }
    }

    public int getParameterCount() {
        return 3;
    }
}