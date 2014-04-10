package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

import java.util.Calendar;
import java.util.Date;

/**
 * User: jamesboe
 * Date: 4/2/14
 * Time: 12:01 PM
 */
public class BusinessDaysBetween extends Function {
    public Value evaluate() {
        Value start = params.get(0);
        Value end = params.get(1);
        if (start.type() == Value.DATE && end.type() == Value.DATE) {
            Date startDate = ((DateValue) start).getDate();
            Date endDate = ((DateValue) end).getDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            int startDay = cal.get(Calendar.DAY_OF_YEAR);
            int startYear = cal.get(Calendar.YEAR);
            cal.setTime(endDate);
            int endDay = cal.get(Calendar.DAY_OF_YEAR);
            int endYear = cal.get(Calendar.YEAR);
            if ((startYear > endYear) || (startYear == endYear && startDay > endDay)) {

            } else if (startYear == endYear && startDay == endDay) {

            } else {
                cal.setTime(startDate);
                int days = 0;
                int day;
                int year;
                do {
                    cal.add(Calendar.DAY_OF_YEAR, 1);
                    int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                    if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {

                    } else {
                        days++;
                    }
                    day = cal.get(Calendar.DAY_OF_YEAR);
                    year = cal.get(Calendar.YEAR);
                } while ((year < endYear) || (day < endDay));
                long ms = days * (1000L * 60 * 60 * 24);
                NumericValue numericValue = new NumericValue(ms);
                numericValue.setCalendarType(Calendar.DAY_OF_YEAR);
                numericValue.setCalendarValue(days);
                return numericValue;
            }

        }
        return new EmptyValue();
    }

    public int getParameterCount() {
        return 2;
    }
}
