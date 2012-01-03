package com.easyinsight.calculations;

import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;
import com.easyinsight.security.SecurityUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * User: jamesboe
 * Date: 12/29/11
 * Time: 1:15 PM
 */
public class DayOfWeek extends Function {
    public Value evaluate() {
        Date startDate = null;
        if (params.size() == 0) {
            startDate = new Date();
        } else {
            Value start = params.get(0);
            if (start.type() == Value.DATE) {
                DateValue dateValue = (DateValue) start;
                startDate = dateValue.getDate();
            }
        }
        if (startDate != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            cal.setFirstDayOfWeek(SecurityUtil.getFirstDayOfWeek());
            return new NumericValue(cal.get(Calendar.DAY_OF_WEEK));
        } else {
            return new EmptyValue();
        }
    }

    public int getParameterCount() {
        return -1;
    }
}
