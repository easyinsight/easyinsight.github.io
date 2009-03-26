package com.easyinsight.analysis;

import com.easyinsight.core.PersistableValue;
import com.easyinsight.core.PersistableStringValue;
import com.easyinsight.core.Value;

import javax.persistence.*;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Jul 8, 2008
 * Time: 2:58:38 PM
 */
@Entity
@Table(name="value_based_filter")
@PrimaryKeyJoinColumn(name="filter_id")
public class PersistableValueFilterDefinition extends PersistableFilterDefinition {
    @Column(name="inclusive")
    private boolean inclusive;
    @OneToMany(cascade= CascadeType.ALL)
    @JoinTable(name="filter_to_value",
               joinColumns=@JoinColumn(name="filter_id"),
               inverseJoinColumns=@JoinColumn(name="value_id"))
    private Set<PersistableValue> filterValues;

    public Set<PersistableValue> getFilterValues() {
        return filterValues;
    }

    public void setFilterValues(Set<PersistableValue> filterValues) {
        this.filterValues = filterValues;
    }

    public boolean isInclusive() {
        return inclusive;
    }

    public void setInclusive(boolean inclusive) {
        this.inclusive = inclusive;
    }

    public FilterDefinition toFilterDefinition() {
        FilterValueDefinition filterDefinition = new FilterValueDefinition();
        filterDefinition.setField(getField());
        filterDefinition.setInclusive(inclusive);
        List<Object> values = new ArrayList<Object>();
        for (PersistableValue filterDefinitionValue : filterValues) {
            values.add(filterDefinitionValue.toValue());
        }
        filterDefinition.setFilteredValues(values);
        return filterDefinition;
    }

    public void resetIDs() {
        super.resetIDs();
        for (PersistableValue persistableValue : filterValues) {
            persistableValue.setValueID(0);
        }
    }
}
