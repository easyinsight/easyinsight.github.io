package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 8/29/11
 * Time: 7:00 PM
 */
public class EqualTo extends Function {

    public Value evaluate() {
        Value compare1 = minusQuotes(getParameter(0));
        Value compare2 = minusQuotes(getParameter(1));
        if (compare1.toString().equals(compare2.toString())) {
            return minusQuotes(getParameter(2));
        } else {
            return minusQuotes(getParameter(3));
        }
    }

    @Override
    public boolean onDemand() {
        return true;
    }

    public int getParameterCount() {
        return -1;
    }
}
