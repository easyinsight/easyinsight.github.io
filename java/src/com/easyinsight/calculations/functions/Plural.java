package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.calculations.Function;
import com.easyinsight.core.*;
import com.easyinsight.export.ExportService;

import java.util.Calendar;

/**
 * User: jamesboe
 * Date: 2/24/14
 * Time: 6:44 PM
 */
public class Plural extends Function {
    public Value evaluate() {
        Value value = getParameter(0);
        AnalysisItem field = findDataSourceItem(0);
        String string = ExportService.createValue(1, field, value, Calendar.getInstance(), "$", true);
        double doubleValue = value.toDouble();
        String word = minusQuotes(getParameter(1)).toString();
        if (Math.abs(doubleValue) <= 1) {
            return new StringValue(string + " " + word);
        } else {
            return new StringValue(string + " " + word + "s");
        }
    }

    @Override
    public boolean onDemand() {
        return true;
    }

    public int getParameterCount() {
        return 2;
    }
}
