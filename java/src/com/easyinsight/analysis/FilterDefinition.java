package com.easyinsight.analysis;

import com.easyinsight.database.Database;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * User: James Boe
 * Date: Jan 12, 2008
 * Time: 9:47:18 PM
 */
@Entity
@Table(name="filter")
@Inheritance(strategy= InheritanceType.JOINED)
public abstract class FilterDefinition implements Serializable, Cloneable {
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="analysis_item_id")
    private AnalysisItem field;
    @Column(name="apply_before_aggregation")
    private boolean applyBeforeAggregation = true;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="filter_id")
    private long filterID;
    @Column(name="intrinsic")
    private boolean intrinsic;
    @Column(name="enabled")
    private boolean enabled = true; 

    public FilterDefinition() {
    }

    public FilterDefinition(AnalysisItem field) {
        this.field = field;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isIntrinsic() {
        return intrinsic;
    }

    public void setIntrinsic(boolean intrinsic) {
        this.intrinsic = intrinsic;
    }

    public long getFilterID() {
        return filterID;
    }

    public void setFilterID(long filterID) {
        this.filterID = filterID;
    }

    public AnalysisItem getField() {
        return field;
    }

    public void setField(AnalysisItem field) {
        this.field = field;
    }

    public boolean isApplyBeforeAggregation() {
        return applyBeforeAggregation;
    }

    public void setApplyBeforeAggregation(boolean applyBeforeAggregation) {
        this.applyBeforeAggregation = applyBeforeAggregation;
    }

    public abstract MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata);

    public abstract String toQuerySQL(String tableName);

    public abstract int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException;

    public boolean validForQuery() {
        if (getField().hasType(AnalysisItemTypes.STEP)) {
            return false;
        }
        return enabled;
    }

    public FilterDefinition clone() throws CloneNotSupportedException {
        FilterDefinition filter = (FilterDefinition) super.clone();
        filter.setFilterID(0);
        return filter;
    }

    public void updateIDs(Map<Long, AnalysisItem> replacementMap) {
        setField(replacementMap.get(field.getAnalysisItemID()));
    }

    public void beforeSave() {

    }

    public void afterLoad() {
        setField((AnalysisItem) Database.deproxy(getField()));
        getField().afterLoad();
    }
}
