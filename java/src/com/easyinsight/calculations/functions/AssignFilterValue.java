package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.*;
import com.easyinsight.calculations.Function;
import com.easyinsight.calculations.FunctionException;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 5/21/13
 * Time: 1:30 PM
 */
public class AssignFilterValue extends Function {
    public Value evaluate() {
        WSAnalysisDefinition report = calculationMetadata.getReport();
        FilterDefinition matchedFilter = null;
        String filterName = minusQuotes(params.get(0)).toString();
        for (FilterDefinition filter : report.getFilterDefinitions()) {
            if (filterName.equals(filter.getFilterName())) {
                matchedFilter = filter;
                break;
            }
        }
        if (matchedFilter != null) {
            if (matchedFilter instanceof FilterValueDefinition) {
                AnalysisDimensionResultMetadata metadata = (AnalysisDimensionResultMetadata) new DataService().getAnalysisItemMetadata(report.getDataFeedID(),
                        matchedFilter.getField(), calculationMetadata.getInsightRequestMetadata().getUtcOffset(), 0, 0);
                SimpleDateFormat sdf = new SimpleDateFormat(minusQuotes(params.get(1)).toString());
                List<Date> dates = new ArrayList<Date>();
                Map<Date, Object> dateMap = new HashMap<Date, Object>();
                FilterValueDefinition filterValueDefinition = (FilterValueDefinition) matchedFilter;
                List<Value> values = metadata.getValues();
                for (Value value : values) {
                    String string = value.toString();
                    try {
                        Date date = sdf.parse(string);
                        dateMap.put(date, value);
                        dates.add(date);
                    } catch (ParseException e) {
                        // ignore
                    }
                }
                Collections.sort(dates);
                Date latestDate = null;
                for (Date date : dates) {
                    if (latestDate == null || date.after(latestDate)) {
                        latestDate = date;
                    }
                }
                if (latestDate != null) {
                    filterValueDefinition.setFilteredValues(Arrays.asList(dateMap.get(latestDate)));
                }
            }
        }

        return new EmptyValue();
    }

    public int getParameterCount() {
        return 2;
    }
}
