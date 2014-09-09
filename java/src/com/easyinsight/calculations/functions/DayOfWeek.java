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
        Date startDate = null;
        if (params.size() == 0) {
            startDate = new Date();
        } else {
            Value start = params.get(0);
            if (start.type() == Value.DATE) {
                DateValue dateValue = (DateValue) start;
                startDate = dateValue.getDate();
            } else if (start.type() == Value.NUMBER) {
                startDate = new Date(start.toDouble().longValue());
            }
        }
        if (startDate != null) {
            Calendar calendar = Calendar.getInstance();


            ZonedDateTime zdt = startDate.toInstant().atZone(ZoneId.ofOffset("", ZoneOffset.ofHours(-(calculationMetadata.getInsightRequestMetadata().getUtcOffset() / 60))));

            System.out.println(zdt + " - " + zdt.getDayOfWeek() + " - " + zdt.with(ChronoField.DAY_OF_WEEK, java.time.DayOfWeek.SUNDAY.getValue()));


            if (params.size() == 2) {
                int dayToSet = params.get(1).toDouble().intValue();

                java.time.DayOfWeek dow = translateDayOfWeek(dayToSet);
                java.time.DayOfWeek firstDayOfWeek = translateDayOfWeek(SecurityUtil.getFirstDayOfWeek());

                WeekFields weekFields = WeekFields.of(firstDayOfWeek, 1);
                TemporalField adjuster = weekFields.dayOfWeek();
                long tFrom = adjuster.getFrom(dow);
                zdt = zdt.with(adjuster, tFrom);

                Date result = Date.from(zdt.toInstant());

                return new DateValue(result);
            } else {
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
                calendar.setTimeZone(timeZone);
                calendar.setTimeInMillis(startDate.getTime());
                calendar.setFirstDayOfWeek(SecurityUtil.getFirstDayOfWeek());
            }
            return new NumericValue(calendar.get(Calendar.DAY_OF_WEEK));
        } else {
            return new EmptyValue();
        }
    }

    protected java.time.DayOfWeek translateDayOfWeek(int dayToSet) {
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
