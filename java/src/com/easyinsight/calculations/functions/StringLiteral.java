package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.*;

/**
 * User: jamesboe
 * Date: 1/17/12
 * Time: 9:18 AM
 */
public class StringLiteral extends Function {
    public Value evaluate() {
        return new StringValue(minusQuotes(0));
    }

    public int getParameterCount() {
        return 1;
    }
}
