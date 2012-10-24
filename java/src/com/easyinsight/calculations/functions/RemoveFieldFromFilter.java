package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemFilterDefinition;
import com.easyinsight.calculations.Function;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

import java.util.Iterator;

/**
 * User: jamesboe
 * Date: 9/17/11
 * Time: 3:42 PM
 */
public class RemoveFieldFromFilter extends Function {
    public Value evaluate() {
        if (calculationMetadata.getFilterDefinition() != null && calculationMetadata.getFilterDefinition() instanceof AnalysisItemFilterDefinition) {
            String fieldName = minusQuotes(params.get(0)).toString().toLowerCase();
            AnalysisItemFilterDefinition analysisItemFilterDefinition = (AnalysisItemFilterDefinition) calculationMetadata.getFilterDefinition();
            Iterator<AnalysisItem> iter = analysisItemFilterDefinition.getAvailableItems().iterator();
            while (iter.hasNext()) {
                AnalysisItem analysisItem = iter.next();
                if (analysisItem.toDisplay().toLowerCase().equals(fieldName)) {
                    iter.remove();
                }
            }
        }

        return new EmptyValue();
    }

    public int getParameterCount() {
        return -1;
    }
}
