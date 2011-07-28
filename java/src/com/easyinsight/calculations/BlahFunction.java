package com.easyinsight.calculations;

import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 7/18/11
 * Time: 11:12 AM
 */
public class BlahFunction extends Function {
    public Value evaluate() {
        return new com.easyinsight.core.StringValue("x");
    }

    public int getParameterCount() {
        return 1;
    }
}
