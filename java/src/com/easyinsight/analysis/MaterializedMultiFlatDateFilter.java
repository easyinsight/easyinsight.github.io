package com.easyinsight.analysis;

import com.easyinsight.calculations.functions.DayOfQuarter;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
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
    private SimpleDateFormat sdf;

    public MaterializedMultiFlatDateFilter(AnalysisItem key, Collection<DateLevelWrapper> wrappers, int level) {
        super(key);
        this.level = level;
        if (level == AnalysisDateDimension.MONTH_FLAT) {
            months = new HashSet<Integer>();
            for (DateLevelWrapper wrapper : wrappers) {
                months.add(wrapper.getDateLevel());
            }
        } else if (level == AnalysisDateDimension.QUARTER_OF_YEAR_LEVEL || level == AnalysisDateDimension.WEEK_LEVEL ||
                level == AnalysisDateDimension.MONTH_LEVEL || level == AnalysisDateDimension.YEAR_LEVEL) {
            valids = new HashSet<>();
            for (DateLevelWrapper wrapper : wrappers) {
                valids.add(wrapper.getShortDisplay());
            }
            if (level == AnalysisDateDimension.YEAR_LEVEL) {
                sdf = new SimpleDateFormat("yyyy");
            } else if (level == AnalysisDateDimension.MONTH_LEVEL) {
                sdf = new SimpleDateFormat("yyyy-MM");
            } else if (level == AnalysisDateDimension.WEEK_LEVEL) {
                sdf = new SimpleDateFormat("yyyy-ww");
            }
        }
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
                if (valids.contains(result)) {
                    return true;
                }
            }
        } else if (level == AnalysisDateDimension.YEAR_LEVEL || level == AnalysisDateDimension.MONTH_LEVEL ||
                level == AnalysisDateDimension.WEEK_LEVEL) {
            DateValue dateValue = findDateValue(value);
            if (dateValue != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateValue.getDate());
                String result = sdf.format(cal.getTime());
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
