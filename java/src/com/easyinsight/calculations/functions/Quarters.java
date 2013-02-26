package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

import java.util.Calendar;

/**
 * User: jamesboe
 * Date: 2/20/13
 * Time: 8:40 AM
 */
public class Quarters extends Function {
    public Value evaluate() {
        int monthNumber = params.get(0).toDouble().intValue();
        NumericValue numericValue = new NumericValue(monthNumber);
        numericValue.setCalendarType(Calendar.MONTH);
        numericValue.setCalendarValue(monthNumber * 3);
        return numericValue;
    }

    public int getParameterCount() {
        return 1;
    }
}
