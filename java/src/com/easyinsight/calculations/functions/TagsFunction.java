package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.*;

/**
 * User: jamesboe
 * Date: 9/22/11
 * Time: 2:59 PM
 */
public class TagsFunction extends Function {
    public Value evaluate() {
        Value tagValue = params.get(0);
        Value originalTagValue = tagValue.getOriginalValue();
        if (tagValue.getOriginalValue() == null) {
            originalTagValue = tagValue;
        }
        StringBuilder sb = new StringBuilder();
        if (originalTagValue.type() == Value.STRING) {
            StringValue stringValue = (StringValue) originalTagValue;
            String[] tokens = stringValue.getValue().split(",");

            for (int i = 0; i < tokens.length; i++) {
                String tag = tokens[i].trim();
                for (int j = 1; j < params.size(); j++) {
                    String param = minusQuotes(j);
                    if (tag.equals(param)) {
                        sb.append(param).append(",");
                    }
                }
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return new StringValue(sb.toString());
    }

    public int getParameterCount() {
        return -1;
    }
}
