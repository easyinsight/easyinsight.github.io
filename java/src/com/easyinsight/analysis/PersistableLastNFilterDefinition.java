package com.easyinsight.analysis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: James Boe
 * Date: Feb 27, 2009
 * Time: 8:50:06 AM
 */
@Entity
@Table(name="last_n_filter")
public class PersistableLastNFilterDefinition extends PersistableFilterDefinition {
    @Column(name="result_limit")
    private int limit;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public FilterDefinition toFilterDefinition() {
        LastNFilterDefinition filterDefinition = new LastNFilterDefinition();
        filterDefinition.setFilterID(getFilterId());
        filterDefinition.setField(getField());
        filterDefinition.setApplyBeforeAggregation(isApplyBeforeAggregation());
        filterDefinition.setLimit(limit);
        filterDefinition.setIntrinsic(isIntrinsic());
        return filterDefinition;
    }
}
