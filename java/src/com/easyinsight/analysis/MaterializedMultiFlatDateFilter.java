package com.easyinsight.analysis;

import com.easyinsight.calculations.functions.DayOfQuarter;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;
import org.jetbrains.annotations.Nullable;

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

    private Set<Integer> months;
    private Set<String> valids;
    private int level;

    public MaterializedMultiFlatDateFilter(AnalysisItem key, Collection<DateLevelWrapper> wrappers, int level) {
        super(key);
        this.level = level;
        if (level == AnalysisDateDimension.MONTH_FLAT) {
            months = new HashSet<Integer>();
            for (DateLevelWrapper wrapper : wrappers) {
                months.add(wrapper.getDateLevel());
            }
        } else if (level == AnalysisDateDimension.QUARTER_OF_YEAR_LEVEL) {
            valids = new HashSet<>();
            for (DateLevelWrapper wrapper : wrappers) {
                if (wrapper.getDateLevel() == 0) {
                    valids.add("Q3-2014");
                } else if (wrapper.getDateLevel() == 1) {
                    valids.add("Q2-2014");
                }
            }
            for (DateLevelWrapper wrapper : wrappers) {
                valids.add(wrapper.getDisplay());
            }
        }
        System.out.println(valids);
    }

    @Override
    public boolean allows(Value value) {

        if (level == AnalysisDateDimension.MONTH_FLAT) {
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
        } else if (level == AnalysisDateDimension.QUARTER_OF_YEAR_LEVEL) {
            DateValue dateValue = findDateValue(value);
            if (dateValue != null) {
                // does the date value fall within
                int quarter = DayOfQuarter.quarter(dateValue.getDate()) + 1;
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateValue.getDate());
                int year = cal.get(Calendar.YEAR);
                /*String quarter = String.valueOf(value.toString().charAt(1));
                String year = value.toString().substring(value.toString().length() - 4);*/
                String result = "Q" + quarter + "-" + year;
                System.out.println(result);
                if (valids.contains(result)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Nullable
    private DateValue findDateValue(Value value) {
        if (value.type() == Value.DATE) {
            return (DateValue) value;
        } else if (value.type() == Value.STRING) {
            Value originalValue = value.getOriginalValue();
            if (originalValue.type() == Value.DATE) {
                return (DateValue) originalValue;

                /*Calendar cal = Calendar.getInstance();
                cal.setTime(dateValue.getDate());
                return months.contains(cal.get(Calendar.MONTH));*/
            }
        }
        return null;
    }
}
