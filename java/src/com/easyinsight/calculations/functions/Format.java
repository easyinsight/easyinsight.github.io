package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.calculations.Function;
import com.easyinsight.core.*;
import com.easyinsight.export.ExportService;

import java.util.Calendar;

/**
 * User: jamesboe
 * Date: 1/27/12
 * Time: 9:40 AM
 */
public class Format extends Function {
    public Value evaluate() {
        Value value = getParameter(0);
        String fieldName = minusBrackets(getParameterName(0));
        AnalysisItem field = null;
        for (AnalysisItem analysisItem : calculationMetadata.getDataSourceFields()) {
            if (fieldName.equals(analysisItem.toDisplay()) || fieldName.equals(analysisItem.getKey().toKeyString())) {
                field = analysisItem;
            }
        }
        return new StringValue(ExportService.createValue(1, field, value, Calendar.getInstance(), "$", true));
    }

    public int getParameterCount() {
        return 1;
    }

    @Override
    public boolean onDemand() {
        return true;
    }
}
