package com.easyinsight.analysis;

import com.easyinsight.analysis.MaterializedFilterDefinition;

import java.util.Date;

/**
 * User: James Boe
 * Date: Jul 8, 2008
 * Time: 2:24:20 PM
 */
public class FilterDateRangeDefinition extends FilterDefinition {
    private Date startDate;
    private Date endDate;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public PersistableFilterDefinition toPersistableFilterDefinition() {
        PersistableDateRangeFilterDefinition date = new PersistableDateRangeFilterDefinition();
        date.setApplyBeforeAggregation(isApplyBeforeAggregation());
        date.setField(getField());
        date.setLowDate(getStartDate());
        date.setHighDate(getEndDate());
        return date;
    }

    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return new MaterializedFilterDateRangeDefinition(getField(), startDate, endDate);
    }
}
