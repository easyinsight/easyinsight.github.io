package com.easyinsight.calculations;

import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 8/29/11
 * Time: 4:35 PM
 */
public class IfNotNull extends Function {
    public Value evaluate() {
        try {
            for (int i = 0; i < params.size(); i += 2) {
                Value v = params.get(i);
                if (v.type() != Value.EMPTY && !"".equals(v.toString().trim())) {
                    return minusQuotes(params.get(i + 1));
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new FunctionException("The notnull() function requires an even number of parameters.");
        }
        return EmptyValue.EMPTY_VALUE;
    }

    public int getParameterCount() {
        return -1;
    }
}
