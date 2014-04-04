package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.*;

/**
 * User: jamesboe
 * Date: 12/14/11
 * Time: 1:54 PM
 */
public class TrimFunction extends Function {
    public Value evaluate() {
        String string = params.get(0).toString();
        return new StringValue(string.trim());
    }

    public int getParameterCount() {
        return 1;
    }
}
