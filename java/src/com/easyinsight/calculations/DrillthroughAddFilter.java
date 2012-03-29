package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.FilterValueDefinition;
import com.easyinsight.core.Value;

import java.util.Arrays;

/**
 * User: jamesboe
 * Date: 1/26/12
 * Time: 2:54 PM
 */
public class DrillthroughAddFilter extends Function {
    public Value evaluate() {
        DrillthroughCalculationMetadata drillthroughCalculationMetadata = (DrillthroughCalculationMetadata) calculationMetadata;
        String target = minusQuotes(0);
        AnalysisItem targetItem = null;
        for (AnalysisItem analysisItem : drillthroughCalculationMetadata.getAnalysisItems()) {
            if (target.equals(analysisItem.toDisplay())) {
                targetItem = analysisItem;
            }
        }
        if (targetItem == null) {
            throw new FunctionException("We couldn't find a field by the name of " + target + ".");
        }
        FilterValueDefinition filterValueDefinition = new FilterValueDefinition();
        filterValueDefinition.setField(targetItem);
        filterValueDefinition.setSingleValue(true);
        filterValueDefinition.setEnabled(true);
        filterValueDefinition.setInclusive(true);
        filterValueDefinition.setToggleEnabled(false);
        filterValueDefinition.setFilteredValues(Arrays.asList(drillthroughCalculationMetadata.getData().get(targetItem.qualifiedName())));
        drillthroughCalculationMetadata.getDrillThroughFilters().add(filterValueDefinition);
        return null;
    }

    public int getParameterCount() {
        return 1;
    }
}
