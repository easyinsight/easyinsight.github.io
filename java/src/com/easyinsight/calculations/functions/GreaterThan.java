package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;

import java.util.Calendar;

/**
 * User: jamesboe
 * Date: 8/29/11
 * Time: 7:00 PM
 */
public class GreaterThan extends Function {
    public Value evaluate() {
        Value compare1 = minusQuotes(getParameter(0));
        Value compare2 = minusQuotes(getParameter(1));
        Value returnValue;
        if (compare1.type() == Value.DATE || compare2.type() == Value.DATE) {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();

            if (compare1.type() == Value.DATE) {
                DateValue dateValue = (DateValue) compare1;
                cal1.setTime(dateValue.getDate());
            } else {
                cal1.setTimeInMillis(compare1.toDouble().longValue());
            }

            if (compare2.type() == Value.DATE) {
                DateValue dateValue = (DateValue) compare2;
                cal2.setTime(dateValue.getDate());
            } else {
                cal2.setTimeInMillis(compare2.toDouble().longValue());
            }

            if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR) ||
                    (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR))) {
                returnValue = minusQuotes(getParameter(2));
            } else {
                returnValue = minusQuotes(getParameter(3));
            }
        } else {
            if (compare1.toDouble() > compare2.toDouble()) {
                returnValue = minusQuotes(getParameter(2));
            } else {
                returnValue = minusQuotes(getParameter(3));
            }
        }

        return returnValue;
        /*
        if (cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR) &&
                    cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
         */
    }

    @Override
    public boolean onDemand() {
        return true;
    }

    public int getParameterCount() {
        return -1;
    }
}
