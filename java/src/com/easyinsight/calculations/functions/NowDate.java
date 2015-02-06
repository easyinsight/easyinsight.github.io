package com.easyinsight.calculations.functions;


import com.easyinsight.calculations.Function;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * User: jamesboe
 * Date: Jul 21, 2010
 * Time: 10:00:11 AM
 */
public class NowDate extends Function {
    public Value evaluate() {
        ZoneId zoneId = calculationMetadata.getInsightRequestMetadata().createZoneID();
        LocalDate startDate = LocalDate.now();
        Date date = Date.from(startDate.atStartOfDay().atZone(zoneId).toInstant());
        DateValue dateValue = new DateValue(date);
        dateValue.setLocalDate(startDate);
        return dateValue;
        //return new DateValue(cal.getTime());
    }

    public int getParameterCount() {
        return 0;
    }
}
