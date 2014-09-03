package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.calculations.FunctionException;
import com.easyinsight.calculations.IFunction;
import com.easyinsight.core.*;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * User: jamesboe
 * Date: 12/7/11
 * Time: 11:10 AM
 */
public class EIDateFormatFunction extends Function implements IFunction {
    public Value evaluate() {
        Value value = getParameter(0);
        if (value.type() == Value.EMPTY) {
            return value;
        }
        Date date;
        if (value.type() == Value.DATE) {
            DateValue dateValue = (DateValue) value;
            date = dateValue.getDate();
        } else if (value.type() == Value.NUMBER) {
            date = new Date(value.toDouble().longValue());
        } else {
            throw new FunctionException("We couldn't parse the value of " + value.toString() + " as a date.");
        }

        String formatString = minusQuotes(getParameter(1)).toString();
        if (formatString.startsWith("QQ")) {
            formatString = "QQ-yyyy";
        } else if ("qq".equals(formatString)) {
            formatString = "QQ";
        }

        if (calculationMetadata.getInsightRequestMetadata() != null) {
            Instant instant = date.toInstant();
            ZoneId zoneId = ZoneId.ofOffset("", ZoneOffset.ofHours(-(calculationMetadata.getInsightRequestMetadata().getUtcOffset() / 60)));
            ZonedDateTime zdt = instant.atZone(zoneId);
            return new StringValue(DateTimeFormatter.ofPattern(formatString).format(zdt));
        } else {
            Instant instant = date.toInstant();
            ZoneId zoneId = ZoneId.of("UTC");
            ZonedDateTime zdt = instant.atZone(zoneId);
            return new StringValue(DateTimeFormatter.ofPattern(formatString).format(zdt));
        }
    }

    @Override
    public boolean onDemand() {
        return true;
    }

    public int getParameterCount() {
        return 2;
    }
}
