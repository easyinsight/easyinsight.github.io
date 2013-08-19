package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.calculations.Function;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 8/12/13
 * Time: 6:04 PM
 */
public class DirectFilterFunction extends Function {
    public Value evaluate() {
        String filterName = minusQuotes(0);
        WSAnalysisDefinition report = calculationMetadata.getReport();
        FilterDefinition filterToAdjust = null;
        for (FilterDefinition filter : report.getFilterDefinitions()) {
            if (filterName.equals(filter.getFilterName())) {
                filterToAdjust = filter;
            }
        }
        if (filterToAdjust == null) {
            return new EmptyValue();
            //throw new FunctionException("Could not find filter " + filterName + ".");
        }
        calculationMetadata.getInsightRequestMetadata();
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getParameterCount() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
