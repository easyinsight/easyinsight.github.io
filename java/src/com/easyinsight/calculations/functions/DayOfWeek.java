package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;
import com.easyinsight.security.SecurityUtil;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * User: jamesboe
 * Date: 12/29/11
 * Time: 1:15 PM
 */
public class DayOfWeek extends Function {
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
            Calendar calendar = Calendar.getInstance();
            int time = calculationMetadata.getInsightRequestMetadata().getUtcOffset() / 60;
            ZonedDateTime zdt = startDate.toInstant().atZone(ZoneId.ofOffset("", ZoneOffset.ofHours(calculationMetadata.getInsightRequestMetadata().getUtcOffset() / 60)));

            System.out.println(zdt + " - " + zdt.getDayOfWeek() + " - " + zdt.with(ChronoField.DAY_OF_WEEK, java.time.DayOfWeek.SUNDAY.getValue()));

            String string;
            if (time > 0) {
                string = "GMT-"+Math.abs(time);
            } else if (time < 0) {
                string = "GMT+"+Math.abs(time);
            } else {
                string = "GMT";
            }
            if (params.size() == 2) {
                int dayToSet = params.get(1).toDouble().intValue();
                Date result = Date.from(zdt.with(ChronoField.DAY_OF_WEEK, dayToSet).toInstant());


                System.out.println("Setting to " + dayToSet + " gave result = " + result);
                return new DateValue(result);
            } else {
                calendar.setFirstDayOfWeek(SecurityUtil.getFirstDayOfWeek());
            }
            return new NumericValue(calendar.get(Calendar.DAY_OF_WEEK));
        } else {
            return new EmptyValue();
        }
    }

    public int getParameterCount() {
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + " - " + Calendar.getInstance().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
    }
}
