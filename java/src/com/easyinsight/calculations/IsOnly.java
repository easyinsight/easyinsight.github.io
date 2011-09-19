package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
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
        AnalysisItem target = null;
        for (AnalysisItem field : calculationMetadata.getDataSourceFields()) {
            if (field.toDisplay().equals(fieldName)) {
                target = field;
                break;
            }
        }
        DataSet dataSet = calculationMetadata.getDataSet();
        Set<Value> values = new HashSet<Value>();
        for (IRow row : dataSet.getRows()) {
            values.add(row.getValue(target));
        }
        Value matchValue = minusQuotes(getParameter(1));
        if (values.size() == 1 && values.contains(matchValue)) {
            System.out.println("doing it...");
            return getParameter(2);
        }
        System.out.println("no joy");
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
