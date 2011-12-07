package com.easyinsight.calculations;

import com.easyinsight.core.*;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        DateValue dateValue = (DateValue) value;
        Date date = dateValue.getDate();
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
