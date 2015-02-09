package com.easyinsight.analysis;

import com.easyinsight.core.Value;
import com.easyinsight.core.DateValue;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

/**
 * User: James Boe
 * Date: Jul 8, 2008
 * Time: 2:59:25 PM
 */
public class MaterializedFilterDateRangeDefinition extends MaterializedFilterDefinition {

    private Temporal lowValue;
    private Temporal highValue;
    private boolean dateTime;

    public MaterializedFilterDateRangeDefinition(AnalysisItem key, Temporal lowValue, Temporal highValue, FilterDateRangeDefinition filter, InsightRequestMetadata insightRequestMetadata) {
        super(key);
        this.lowValue = lowValue;
        this.highValue = highValue;
        dateTime = filter.dateTime(insightRequestMetadata);
    }

    public boolean allows(Value value) {
        boolean allowed = false;
        DateValue dateValue = findDateValue(value);
        if (dateValue != null) {
            if (dateTime) {
                ZonedDateTime zdt = dateValue.getZonedDateTime();
                allowed = (zdt.isEqual((java.time.chrono.ChronoZonedDateTime<?>) lowValue) || zdt.isAfter((java.time.chrono.ChronoZonedDateTime<?>) lowValue)) &&
                        (zdt.isEqual((java.time.chrono.ChronoZonedDateTime<?>) highValue) || zdt.isBefore((java.time.chrono.ChronoZonedDateTime<?>) highValue));
            } else {
                LocalDate localDate = dateValue.getLocalDate();
                allowed = (localDate.isEqual((java.time.chrono.ChronoLocalDate) lowValue) || localDate.isAfter((java.time.chrono.ChronoLocalDate) lowValue)) &&
                        (localDate.isEqual((java.time.chrono.ChronoLocalDate) highValue) || localDate.isBefore((java.time.chrono.ChronoLocalDate) highValue));
            }
            System.out.println(dateValue.getLocalDate() + " - " + allowed);
        }
        return allowed;
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

    @Override
    public void log(InsightRequestMetadata insightRequestMetadata, FilterDefinition filterDefinition) {
        insightRequestMetadata.addAudit(filterDefinition, "Start date on processing in memory is " + (((AnalysisDateDimension) filterDefinition.getField()).isTimeshift(insightRequestMetadata) ? " time shifted " : " not time shifted ") + " at query to " +  lowValue);
        insightRequestMetadata.addAudit(filterDefinition, "End date on processing in memory is " + (((AnalysisDateDimension) filterDefinition.getField()).isTimeshift(insightRequestMetadata) ? " time shifted " : " not time shifted ") + " at query to " +  highValue);
    }
}
