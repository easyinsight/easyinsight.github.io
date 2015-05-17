package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;
import com.easyinsight.security.SecurityUtil;

import java.time.*;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
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
        ZoneId zoneId = calculationMetadata.getInsightRequestMetadata().createZoneID();
        LocalDate startDate = date();
        if (startDate != null) {
            if (params.size() == 2) {
                int dayToSet = params.get(1).toDouble().intValue();

                java.time.DayOfWeek dow = translateDayOfWeek(dayToSet);
                java.time.DayOfWeek firstDayOfWeek = translateDayOfWeek(SecurityUtil.getFirstDayOfWeek());



                WeekFields weekFields = WeekFields.of(firstDayOfWeek, 1);
                TemporalField adjuster = weekFields.dayOfWeek();
                long tFrom = adjuster.getFrom(dow);
                startDate = startDate.with(adjuster, tFrom);
                Instant instant = startDate.atStartOfDay().atZone(zoneId).toInstant();
                Date result = Date.from(instant);

                return new DateValue(result);
            } else {
                return new NumericValue(Utils.inverseDayOfWeek(startDate.getDayOfWeek()));
            }
        } else {
            return new EmptyValue();
        }
    }



    public static java.time.DayOfWeek translateDayOfWeek(int dayToSet) {
        java.time.DayOfWeek dow;
        switch(dayToSet) {
            case Calendar.SUNDAY:
                dow = java.time.DayOfWeek.SUNDAY;
                break;
            case Calendar.MONDAY:
                dow = java.time.DayOfWeek.MONDAY;
                break;
            case Calendar.TUESDAY:
                dow = java.time.DayOfWeek.TUESDAY;
                break;
            case Calendar.WEDNESDAY:
                dow = java.time.DayOfWeek.WEDNESDAY;
                break;
            case Calendar.THURSDAY:
                dow = java.time.DayOfWeek.THURSDAY;
                break;
            case Calendar.FRIDAY:
                dow = java.time.DayOfWeek.FRIDAY;
                break;
            case Calendar.SATURDAY:
                dow = java.time.DayOfWeek.SATURDAY;
                break;
            default:
                throw new RuntimeException();
        }
        return dow;
    }

    public int getParameterCount() {
        return -1;
    }

    public static void main(String[] args) {

    }
}
