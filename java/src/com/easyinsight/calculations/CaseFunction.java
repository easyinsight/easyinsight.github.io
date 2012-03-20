package com.easyinsight.calculations;

import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 8/29/11
 * Time: 4:35 PM
 */
public class CaseFunction extends Function {
    public Value evaluate() {
        Value value = getParameter(0);
        for (int i = 1; i < (paramCount() - 1); i += 2) {
            Value paramValue = minusQuotes(getParameter(i));
            if (value.equals(paramValue)) {
                if ((i + 1) < (paramCount() - 1)) {
                    return minusQuotes(getParameter(i + 1));
                }
            }
        }
        return minusQuotes(getParameter(paramCount() - 1));
    }

    @Override
    public boolean onDemand() {
        return true;
    }

    public int getParameterCount() {
        return -1;
    }
}
