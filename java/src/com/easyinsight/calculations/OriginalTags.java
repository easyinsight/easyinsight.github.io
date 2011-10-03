package com.easyinsight.calculations;

import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 10/3/11
 * Time: 10:24 AM
 */
public class OriginalTags extends Function {
    public Value evaluate() {
        return params.get(0).getOriginalValue();
    }

    public int getParameterCount() {
        return 1;
    }
}
