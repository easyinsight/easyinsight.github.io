package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.calculations.FunctionException;
import com.easyinsight.calculations.IFunction;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.StringValue;
import com.easyinsight.core.Value;

import java.text.SimpleDateFormat;
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

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(minusQuotes(getParameter(1)).toString());
        return new StringValue(simpleDateFormat.format(date));
    }

    @Override
    public boolean onDemand() {
        return true;
    }

    public int getParameterCount() {
        return 2;
    }
}
