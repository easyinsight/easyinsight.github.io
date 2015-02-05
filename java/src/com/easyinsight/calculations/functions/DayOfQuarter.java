package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

import java.time.*;
import java.time.temporal.IsoFields;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
            ZoneId zoneId = calculationMetadata.getInsightRequestMetadata().createZoneID();
            Instant instant1 = startDate.toInstant();
            LocalDate localDate = instant1.atZone(zoneId).toLocalDate();

            if (params.size() == 2) {
                int dayToSet = params.get(1).toDouble().intValue();
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
