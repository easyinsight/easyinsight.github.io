package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.FilterValueDefinition;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.HashSet;
import java.util.Set;

/**
 * User: jamesboe
 * Date: 9/17/11
 * Time: 5:18 PM
 */
public class IsOnly extends Function {
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

            String matchValue = minusQuotes(getParameter(1)).toString();
            if (value != null && value.equals(matchValue)) {
                return getParameter(2);
            }
        } else {
            AnalysisItem target = null;
            for (AnalysisItem field : calculationMetadata.getDataSourceFields()) {
                if (field.toDisplay().equals(fieldName)) {
                    target = field;
                    break;
                }
            }
            DataSet dataSet = calculationMetadata.getDataSet();
            if (dataSet != null) {
                Set<Value> values = new HashSet<Value>();
                for (IRow row : dataSet.getRows()) {
                    values.add(row.getValue(target));
                }
                Value matchValue = minusQuotes(getParameter(1));
                if (values.size() == 1 && values.contains(matchValue)) {
                    return getParameter(2);
                }
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
