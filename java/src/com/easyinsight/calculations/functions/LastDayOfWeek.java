package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;
import com.easyinsight.security.SecurityUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

/**
 * User: jamesboe
 * Date: 9/7/14
 * Time: 12:27 PM
 */
public class LastDayOfWeek extends Function {
    @Override
    public Value evaluate() {
        int firstDayOfWeek = SecurityUtil.getFirstDayOfWeek();
        java.time.DayOfWeek dayOfWeek = LocalDate.now().with(ChronoField.DAY_OF_WEEK, firstDayOfWeek).minusDays(1).getDayOfWeek();
        return new NumericValue(dayOfWeek.getValue());
    }

    @Override
    public int getParameterCount() {
        return 0;
    }
}
