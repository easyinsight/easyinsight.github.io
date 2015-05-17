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
 * Time: 1:15 PM
 */
public class DayOfMonth extends Function {
    public Value evaluate() {
        ZoneId zoneId = calculationMetadata.getInsightRequestMetadata().createZoneID();
        LocalDate startDate = date();
        if (startDate != null) {

                // pathway enhanced network

                if (params.size() == 2) {
                    int day = params.get(1).toDouble().intValue();
                    if (day < 1) {
                        day = 1;
                    }
                    startDate = startDate.withDayOfMonth(day);
                    Date date = Date.from(startDate.atStartOfDay().atZone(zoneId).toInstant());
                    return new DateValue(date);
                } else {
                    return new NumericValue(startDate.getDayOfMonth());
                }

        } else {
            return new EmptyValue();
        }
    }

    public int getParameterCount() {
        return -1;
    }
}
