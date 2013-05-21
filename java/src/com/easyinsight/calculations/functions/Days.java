package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

import java.util.Calendar;

/**
 * User: jamesboe
 * Date: 1/27/12
 * Time: 9:40 AM
 */
public class Days extends Function {
    public Value evaluate() {
        int dayNumber = params.get(0).toDouble().intValue();
        long days = params.get(0).toDouble().longValue();
        long ms = days * (1000 * 60 * 60 * 24);
        NumericValue numericValue = new NumericValue(ms);
        numericValue.setCalendarType(Calendar.DAY_OF_YEAR);
        numericValue.setCalendarValue(dayNumber);
        return numericValue;
    }

    public int getParameterCount() {
        return 1;
    }
}
