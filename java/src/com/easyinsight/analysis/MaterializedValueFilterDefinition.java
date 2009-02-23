package com.easyinsight.analysis;

import com.easyinsight.analysis.MaterializedFilterDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.core.Value;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.StringValue;

import java.util.Set;
import java.util.List;
import java.util.HashSet;

/**
 * User: James Boe
 * Date: Jul 8, 2008
 * Time: 3:02:48 PM
 */
public class MaterializedValueFilterDefinition extends MaterializedFilterDefinition {
    private Set<Value> values;
    private boolean inclusive;

    public MaterializedValueFilterDefinition(AnalysisItem key, List<String> possibleValues, boolean inclusive) {
        super(key);
        this.inclusive = inclusive;
        values = new HashSet<Value>();
        for (String value : possibleValues) {
            if (key.hasType(AnalysisItemTypes.MEASURE)) {
                NumericValue numericValue = new NumericValue();
                numericValue.setValue(value);
                values.add(numericValue);
            } else if (key.hasType(AnalysisItemTypes.DATE_DIMENSION)) {

            } else {
                if ("".equals(value)) {
                    values.add(new EmptyValue());
                } else {
                    StringValue stringValue = new StringValue();
                    stringValue.setValue(value);
                    values.add(stringValue);
                }
            }
        }
    }

    public boolean isInclusive() {
        return inclusive;
    }

    public boolean allows(Value value, Value preTransformValue) {
        boolean allows;
        allows = values.contains(value);
        if (!inclusive) allows = !allows;
        return allows;
    }
}
