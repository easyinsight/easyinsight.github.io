package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 12/9/14
 * Time: 4:49 PM
 */
public class Contains extends Function {
    @Override
    public Value evaluate() {
        String string = params.get(0).toString().toLowerCase();
        for (int i = 1; i < params.size(); i++) {
            String match = minusQuotes(i);
            if (string.contains(match.toLowerCase())) {
                return new NumericValue(1);
            }
        }
        return new NumericValue(0);
    }

    @Override
    public int getParameterCount() {
        return -1;
    }
}
