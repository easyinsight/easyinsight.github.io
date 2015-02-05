package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.IsoFields;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * User: jamesboe
 * Date: 12/29/11
 * Time: 1:15 PM
 */
public class DayOfYear extends Function {
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
            ZoneId zoneId = calculationMetadata.getInsightRequestMetadata().createZoneID();
            Instant instant1 = startDate.toInstant();
            LocalDate localDate = instant1.atZone(zoneId).toLocalDate();

            if (params.size() == 2) {
                int dayToSet = params.get(1).toDouble().intValue();
                localDate = localDate.withDayOfYear(dayToSet);
                Instant instant = localDate.atStartOfDay().atZone(zoneId).toInstant();
                return new DateValue(Date.from(instant));
            } else {
                return new NumericValue(localDate.getDayOfYear());
            }
        } else {
            return new EmptyValue();
        }
    }

    public int getParameterCount() {
        return -1;
    }
}
