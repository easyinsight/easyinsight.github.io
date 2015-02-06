package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;
import com.easyinsight.security.SecurityUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

/**
 * User: jamesboe
 * Date: 12/29/11
 * Time: 1:15 PM
 */
public class WeekOfYear extends Function {
    public Value evaluate() {
        LocalDate startDate = date();
        if (startDate != null) {
            int weekOfYear = startDate.with(ChronoField.DAY_OF_WEEK, DayOfWeek.translateDayOfWeek(SecurityUtil.getFirstDayOfWeek()).getValue()).get(ChronoField.ALIGNED_WEEK_OF_YEAR);
            return new NumericValue(weekOfYear);
        } else {
            return new EmptyValue();
        }
    }

    public int getParameterCount() {
        return -1;
    }
}
