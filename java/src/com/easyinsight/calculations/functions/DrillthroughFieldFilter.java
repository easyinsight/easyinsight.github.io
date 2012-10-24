package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.FilterValueDefinition;
import com.easyinsight.calculations.DrillthroughCalculationMetadata;
import com.easyinsight.calculations.Function;
import com.easyinsight.calculations.FunctionException;
import com.easyinsight.core.Value;

import java.util.Arrays;
import java.util.List;

/**
 * User: jamesboe
 * Date: 1/26/12
 * Time: 2:54 PM
 */
public class DrillthroughFieldFilter extends Function {
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
        List<FilterDefinition> filters = targetItem.getFilters();
        for (FilterDefinition filter : filters) {
            try {
                FilterDefinition clone = filter.clone();
                drillthroughCalculationMetadata.getDrillThroughFilters().add(clone);
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public int getParameterCount() {
        return 1;
    }
}
