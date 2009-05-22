package com.easyinsight.analysis;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Jan 12, 2008
 * Time: 9:47:18 PM
 */
public abstract class FilterDefinition implements Serializable {
    private AnalysisItem field;
    private boolean applyBeforeAggregation = true;
    private long filterID;

    public FilterDefinition() {
    }

    public FilterDefinition(AnalysisItem field) {
        this.field = field;
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

    public abstract PersistableFilterDefinition toPersistableFilterDefinition();

    public abstract MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata);

    public abstract String toQuerySQL();

    public abstract int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException;

    public boolean validForQuery() {
        return true;
    }
}
