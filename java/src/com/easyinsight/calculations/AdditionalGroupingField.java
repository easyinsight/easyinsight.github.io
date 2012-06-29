package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 3/19/12
 * Time: 4:52 PM
 */
public class AdditionalGroupingField extends Function {
    public Value evaluate() {
        WSAnalysisDefinition report = calculationMetadata.getReport();
        String field = minusQuotes(0);
        AnalysisItem match = null;
        for (AnalysisItem analysisItem : calculationMetadata.getDataSourceFields()) {
            if (field.equals(analysisItem.toDisplay())) {
                match = analysisItem;
            }
        }
        if (match == null) {
            throw new FunctionException("Could not find field " + field + ".");
        }
        AnalysisItem clone;
        try {
            clone = match.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        report.getAdditionalGroupingItems().add(clone);
        return null;
    }

    public int getParameterCount() {
        return 1;
    }
}
