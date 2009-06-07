package com.easyinsight.analysis;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
        filter.setFilterId(getFilterID());
        filter.setField(getField());
        filter.setApplyBeforeAggregation(isApplyBeforeAggregation());
        filter.setLimit(limit);
        return filter;
    }

    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return new MaterializedLastNFilterDefinition(getField(), limit);
    }

    public String toQuerySQL(String tableName) {
        // TODO: implement
        StringBuilder queryBuilder = new StringBuilder();
        return queryBuilder.toString();
    }

    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        // TODO: implement
        return start;
    }
}
