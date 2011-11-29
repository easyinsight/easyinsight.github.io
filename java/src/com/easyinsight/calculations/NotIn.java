package com.easyinsight.calculations;

import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.FilterValueDefinition;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 9/17/11
 * Time: 5:18 PM
 */
public class NotIn extends Function {
    public Value evaluate() {
        String fieldName = minusBrackets(getParameterName(0));
        String value = null;
        if (calculationMetadata.getFilters() != null) {
            for (FilterDefinition filterDefinition : calculationMetadata.getFilters()) {
                if (filterDefinition.getField() != null && filterDefinition.getField().toDisplay().equals(fieldName)) {
                    if (filterDefinition instanceof FilterValueDefinition) {
                        FilterValueDefinition filterValueDefinition = (FilterValueDefinition) filterDefinition;
                        if (filterValueDefinition.getFilteredValues().size() == 1) {
                            value = filterValueDefinition.getFilteredValues().get(0).toString();
                        }
                    }
                }
            }
            if (value == null || "All".equals(value)) {
                return getParameter(paramCount() - 1);
            }
            boolean found = false;
            for (int i = 1; i < paramCount() - 1; i++) {
                String matchValue = minusQuotes(getParameter(i)).toString();
                if (value.equals(matchValue)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return getParameter(paramCount() - 1);
            }
        }
        return new EmptyValue();
    }

    @Override
    public boolean onDemand() {
        return true;
    }

    public int getParameterCount() {
        return -1;
    }
}
