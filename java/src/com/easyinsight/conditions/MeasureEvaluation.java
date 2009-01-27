package com.easyinsight.conditions;

import com.easyinsight.core.Value;

import java.util.List;

/**
 * User: James Boe
 * Date: Jul 2, 2008
 * Time: 3:05:39 PM
 */
public class MeasureEvaluation {
    public void evaluateMeasures(List<Value> values) {
        for (Value value : values) {
            ConditionRange conditionRange = findRange(value);
            conditionRange.getColor(value);
        }
    }

    private ConditionRange findRange(Value value) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    private static class ConditionRange {
        public String getColor(Value value) {
            return null;
        }
    }
}
