package com.easyinsight.analysis;

import javax.persistence.*;

/**
 * User: jamesboe
 * Date: 2/11/13
 * Time: 9:27 AM
 */
@Entity
@Table(name="filter_set_stub")
public class FilterSetStub implements Cloneable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="filter_set_stub_id")
    private long filterSetStubID;

    @Column(name="filter_set_id")
    private Long filterSetID;

    public Long getFilterSetID() {
        return filterSetID;
    }

    public void setFilterSetID(Long filterSetID) {
        this.filterSetID = filterSetID;
    }

    public long getFilterSetStubID() {
        return filterSetStubID;
    }

    public void setFilterSetStubID(long filterSetStubID) {
        this.filterSetStubID = filterSetStubID;
    }

    public FilterSetStub clone() throws CloneNotSupportedException {
        FilterSetStub clone = (FilterSetStub) super.clone();
        clone.setFilterSetStubID(0);
        return clone;
    }
}
