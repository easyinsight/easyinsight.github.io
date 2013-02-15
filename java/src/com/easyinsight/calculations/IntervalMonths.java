package com.easyinsight.calculations;

import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;
import org.joda.time.Months;

/**
 * User: jamesboe
 * Date: 2/13/13
 * Time: 4:25 PM
 */
public class IntervalMonths extends Function {
    public Value evaluate() {
        Value value = getParameter(0);
        double doubleValue = value.toDouble();
        return new NumericValue(Months.months((int) doubleValue).getMonths());
    }

    public int getParameterCount() {
        return 1;
    }
}
