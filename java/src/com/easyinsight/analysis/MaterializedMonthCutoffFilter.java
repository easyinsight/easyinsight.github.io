package com.easyinsight.analysis;

import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;

import java.util.Calendar;

/**
 * User: jamesboe
 * Date: Sep 12, 2010
 * Time: 6:41:07 PM
 */
public class MaterializedMonthCutoffFilter extends MaterializedFilterDefinition {

    private int month;

    public MaterializedMonthCutoffFilter(AnalysisItem key, int month) {
        super(key);
        this.month = month;
    }

    @Override
    public boolean allows(Value value) {
        if (value.type() == Value.DATE) {
            DateValue dateValue = (DateValue) value;
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateValue.getDate());
            int month = cal.get(Calendar.MONTH);
            return month <= this.month;
        } else if (value.type() == Value.STRING) {
            Value originalValue = value.getOriginalValue();
            if (originalValue.type() == Value.DATE) {
                DateValue dateValue = (DateValue) originalValue;
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateValue.getDate());
                int month = cal.get(Calendar.MONTH);
                return month <= this.month;
            }
        }
        return false;
    }
}
