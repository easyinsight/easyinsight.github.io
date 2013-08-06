package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.calculations.Function;
import com.easyinsight.calculations.FunctionException;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 8/5/13
 * Time: 11:33 AM
 */
public class AdjustFilterFunction extends Function {
    public Value evaluate() {
        String filterName = minusQuotes(0);
        String fieldName = minusQuotes(1);
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
        AnalysisItem fieldToAssign = null;
        for (AnalysisItem analysisItem : calculationMetadata.getDataSourceFields()) {
            if (fieldName.equals(analysisItem.toDisplay())) {
                fieldToAssign = analysisItem;
            }
        }
        if (fieldToAssign == null) {
            throw new FunctionException("Could not find field " + fieldToAssign + ".");
        }
        filterToAdjust.setField(fieldToAssign);
        return new EmptyValue();
    }

    public int getParameterCount() {
        return 2;
    }
}
