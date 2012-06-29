package com.easyinsight.calculations;

import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;

import java.util.Date;

/**
 * User: jamesboe
 * Date: 5/7/12
 * Time: 10:25 AM
 */
public class DateFunction extends Function {
    public Value evaluate() {
        long ms = params.get(0).toDouble().longValue();
        if (ms == 0) {
            return new EmptyValue();
        } else {
            return new DateValue(new Date(ms));
        }
    }

    public int getParameterCount() {
        return 1;
    }
}
