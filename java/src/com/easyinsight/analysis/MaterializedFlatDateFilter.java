package com.easyinsight.analysis;

import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;

import java.util.Calendar;

/**
 * User: jamesboe
 * Date: Sep 12, 2010
 * Time: 6:41:07 PM
 */
public class MaterializedFlatDateFilter extends MaterializedFilterDefinition {

    private int type;
    private int value;

    public MaterializedFlatDateFilter(AnalysisItem key, int type, int value) {
        super(key);
        this.type = type;
        this.value = value;
    }

    @Override
    public boolean allows(Value value) {
        if (value.type() == Value.DATE) {
            DateValue dateValue = (DateValue) value;
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateValue.getDate());
            if (type == AnalysisDateDimension.YEAR_LEVEL) {
                int year = cal.get(Calendar.YEAR);
                return year == this.value;
            } else if (type == AnalysisDateDimension.MONTH_LEVEL) {
                int month = cal.get(Calendar.MONTH);
                return month == this.value;
            }
        } else if (value.type() == Value.STRING) {
            Value originalValue = value.getOriginalValue();
            if (originalValue.type() == Value.DATE) {
                DateValue dateValue = (DateValue) originalValue;
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateValue.getDate());
                if (type == AnalysisDateDimension.YEAR_LEVEL) {
                    int year = cal.get(Calendar.YEAR);
                    return year == this.value;
                } else if (type == AnalysisDateDimension.MONTH_LEVEL) {
                    int month = cal.get(Calendar.MONTH);
                    return month == this.value;
                }
            }
        }
        return false;
    }
}
