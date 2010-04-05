package com.easyinsight.etl;

import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: Apr 4, 2010
 * Time: 3:26:29 PM
 */
public class LookupPair {
    private Value sourceValue;
    private Value targetValue;

    public Value getSourceValue() {
        return sourceValue;
    }

    public void setSourceValue(Value sourceValue) {
        this.sourceValue = sourceValue;
    }

    public Value getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Value targetValue) {
        this.targetValue = targetValue;
    }
}
