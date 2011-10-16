package com.easyinsight.calculations;

import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 10/3/11
 * Time: 10:24 AM
 */
public class OriginalTags extends Function {
    public Value evaluate() {
        Value originalValue = params.get(0).getOriginalValue();
        if (originalValue == null) {
            return new EmptyValue();
        } else {
            return originalValue;
        }
    }

    public int getParameterCount() {
        return 1;
    }
}
