package com.easyinsight.calculations;

import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

import java.util.Calendar;

/**
 * User: jamesboe
 * Date: 2/13/13
 * Time: 4:25 PM
 */
public class IntervalQuarters extends Function {
    public Value evaluate() {
        Value startDate = params.get(0);
        Value endDate = params.get(1);
        if (startDate.type() == Value.DATE && endDate.type() == Value.DATE) {
            DateValue startDateValue = (DateValue) startDate;
            DateValue endDateValue = (DateValue) endDate;
            if (endDateValue.getDate().before(startDateValue.getDate())) {
                return new EmptyValue();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDateValue.getDate());
            int i = 0;
            int lastQuarter = (int) Math.floor(cal.get(Calendar.MONTH) / 3);
            while (cal.getTime().before(endDateValue.getDate())) {
                cal.add(Calendar.MONTH, 1);
                int quarter = (int) Math.floor(cal.get(Calendar.MONTH) / 3);
                if (quarter != lastQuarter) {
                    i++;
                }
                lastQuarter = quarter;
            }
            return new NumericValue(i);
        }
        return new EmptyValue();
    }

    public int getParameterCount() {
        return 2;
    }
}
