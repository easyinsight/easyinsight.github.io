package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItemFilterDefinition;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 3/19/12
 * Time: 9:37 AM
 */
public class MapFilterField extends Function {
    public Value evaluate() {
        String filterName = minusQuotes(0);
        String targetFilter = minusQuotes(1);
        WSAnalysisDefinition report = calculationMetadata.getReport();
        AnalysisItemFilterDefinition aFilter = null;
        for (FilterDefinition filter : report.getFilterDefinitions()) {
            if (targetFilter.equals(filter.getFilterName())) {
                aFilter = (AnalysisItemFilterDefinition) filter;
            }
        }
        if (aFilter == null) {
            throw new FunctionException("Could not find a field choice filter labeled as " + targetFilter + ".");
        }
        for (FilterDefinition filter : report.getFilterDefinitions()) {
            if (filterName.equals(filter.getFilterName())) {
                filter.setField(aFilter.getTargetItem());
            }
        }
        return new EmptyValue();
    }

    public int getParameterCount() {
        return 2;
    }
}
