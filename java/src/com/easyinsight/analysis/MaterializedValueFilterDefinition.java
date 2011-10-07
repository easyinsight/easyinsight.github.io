package com.easyinsight.analysis;

import com.easyinsight.core.Value;
import com.easyinsight.core.StringValue;

import java.util.Set;

/**
 * User: James Boe
 * Date: Jul 8, 2008
 * Time: 3:02:48 PM
 */
public class MaterializedValueFilterDefinition extends MaterializedFilterDefinition {
    private Set<Value> values;
    private boolean inclusive;
    private boolean all = false;

    public MaterializedValueFilterDefinition(AnalysisItem key, Set<Value> possibleValues, boolean inclusive) {
        super(key);
        this.inclusive = inclusive;
        values = possibleValues;
        if (values.size() == 1) {
            Value value = values.iterator().next();
            if (value.toString().equals("All")) {
                all = true;
            }
        }
    }

    public boolean isInclusive() {
        return inclusive;
    }

    public boolean allows(Value value) {
        boolean allows;
        if (all) {
            return true;
        }
        if (getKey().hasType(AnalysisItemTypes.LISTING)) {
            AnalysisList analysisList = (AnalysisList) getKey();
            /*Value start = value.getOriginalValue();
            if (start == null) {
                start = value;
            }*/

            Value[] values = analysisList.transformToMultiple(value);
            for (Value splitValue : values) {
                if (this.values.contains(splitValue)) {
                    return true;
                }
            }
            return false;
        } else {
            allows = values.contains(value);
            if (!allows) {
                if (value.type() == Value.EMPTY) {
                    StringValue emptyValue = new StringValue("");
                    allows = values.contains(emptyValue);
                }
            }
            if (!inclusive) allows = !allows;
            return allows;
        }
    }
}
