package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

import java.time.*;
import java.util.Date;

/**
 * User: jamesboe
 * Date: 12/29/11
 * Time: 1:05 PM
 */
public class BusinessDaysInMonth extends Function {
    public Value evaluate() {
        LocalDate startDate = date();
        if (startDate != null) {
            int month = startDate.getMonthValue();
            //int month = calendar.get(Calendar.MONTH);
            /*Calendar cal1 = Calendar.getInstance();
            cal1.setTime(startDate);
            cal1.set(Calendar.DAY_OF_MONTH, 1);*/
            LocalDate sd2 = startDate.withDayOfMonth(1);
            boolean daysFound = false;
            int i = 0;
            while (!daysFound) {
                java.time.DayOfWeek dayOfWeek = sd2.getDayOfWeek();
                if (dayOfWeek == java.time.DayOfWeek.SUNDAY || dayOfWeek == java.time.DayOfWeek.SATURDAY) {

                } else {
                    i++;
                }
                sd2 = sd2.plusDays(1);

                if (sd2.getMonthValue() != month) {
                    daysFound = true;
                }
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
