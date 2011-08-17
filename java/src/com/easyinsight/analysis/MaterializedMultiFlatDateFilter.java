package com.easyinsight.analysis;

import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * User: jamesboe
 * Date: Sep 12, 2010
 * Time: 6:41:07 PM
 */
public class MaterializedMultiFlatDateFilter extends MaterializedFilterDefinition {

    private int type;
    private int value;

    private Set<Integer> months;

    public MaterializedMultiFlatDateFilter(AnalysisItem key, Collection<DateLevelWrapper> wrappers) {
        super(key);
        months = new HashSet<Integer>();
        for (DateLevelWrapper wrapper : wrappers) {
            months.add(wrapper.getDateLevel());
        }
    }

    @Override
    public boolean allows(Value value) {
        if (value.type() == Value.DATE) {
            DateValue dateValue = (DateValue) value;
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateValue.getDate());
            return months.contains(cal.get(Calendar.MONTH));
        } else if (value.type() == Value.STRING) {
            Value originalValue = value.getOriginalValue();
            if (originalValue.type() == Value.DATE) {
                DateValue dateValue = (DateValue) originalValue;
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateValue.getDate());
                return months.contains(cal.get(Calendar.MONTH));
            }
        }
        return false;
    }
}
