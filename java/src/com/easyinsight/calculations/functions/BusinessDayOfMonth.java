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
public class BusinessDayOfMonth extends Function {
    public Value evaluate() {
        LocalDate startDate = date();
        if (startDate != null) {

            int targetDay = startDate.getDayOfMonth();
            LocalDate date1 = startDate.withDayOfMonth(1);
            boolean daysFound = false;
            int i = 0;
            while (!daysFound) {
                java.time.DayOfWeek dayOfWeek = date1.getDayOfWeek();
                if (dayOfWeek == java.time.DayOfWeek.SUNDAY || dayOfWeek == java.time.DayOfWeek.SATURDAY) {

                } else {
                    i++;
                }
                date1 = date1.plusDays(1);
                if (date1.getDayOfMonth() == targetDay) {
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
