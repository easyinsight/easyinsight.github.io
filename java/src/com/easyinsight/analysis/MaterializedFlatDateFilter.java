package com.easyinsight.analysis;

import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;
import org.jetbrains.annotations.Nullable;

/**
 * User: jamesboe
 * Date: Sep 12, 2010
 * Time: 6:41:07 PM
 */
public class MaterializedFlatDateFilter extends MaterializedFilterDefinition {

    private int type;
    private int value;
    private boolean dateTime;

    public MaterializedFlatDateFilter(AnalysisItem key, int type, int value, InsightRequestMetadata insightRequestMetadata) {
        super(key);
        this.type = type;
        this.value = value;
        AnalysisDateDimension date = (AnalysisDateDimension) key;
        dateTime = (date.isTimeshift(insightRequestMetadata));
    }

    @Override
    public boolean allows(Value value) {
        if (type != AnalysisDateDimension.MONTH_LEVEL && this.value == 0) {
            return true;
        }
        DateValue dateValue = findDateValue(value);
        if (dateValue != null) {
            if (type == AnalysisDateDimension.YEAR_LEVEL) {
                int year;
                if (dateTime) {
                    year = dateValue.getZonedDateTime().getYear();
                } else {
                    year = dateValue.getLocalDate().getYear();
                }
                return year == this.value;
            } else if (type == AnalysisDateDimension.MONTH_LEVEL) {
                int month;
                if (dateTime) {
                    month = dateValue.getZonedDateTime().getMonthValue() - 1;
                } else {
                    month = dateValue.getLocalDate().getMonthValue() - 1;
                }
                return month == this.value;
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
