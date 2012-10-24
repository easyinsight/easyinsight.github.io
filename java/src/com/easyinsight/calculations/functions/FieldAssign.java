package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemFilterDefinition;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.calculations.Function;
import com.easyinsight.calculations.FunctionException;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 1/26/12
 * Time: 2:54 PM
 */
public class FieldAssign extends Function {
    public Value evaluate() {
        String sourceFilter = minusQuotes(0);
        String targetFilter = minusQuotes(1);
        AnalysisItemFilterDefinition sourceFilterDefinition = null;
        FilterDefinition targetFilterDefinition = null;
        WSAnalysisDefinition report = calculationMetadata.getReport();
        for (FilterDefinition filterDefinition : report.getFilterDefinitions()) {
            if (sourceFilter.equals(filterDefinition.getFilterName())) {
                sourceFilterDefinition = (AnalysisItemFilterDefinition) filterDefinition;
            }
            if (targetFilter.equals(filterDefinition.getFilterName())) {
                targetFilterDefinition = filterDefinition;
            }
        }
        if (sourceFilterDefinition == null) {
            throw new FunctionException("We couldn't find a filter labeled " + sourceFilter + ".");
        }
        if (targetFilterDefinition == null) {
            throw new FunctionException("We couldn't find a filter labeled " + targetFilter + ".");
        }
        targetFilterDefinition.setField(sourceFilterDefinition.getTargetItem());
        return null;
    }

    public int getParameterCount() {
        return 2;
    }
}
