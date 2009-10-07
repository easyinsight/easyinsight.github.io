package com.easyinsight.analysis;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.PrimaryKeyJoinColumn;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * User: James Boe
 * Date: Oct 28, 2008
 * Time: 11:08:41 AM
 */
@Entity
@Table(name="rolling_range_filter")
@PrimaryKeyJoinColumn(name="filter_id")
public class RollingFilterDefinition extends FilterDefinition {

    @Column(name="interval_value")
    private int interval;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
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
            long endTime = MaterializedRollingFilterDefinition.findEndDate(interval, now);
            preparedStatement.setTimestamp(start++, new java.sql.Timestamp(startTime));
            preparedStatement.setTimestamp(start++, new java.sql.Timestamp(endTime));
        }
        return start;
    }
}
