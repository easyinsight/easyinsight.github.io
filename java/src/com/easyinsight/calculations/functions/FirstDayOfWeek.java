package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;
import com.easyinsight.security.SecurityUtil;

/**
 * User: jamesboe
 * Date: 9/7/14
 * Time: 12:27 PM
 */
public class FirstDayOfWeek extends Function {
    @Override
    public Value evaluate() {
        int day = SecurityUtil.getFirstDayOfWeek();
        return new NumericValue(day);
    }

    @Override
    public int getParameterCount() {
        return 0;
    }
}
