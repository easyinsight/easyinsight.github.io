package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.calculations.Function;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

import java.util.Calendar;

/**
 * User: jamesboe
 * Date: 6/3/13
 * Time: 2:24 PM
 */
public class DateLevelFunction extends Function {
    public Value evaluate() {
        Value value = params.get(0);
        if (value.type() == Value.DATE) {
            DateValue dateValue = (DateValue) value;
            int level = Integer.parseInt(minusQuotes(1));
            AnalysisDateDimension dateDimension = new AnalysisDateDimension();
            dateDimension.setDateLevel(level);
            Value result = dateDimension.transformValue(dateValue, new InsightRequestMetadata(), false, Calendar.getInstance());
            System.out.println("transformed " + dateValue + " into " + result);
            return result;
        }
        return new EmptyValue();
    }

    public int getParameterCount() {
        return 2;
    }
}
