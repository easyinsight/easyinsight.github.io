package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.FilterDefinition;

import javax.persistence.*;

/**
 * User: jamesboe
 * Date: 9/26/11
 * Time: 11:32 AM
 */
@Entity
@Table(name="trend_column")
public class TrendColumn {

    @Id
    @Column(name="trend_column_id")
    private long trendColumnID;

    @OneToOne
    @JoinColumn(name="filter_id")
    private FilterDefinition filterDefinition;
    @Column(name="column_label")
    private String label;

    public long getTrendColumnID() {
        return trendColumnID;
    }

    public void setTrendColumnID(long trendColumnID) {
        this.trendColumnID = trendColumnID;
    }

    public FilterDefinition getFilterDefinition() {
        return filterDefinition;
    }

    public void setFilterDefinition(FilterDefinition filterDefinition) {
        this.filterDefinition = filterDefinition;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
