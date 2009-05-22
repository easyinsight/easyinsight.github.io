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

    public MaterializedValueFilterDefinition(AnalysisItem key, Set<Value> possibleValues, boolean inclusive) {
        super(key);
        this.inclusive = inclusive;
        values = possibleValues;
    }

    public boolean isInclusive() {
        return inclusive;
    }

    public boolean allows(Value value) {
        boolean allows;
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
