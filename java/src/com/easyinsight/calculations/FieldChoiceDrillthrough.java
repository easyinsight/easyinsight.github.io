package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemFilterDefinition;
import com.easyinsight.analysis.FilterValueDefinition;
import com.easyinsight.core.Value;

import java.util.Arrays;

/**
 * User: jamesboe
 * Date: 1/26/12
 * Time: 2:54 PM
 */
public class FieldChoiceDrillthrough extends Function {
    public Value evaluate() {
        DrillthroughCalculationMetadata drillthroughCalculationMetadata = (DrillthroughCalculationMetadata) calculationMetadata;
        String filterName = minusQuotes(0);
        String target = minusQuotes(1);
        AnalysisItem targetItem = null;
        for (AnalysisItem analysisItem : drillthroughCalculationMetadata.getAnalysisItems()) {
            if (target.equals(analysisItem.toDisplay())) {
                targetItem = analysisItem;
            }
        }
        AnalysisItemFilterDefinition analysisItemFilterDefinition = new AnalysisItemFilterDefinition();
        analysisItemFilterDefinition.setTargetItem(targetItem);
        analysisItemFilterDefinition.setFilterName(filterName);

        drillthroughCalculationMetadata.getDrillThroughFilters().add(analysisItemFilterDefinition);
        return null;
    }

    public int getParameterCount() {
        return 1;
    }
}
