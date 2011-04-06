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

    public static final int LAST = 0;
    public static final int NEXT = 1;
    public static final int BEFORE = 2;
    public static final int AFTER = 3;

    @Column(name="interval_value")
    private int interval;

    @Column(name="before_or_after")
    private int customBeforeOrAfter;

    @Column(name="interval_type")
    private int customIntervalType;

    @Column(name="interval_amount")
    private int customIntervalAmount;

    public int getCustomBeforeOrAfter() {
        return customBeforeOrAfter;
    }

    public void setCustomBeforeOrAfter(int customBeforeOrAfter) {
        this.customBeforeOrAfter = customBeforeOrAfter;
    }

    public int getCustomIntervalType() {
        return customIntervalType;
    }

    public void setCustomIntervalType(int customIntervalType) {
        this.customIntervalType = customIntervalType;
    }

    public int getCustomIntervalAmount() {
        return customIntervalAmount;
    }

    public void setCustomIntervalAmount(int customIntervalAmount) {
        this.customIntervalAmount = customIntervalAmount;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return new MaterializedRollingFilterDefinition(this, insightRequestMetadata == null ? null : insightRequestMetadata.getNow(), insightRequestMetadata);
    }

    public String toQuerySQL(String tableName) {
        StringBuilder queryBuilder = new StringBuilder();
        if (interval == MaterializedRollingFilterDefinition.LAST_DAY) {
            queryBuilder.append("date(").append(getField().toKeySQL()).append(") = (select max(date(").append(getField().toKeySQL()).append(")) from ").append(tableName).append(")");
        } else {
            if (customBeforeOrAfter == RollingFilterDefinition.AFTER) {
                queryBuilder.append(getField().toKeySQL());
                queryBuilder.append(" >= ?");
            } else if (customBeforeOrAfter == RollingFilterDefinition.BEFORE) {
                queryBuilder.append(getField().toKeySQL());
                queryBuilder.append(" <= ?");
            } else {
                queryBuilder.append(getField().toKeySQL());
                queryBuilder.append(" >= ? AND ");
                queryBuilder.append(getField().toKeySQL());
                queryBuilder.append(" <= ?");
            }
        }
        return queryBuilder.toString();
    }

    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        if (interval != MaterializedRollingFilterDefinition.LAST_DAY) {
            Date now = insightRequestMetadata.getNow();
            long startTime = MaterializedRollingFilterDefinition.findStartDate(this, now);
            long endTime = MaterializedRollingFilterDefinition.findEndDate(this, now);
            AnalysisDateDimension date = (AnalysisDateDimension) getField();
            long workingEndDate;
            long workingStartDate;
            workingEndDate = endTime + insightRequestMetadata.getUtcOffset() * 1000 * 60;
            workingStartDate = startTime + insightRequestMetadata.getUtcOffset() * 1000 * 60;
            /*if (date.isTimeshift()) {

            } else {*/
            /*workingEndDate = endTime;
            workingStartDate = startTime;*/
            //}
            if (customBeforeOrAfter == RollingFilterDefinition.AFTER) {
                preparedStatement.setTimestamp(start++, new java.sql.Timestamp(workingStartDate));
            } else if (customBeforeOrAfter == RollingFilterDefinition.BEFORE) {
                preparedStatement.setTimestamp(start++, new java.sql.Timestamp(workingEndDate));
            } else {
                preparedStatement.setTimestamp(start++, new java.sql.Timestamp(workingStartDate));
                preparedStatement.setTimestamp(start++, new java.sql.Timestamp(workingEndDate));
            }
        }
        return start;
    }

    @Override
    public String toXML() {
        String xml = "<rollingFilter>";
        xml += getField().toXML();
        xml += "</rollingFilter>";
        return xml;
    }
}
