package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * User: jamesboe
 * Date: 2/19/13
 * Time: 2:24 PM
 */
public class DaysInQuarter extends Function {
    public Value evaluate() {
        Date startDate = null;
        if (params.size() == 0) {
            startDate = new Date();
        } else {
            Value start = params.get(0);
            if (start.type() == Value.DATE) {
                DateValue dateValue = (DateValue) start;
                startDate = dateValue.getDate();
            }
        }
        if (startDate != null) {
            Calendar cal = Calendar.getInstance();
            int time = calculationMetadata.getInsightRequestMetadata().getUtcOffset() / 60;
            String string;
            if (time > 0) {
                string = "GMT-"+Math.abs(time);
            } else if (time < 0) {
                string = "GMT+"+Math.abs(time);
            } else {
                string = "GMT";
            }
            TimeZone timeZone = TimeZone.getTimeZone(string);
            cal.setTimeZone(timeZone);
            cal.setTimeInMillis(startDate.getTime());
            int i = 0;
            int month = cal.get(Calendar.MONTH);
            int firstMonth = month / 3 * 3;
            cal.set(Calendar.MONTH, firstMonth);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            int quarter = month / 3;
            int newQuarter = quarter;
            while (quarter == newQuarter) {
                cal.add(Calendar.DAY_OF_YEAR, 1);
                newQuarter = (int) Math.floor(cal.get(Calendar.MONTH) / 3);
                i++;
            }
            return new NumericValue(i);
        } else {
            return new EmptyValue();
        }
    }

    public int getParameterCount() {
        return -1;
    }
}
