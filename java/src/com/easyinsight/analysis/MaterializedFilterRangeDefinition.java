package com.easyinsight.analysis;

import com.easyinsight.MaterializedFilterDefinition;
import com.easyinsight.AnalysisItem;
import com.easyinsight.core.Value;

/**
 * User: James Boe
 * Date: Jul 8, 2008
 * Time: 2:59:17 PM
 */
public class MaterializedFilterRangeDefinition extends MaterializedFilterDefinition {

    private double lowValue;
    private double highValue;

    public MaterializedFilterRangeDefinition(AnalysisItem key, Double lowValue, Double highValue) {
        super(key);
        this.lowValue = lowValue == null ? Double.MIN_VALUE : lowValue;
        this.highValue = highValue == null ? Double.MAX_VALUE : highValue;
    }

    public boolean allows(Value value, Value preTransformValue) {
        boolean allowed = false;
        if (value.type() == Value.NUMBER) {
            Double doubleValue = value.toDouble();
            if (doubleValue != null) {
                allowed = doubleValue >= lowValue && doubleValue <= highValue;
            }
        }
        return allowed;
    }
}
