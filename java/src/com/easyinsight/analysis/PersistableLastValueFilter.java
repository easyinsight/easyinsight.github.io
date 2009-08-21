package com.easyinsight.analysis;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: jamesboe
 * Date: Aug 5, 2009
 * Time: 12:53:10 PM
 */
@Entity
@Table(name="last_value_filter")
public class PersistableLastValueFilter extends PersistableFilterDefinition {
    public FilterDefinition toFilterDefinition() {
        LastValueFilter lastValueFilter = new LastValueFilter();
        lastValueFilter.setEnabled(isEnabled());
        lastValueFilter.setApplyBeforeAggregation(isApplyBeforeAggregation());
        lastValueFilter.setField(getField());
        lastValueFilter.setFilterID(getFilterId());
        lastValueFilter.setIntrinsic(isIntrinsic());
        return lastValueFilter;
    }
}
