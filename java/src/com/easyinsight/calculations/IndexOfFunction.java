package com.easyinsight.calculations;

import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 12/14/11
 * Time: 1:54 PM
 */
public class IndexOfFunction extends Function {
    public Value evaluate() {
        String string = params.get(0).toString();
        String symbol = minusQuotes(1);
        return new NumericValue(string.indexOf(symbol));
    }

    public int getParameterCount() {
        return 2;
    }
}
