package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.calculations.FunctionException;
import com.easyinsight.calculations.IFunction;
import com.easyinsight.core.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * User: jamesboe
 * Date: 12/7/11
 * Time: 11:10 AM
 */
public class EIDateFormatFunction extends Function implements IFunction {
    public Value evaluate() {
        Value value = getParameter(0);
        if (value.type() == Value.EMPTY) {
            return value;
        }
        Date date;
        if (value.type() == Value.DATE) {
            DateValue dateValue = (DateValue) value;
            date = dateValue.getDate();
        } else if (value.type() == Value.NUMBER) {
            date = new Date(value.toDouble().longValue());
        } else {
            throw new FunctionException("We couldn't parse the value of " + value.toString() + " as a date.");
        }
        Calendar cal = null;

        if (calculationMetadata.getInsightRequestMetadata() != null) {
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

            cal = Calendar.getInstance();
            cal.setTimeZone(timeZone);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(minusQuotes(getParameter(1)).toString());
        if (cal != null) {
            simpleDateFormat.setCalendar(cal);
        }
        return new StringValue(simpleDateFormat.format(date));
    }

    @Override
    public boolean onDemand() {
        return true;
    }

    public int getParameterCount() {
        return 2;
    }
}
