package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.FilterDateRangeDefinition;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.calculations.Function;
import com.easyinsight.calculations.FunctionException;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

import java.util.Date;

/**
 * User: jamesboe
 * Date: 10/9/12
 * Time: 12:29 PM
 */
public class DateFilter extends Function {
    public Value evaluate() {
        String filterName = minusQuotes(0);
        Value startDateValue = params.get(1);
        Value endDateValue = params.get(2);
        Date startDate;
        Date endDate;
        if (startDateValue.type() == Value.DATE) {
            startDate = ((DateValue) startDateValue).getDate();
        } else if (startDateValue.type() == Value.NUMBER) {
            startDate = new Date(startDateValue.toDouble().longValue());
        } else {
            throw new FunctionException("Start date has to be a date or number of milliseconds.");
        }
        if (endDateValue.type() == Value.DATE) {
            endDate = ((DateValue) endDateValue).getDate();
        } else if (endDateValue.type() == Value.NUMBER) {
            endDate = new Date(endDateValue.toDouble().longValue());
        } else {
            throw new FunctionException("End date has to be a date or number of milliseconds.");
        }
        WSAnalysisDefinition report = calculationMetadata.getReport();
        FilterDateRangeDefinition target = null;
        for (FilterDefinition filterDefinition : report.getFilterDefinitions()) {
            if (filterDefinition instanceof FilterDateRangeDefinition) {
                FilterDateRangeDefinition filterDateRangeDefinition = (FilterDateRangeDefinition) filterDefinition;
                if (filterName.equals(filterDateRangeDefinition.getFilterName())) {
                    target = filterDateRangeDefinition;
                    break;
                }
            }
        }
        if (target == null) {
            throw new FunctionException("Could not find an absolute filter by the name of " + target + " in the report.");
        }
        target.setStartDate(startDate);
        target.setEndDate(endDate);

        return new EmptyValue();
    }

    public int getParameterCount() {
        return 3;
    }
}
