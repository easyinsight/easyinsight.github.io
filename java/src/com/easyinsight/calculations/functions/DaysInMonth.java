package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * User: jamesboe
 * Date: 12/29/11
 * Time: 1:05 PM
 */
public class DaysInMonth extends Function {
    public Value evaluate() {
        LocalDate startDate = date();
        if (startDate != null) {
            return new NumericValue(startDate.lengthOfMonth());
        } else {
            return new EmptyValue();
        }
    }

    public int getParameterCount() {
        return -1;
    }
}
