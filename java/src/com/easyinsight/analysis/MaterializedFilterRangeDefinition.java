package com.easyinsight.analysis;

import com.easyinsight.analysis.MaterializedFilterDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.Value;

/**
 * User: James Boe
 * Date: Jul 8, 2008
 * Time: 2:59:17 PM
 */
public class MaterializedFilterRangeDefinition extends MaterializedFilterDefinition {

    private Double lowValue;
    private Double highValue;

    public MaterializedFilterRangeDefinition(AnalysisItem key, Double lowValue, Double highValue) {
        super(key);
        this.lowValue = lowValue;
        this.highValue = highValue;
    }

    public boolean allows(Value value) {
        boolean allowed = false;
        if (value.type() == Value.NUMBER) {
            Double doubleValue = value.toDouble();
            if (doubleValue != null) {
                allowed = (lowValue == null || doubleValue > lowValue) && (highValue == null || doubleValue < highValue);
            }
        }
        return allowed;
    }
}
