package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.calculations.FunctionException;
import com.easyinsight.calculations.IFunction;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.StringValue;
import com.easyinsight.core.Value;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
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
public class EIDateFormatNoShiftFunction extends Function implements IFunction {
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

        String string;
        String formatString = minusQuotes(getParameter(1)).toString();
        if (formatString.startsWith("QQ")) {
            formatString = "QQ-yyyy";
        } else if ("qq".equals(formatString)) {
            formatString = "QQ";
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatString);
        Instant instant = date.toInstant();
        ZonedDateTime zdt = instant.atZone(ZoneId.of("UTC"));
        string = dateTimeFormatter.format(zdt);
        System.out.println(string);


        if (calculationMetadata.getInsightRequestMetadata() != null && calculationMetadata.getInsightRequestMetadata().isLogReport()) {
            System.out.println("Translated " + date + " to " + string);
        }
        return new StringValue(string);
    }

    @Override
    public boolean onDemand() {
        return true;
    }

    public int getParameterCount() {
        return 2;
    }
}
