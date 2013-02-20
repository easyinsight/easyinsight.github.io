package com.easyinsight.calculations;

import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;
import org.joda.time.Interval;
import org.joda.time.Weeks;

/**
 * User: jamesboe
 * Date: 2/13/13
 * Time: 4:25 PM
 */
public class IntervalWeeks extends Function {
    public Value evaluate() {
        Value startDate = params.get(0);
        Value endDate = params.get(1);
        if (startDate.type() == Value.DATE && endDate.type() == Value.DATE) {
            DateValue startDateValue = (DateValue) startDate;
            DateValue endDateValue = (DateValue) endDate;
            Interval interval = new Interval(startDateValue.getDate().getTime(), endDateValue.getDate().getTime());
            return new NumericValue(Weeks.weeksIn(interval).getWeeks());
        }
        return new EmptyValue();
    }

    public int getParameterCount() {
        return 2;
    }
}
