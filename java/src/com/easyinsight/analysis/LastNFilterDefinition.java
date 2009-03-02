package com.easyinsight.analysis;

/**
 * User: James Boe
 * Date: Feb 27, 2009
 * Time: 8:30:42 AM
 */
public class LastNFilterDefinition extends FilterDefinition {

    private int limit;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public PersistableFilterDefinition toPersistableFilterDefinition() {
        PersistableLastNFilterDefinition filter = new PersistableLastNFilterDefinition();
        filter.setField(getField());
        filter.setApplyBeforeAggregation(isApplyBeforeAggregation());
        filter.setLimit(limit);
        return filter;
    }

    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return new MaterializedLastNFilterDefinition(getField(), limit);
    }
}
