package com.easyinsight.analysis;

import com.easyinsight.AnalysisItem;
import com.easyinsight.MaterializedFilterDefinition;

import java.io.Serializable;

/**
 * User: James Boe
 * Date: Jan 12, 2008
 * Time: 9:47:18 PM
 */
public abstract class FilterDefinition implements Serializable {
    private AnalysisItem field;
    private boolean applyBeforeAggregation = true;

    public FilterDefinition() {
    }

    public FilterDefinition(AnalysisItem field) {
        this.field = field;
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
}
