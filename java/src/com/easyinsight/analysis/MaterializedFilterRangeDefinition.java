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
    private int upperOperator;
    private int lowerOperator;

    public MaterializedFilterRangeDefinition(AnalysisItem key, Double lowValue, Double highValue, int lowerOperator, int upperOperator) {
        super(key);
        this.lowValue = lowValue;
        this.highValue = highValue;
        this.lowerOperator = lowerOperator;
        this.upperOperator = upperOperator;
    }

    public boolean allows(Value value) {
        boolean allowed = false;
        if (value.type() == Value.NUMBER) {
            Double doubleValue = value.toDouble();
            if (doubleValue != null) {
                boolean lowerRange, upperRange;
                if(lowValue != null) {
                    if(lowerOperator == FilterRangeDefinition.LESS_THAN) {
                        lowerRange = doubleValue > lowValue;
                    } else {
                        lowerRange = doubleValue >= lowValue;
                    }
                } else {
                    lowerRange = true;
                }
                if(highValue != null) {
                    if(upperOperator == FilterRangeDefinition.LESS_THAN) {
                        upperRange = doubleValue < highValue;
                    } else {
                        upperRange = doubleValue <= highValue;
                    }
                } else {
                    upperRange = true;
                }
                allowed = upperRange && lowerRange;
            }
        }
        return allowed;
    }
}
