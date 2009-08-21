package com.easyinsight.analysis;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;

/**
 * User: James Boe
 * Date: Oct 28, 2008
 * Time: 11:36:23 AM
 */
@Entity
@Table(name="rolling_range_filter")
public class PersistableRollingFilterDefinition extends PersistableFilterDefinition {

    @Column(name="interval_value")
    private int interval;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public FilterDefinition toFilterDefinition() {
        RollingFilterDefinition rollingFilterDefinition = new RollingFilterDefinition();
        rollingFilterDefinition.setEnabled(isEnabled());
        rollingFilterDefinition.setFilterID(getFilterId());
        rollingFilterDefinition.setField(getField());
        rollingFilterDefinition.setApplyBeforeAggregation(isApplyBeforeAggregation());
        rollingFilterDefinition.setInterval(interval);
        rollingFilterDefinition.setIntrinsic(isIntrinsic());
        return rollingFilterDefinition;
    }
}
