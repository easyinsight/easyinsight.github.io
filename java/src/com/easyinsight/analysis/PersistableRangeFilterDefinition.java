package com.easyinsight.analysis;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;

/**
 * User: James Boe
 * Date: Jul 8, 2008
 * Time: 2:58:49 PM
 */
@Entity
@Table(name="range_filter")
@PrimaryKeyJoinColumn(name="filter_id")
public class PersistableRangeFilterDefinition extends PersistableFilterDefinition {

    @Column(name="low_value", nullable = true)
    private Double lowValue;
    @Column(name="high_value", nullable = true)
    private Double highValue;

    public Double getLowValue() {
        return lowValue;
    }

    public void setLowValue(Double lowValue) {
        this.lowValue = lowValue;
    }

    public Double getHighValue() {
        return highValue;
    }

    public void setHighValue(Double highValue) {
        this.highValue = highValue;
    }

    public FilterDefinition toFilterDefinition() {
        FilterRangeDefinition range = new FilterRangeDefinition();
        range.setFilterID(getFilterId());
        range.setField(getField());
        range.setStartValue(lowValue != null ? lowValue : 0);
        range.setStartValueDefined(lowValue != null);
        range.setEndValue(highValue != null ? highValue : 0);
        range.setEndValueDefined(highValue != null);
        range.setIntrinsic(isIntrinsic());
        return range;
    }
}
