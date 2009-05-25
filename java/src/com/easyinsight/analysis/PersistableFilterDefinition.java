package com.easyinsight.analysis;

import com.easyinsight.analysis.AnalysisItem;

import javax.persistence.*;
import java.util.Map;

/**
 * User: James Boe
 * Date: Apr 16, 2008
 * Time: 1:39:51 PM
 */
@Entity
@Table(name="filter")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class PersistableFilterDefinition implements Cloneable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="filter_id")
    private long filterId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="analysis_item_id")
    private AnalysisItem field;

    @Column(name="apply_before_aggregation")
    private boolean applyBeforeAggregation;

    public PersistableFilterDefinition clone() throws CloneNotSupportedException {
        PersistableFilterDefinition filter = (PersistableFilterDefinition) super.clone();
        filter.setFilterId(0);
        return filter;
    }

    public boolean isApplyBeforeAggregation() {
        return applyBeforeAggregation;
    }

    public void setApplyBeforeAggregation(boolean applyBeforeAggregation) {
        this.applyBeforeAggregation = applyBeforeAggregation;
    }

    public long getFilterId() {
        return filterId;
    }

    public void setFilterId(long filterId) {
        this.filterId = filterId;
    }

    public AnalysisItem getField() {
        return field;
    }

    public void setField(AnalysisItem field) {
        this.field = field;
    }

    public abstract FilterDefinition toFilterDefinition();

    public void resetIDs() {
        this.filterId = 0;
    }

    public void updateIDs(Map<Long, AnalysisItem> replacementMap) {
        setField(replacementMap.get(field.getAnalysisItemID()));
    }
}
