package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * User: jamesboe
 * Date: 12/29/11
 * Time: 1:15 PM
 */
public class DayOfMonth extends Function {
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
            if (calculationMetadata != null && calculationMetadata.isFilterTimeShift()) {
                Instant instant = startDate.toInstant();
                ZoneId zoneId = calculationMetadata.getInsightRequestMetadata().createZoneID();

                ZonedDateTime zdt = instant.atZone(zoneId);

                // pathway enhanced network

                if (params.size() == 2) {
                    zdt = zdt.withDayOfMonth(params.get(1).toDouble().intValue());
                    zdt = zdt.withHour(0).withMinute(0).withSecond(0).withNano(0);
                    instant = zdt.toInstant();
                    Date date = Date.from(instant);
                    System.out.println("result = " + date);
                    return new DateValue(date);
                } else {
                    return new NumericValue(zdt.getDayOfMonth());
                }
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(startDate.getTime());
                if (params.size() == 2) {
                    int dayToSet = params.get(1).toDouble().intValue();
                    calendar.set(Calendar.DAY_OF_MONTH, dayToSet);
                    return new DateValue(calendar.getTime());
                } else {
                    return new NumericValue(calendar.get(Calendar.DAY_OF_MONTH));
                }
            }
        } else {
            return new EmptyValue();
        }
    }

    public static void main(String[] args) {


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -2);
        Instant instant = cal.getTime().toInstant();

        ZoneId zoneId = ZoneId.systemDefault();
        //ZoneId zoneId = ZoneId.ofOffset("", ZoneOffset.ofHours(-(calculationMetadata.getInsightRequestMetadata().getUtcOffset() / 60)));
        System.out.println(zoneId.getId());
        ZonedDateTime zdt = instant.atZone(zoneId);
        zdt = zdt.withDayOfMonth(1);
        zdt = zdt.withHour(0).withMinute(0).withSecond(0).withNano(0);
        instant = zdt.toInstant();
        Date date = Date.from(instant);
        System.out.println(date);
    }

    public int getParameterCount() {
        return -1;
    }
}
