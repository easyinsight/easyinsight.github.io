package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;
import com.easyinsight.logging.LogClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.util.Calendar;
import java.util.Date;

/**
 * User: jamesboe
 * Date: 7/8/12
 * Time: 10:47 AM
 */
public class ParseDate extends Function {
    public Value evaluate() {
        ZoneId zoneId = calculationMetadata.getInsightRequestMetadata().createZoneID();
        try {
            Value start = params.get(0);
            String string = start.toString();
            String value;
            if (string.startsWith("\"")) {
                value = minusQuotes(0);
            } else {
                value = string;
            }
            String format = minusQuotes(1);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format).withZone(zoneId);
            TemporalAccessor ta = dateTimeFormatter.parse(value);
            /*SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(value);*/
            int dayOfMonth = ta.get(ChronoField.DAY_OF_MONTH);
            int month = ta.get(ChronoField.MONTH_OF_YEAR);
            int year = ta.get(ChronoField.YEAR);

            LocalDate localDate = LocalDate.of(year, month, dayOfMonth);
            Date date = Date.from(localDate.atStartOfDay(zoneId).toInstant());
            DateValue dateValue = new DateValue(date);
            System.out.println("local date = " + localDate);
            dateValue.setLocalDate(localDate);
            return dateValue;
        } catch (Exception e) {
            LogClass.error(e);
            return new EmptyValue();
        }
    }

    public int getParameterCount() {
        return 2;
    }
}
