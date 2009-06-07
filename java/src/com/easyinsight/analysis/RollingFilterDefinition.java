package com.easyinsight.analysis;

import com.easyinsight.analysis.MaterializedFilterDefinition;
import com.easyinsight.analysis.MaterializedRollingFilterDefinition;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * User: James Boe
 * Date: Oct 28, 2008
 * Time: 11:08:41 AM
 */
public class RollingFilterDefinition extends FilterDefinition {

    private int interval;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public PersistableFilterDefinition toPersistableFilterDefinition() {
        PersistableRollingFilterDefinition filter = new PersistableRollingFilterDefinition();
        filter.setFilterId(getFilterID());
        filter.setField(getField());
        filter.setApplyBeforeAggregation(isApplyBeforeAggregation());
        filter.setInterval(getInterval());
        return filter;
    }

    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return new MaterializedRollingFilterDefinition(getField(), interval, insightRequestMetadata == null ? null : insightRequestMetadata.getNow());
    }

    public String toQuerySQL(String tableName) {
        StringBuilder queryBuilder = new StringBuilder();
        if (interval == MaterializedRollingFilterDefinition.LAST_DAY) {
            queryBuilder.append("date(").append(getField().toKeySQL()).append(") = (select max(date(").append(getField().toKeySQL()).append(")) from ").append(tableName).append(")");
        } else {
            queryBuilder.append(getField().toKeySQL());
            queryBuilder.append(" >= ? AND ");
            queryBuilder.append(getField().toKeySQL());
            queryBuilder.append(" <= ?");
        }
        return queryBuilder.toString();
    }

    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        if (interval != MaterializedRollingFilterDefinition.LAST_DAY) {
            Date now = insightRequestMetadata.getNow();
            long startTime = MaterializedRollingFilterDefinition.findStartDate(interval, now);
            preparedStatement.setTimestamp(start++, new java.sql.Timestamp(startTime));
            preparedStatement.setTimestamp(start++, new java.sql.Timestamp(now.getTime()));
        }
        return start;
    }
}
