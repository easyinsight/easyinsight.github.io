package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemFilterDefinition;
import com.easyinsight.analysis.definitions.WSVerticalListDefinition;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 9/17/11
 * Time: 3:42 PM
 */
public class AddFieldToFilter extends Function {
    public Value evaluate() {
        if (calculationMetadata.getFilterDefinition() != null) {
            String fieldName = minusQuotes(params.get(0)).toString().toLowerCase();
            AnalysisItem fieldToAdd = null;
            for (AnalysisItem analysisItem : calculationMetadata.getDataSourceFields()) {
                if (analysisItem.toDisplay().toLowerCase().equals(fieldName)) {
                    fieldToAdd = analysisItem;
                }
            }
            AnalysisItemFilterDefinition analysisItemFilterDefinition = (AnalysisItemFilterDefinition) calculationMetadata.getFilterDefinition();
            if (!analysisItemFilterDefinition.getAvailableItems().contains(fieldToAdd)) {
                analysisItemFilterDefinition.getAvailableItems().add(fieldToAdd);
            }
        }
        return new EmptyValue();
    }

    public int getParameterCount() {
        return -1;
    }
}
