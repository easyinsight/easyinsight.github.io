package com.easyinsight.calculations;

import com.easyinsight.core.DateValue;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;

import java.util.Calendar;
import java.util.Date;

/**
 * User: jamesboe
 * Date: 12/29/11
 * Time: 1:14 PM
 */
public class Month extends Function {
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
            return new NumericValue(cal.get(Calendar.MONTH));
        } else {
            return new EmptyValue();
        }
    }

    public int getParameterCount() {
        return -1;
    }
}
