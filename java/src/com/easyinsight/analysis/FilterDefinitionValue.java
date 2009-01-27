package com.easyinsight.analysis;

import javax.persistence.*;

/**
 * User: James Boe
 * Date: Apr 12, 2008
 * Time: 10:18:32 PM
 */
@Entity
@Table(name="filter_value")
public class FilterDefinitionValue {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="filter_value_id")
    private long filterDefinitionValueID;
    @Column(name="filter_value")
    private String value;

    public FilterDefinitionValue() {
    }

    public FilterDefinitionValue(String value) {
        this.value = value;
    }

    public long getFilterDefinitionValueID() {
        return filterDefinitionValueID;
    }

    public void setFilterDefinitionValueID(long filterDefinitionValueID) {
        this.filterDefinitionValueID = filterDefinitionValueID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
