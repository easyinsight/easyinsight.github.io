package com.easyinsight.analysis;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;
import java.util.Date;

/**
 * User: James Boe
 * Date: Jul 8, 2008
 * Time: 2:58:59 PM
 */
@Entity
@Table(name="date_range_filter")
@PrimaryKeyJoinColumn(name="filter_id")
public class PersistableDateRangeFilterDefinition extends PersistableFilterDefinition {

    @Column(name="low_value")
    private Date lowDate;

    @Column(name="high_value")
    private Date highDate;

    public Date getLowDate() {
        return lowDate;
    }

    public void setLowDate(Date lowDate) {
        this.lowDate = lowDate;
    }

    public Date getHighDate() {
        return highDate;
    }

    public void setHighDate(Date highDate) {
        this.highDate = highDate;
    }

    public FilterDefinition toFilterDefinition() {
        FilterDateRangeDefinition date = new FilterDateRangeDefinition();
        date.setFilterID(getFilterId());
        date.setField(getField());
        date.setStartDate(lowDate);
        date.setEndDate(highDate);
        return date;
    }
}
