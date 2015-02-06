package com.easyinsight.analysis;

import com.easyinsight.calculations.functions.DayOfQuarter;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;
import org.jetbrains.annotations.Nullable;

import java.time.format.DateTimeFormatter;
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
    private DateTimeFormatter sdf;
    private boolean dateTime;

    public MaterializedMultiFlatDateFilter(AnalysisItem key, Collection<DateLevelWrapper> wrappers, int level, InsightRequestMetadata insightRequestMetadata) {
        super(key);
        AnalysisDateDimension date = (AnalysisDateDimension) key;
        dateTime = (date.isTimeshift(insightRequestMetadata));
        this.level = level;
        if (level == AnalysisDateDimension.MONTH_FLAT) {
            months = new HashSet<>();
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
                sdf = DateTimeFormatter.ofPattern("yyyy");
            } else if (level == AnalysisDateDimension.MONTH_LEVEL) {
                sdf = DateTimeFormatter.ofPattern("yyyy-MM");
            } else if (level == AnalysisDateDimension.WEEK_LEVEL) {
                sdf = DateTimeFormatter.ofPattern("yyyy-ww");
            }
        }
    }

    @Override
    public boolean allows(Value value) {
        if (level == AnalysisDateDimension.MONTH_FLAT) {
            if (dateTime) {
                if (value.type() == Value.DATE) {
                    DateValue dateValue = (DateValue) value;
                    int month = dateValue.getZonedDateTime().getMonthValue() - 1;
                    return months.contains(month);
                } else if (value.type() == Value.STRING) {
                    Value originalValue = value.getOriginalValue();
                    if (originalValue.type() == Value.DATE) {
                        DateValue dateValue = (DateValue) originalValue;
                        int month = dateValue.getZonedDateTime().getMonthValue() - 1;
                        return months.contains(month);
                    }
                }
            } else {
                if (value.type() == Value.DATE) {
                    DateValue dateValue = (DateValue) value;
                    int month = dateValue.getLocalDate().getMonthValue() - 1;
                    return months.contains(month);
                } else if (value.type() == Value.STRING) {
                    Value originalValue = value.getOriginalValue();
                    if (originalValue.type() == Value.DATE) {
                        DateValue dateValue = (DateValue) originalValue;
                        int month = dateValue.getLocalDate().getMonthValue() - 1;
                        return months.contains(month);
                    }
                }
            }
        } else if (level == AnalysisDateDimension.QUARTER_OF_YEAR_LEVEL) {
            DateValue dateValue = findDateValue(value);
            if (dateValue != null) {
                String result;
                if (dateTime) {
                    int quarter = DayOfQuarter.quarter(dateValue.getZonedDateTime()) + 1;
                    result = "Q" + quarter + "-" + dateValue.getZonedDateTime().getYear();
                } else {
                    int quarter = DayOfQuarter.quarter(dateValue.getLocalDate()) + 1;
                    result = "Q" + quarter + "-" + dateValue.getLocalDate().getYear();
                }
                if (valids.contains(result)) {
                    return true;
                }
            }
        } else if (level == AnalysisDateDimension.YEAR_LEVEL || level == AnalysisDateDimension.MONTH_LEVEL ||
                level == AnalysisDateDimension.WEEK_LEVEL) {
            DateValue dateValue = findDateValue(value);
            if (dateValue != null) {
                String result;
                if (dateTime) {
                    result = sdf.format(dateValue.getZonedDateTime());
                } else {
                    result = sdf.format(dateValue.getLocalDate());
                }
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
            }
        }
        return null;
    }
}
