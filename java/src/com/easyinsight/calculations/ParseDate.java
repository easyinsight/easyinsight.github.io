package com.easyinsight.calculations;

import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: jamesboe
 * Date: 7/8/12
 * Time: 10:47 AM
 */
public class ParseDate extends Function {
    public Value evaluate() {
        try {
            String value = params.get(0).toString();
            String format = minusQuotes(1);
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(value);
            return new DateValue(date);
        } catch (ParseException e) {
            return new EmptyValue();
        }
    }

    public int getParameterCount() {
        return 2;
    }
}
