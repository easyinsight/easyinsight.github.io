package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.IsoFields;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;

/**
 * User: jamesboe
 * Date: 12/29/11
 * Time: 1:15 PM
 */
public class DayOfQuarter extends Function {


    public static int quarter(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return (int) Math.floor(cal.get(Calendar.MONTH) / 3);
    }

    public static int quarter(Temporal temporal) {
        return (temporal.get(ChronoField.MONTH_OF_YEAR) - 1) / 3;
    }

    public Value evaluate() {
        ZoneId zoneId = calculationMetadata.getInsightRequestMetadata().createZoneID();
        LocalDate localDate = date();
        if (localDate != null) {


            if (params.size() == 2) {
                int dayToSet = params.get(1).toDouble().intValue();
                if (dayToSet < 1) {
                    dayToSet = 1;
                }
                localDate = localDate.with(IsoFields.DAY_OF_QUARTER, dayToSet);
                Instant instant = localDate.atStartOfDay().atZone(zoneId).toInstant();
                Date d = Date.from(instant);
                return new DateValue(d);
            } else {
                return new NumericValue(localDate.get(IsoFields.DAY_OF_QUARTER));
            }
        } else {
            return new EmptyValue();
        }
    }

    public int getParameterCount() {
        return -1;
    }
}
