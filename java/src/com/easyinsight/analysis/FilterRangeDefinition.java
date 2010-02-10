package com.easyinsight.analysis;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Jul 8, 2008
 * Time: 2:23:40 PM
 */
@Entity
@Table(name="range_filter")
@PrimaryKeyJoinColumn(name="filter_id")
public class FilterRangeDefinition extends FilterDefinition {
    @Column(name="low_value")
    private double startValue;
    @Column(name="low_value_defined")
    private boolean startValueDefined;
    @Column(name="high_value")
    private double endValue;
    @Column(name="high_value_defined")
    private boolean endValueDefined;

    public FilterRangeDefinition() {
        setApplyBeforeAggregation(false);
    }

    public boolean isStartValueDefined() {
        return startValueDefined;
    }

    public void setStartValueDefined(boolean startValueDefined) {
        this.startValueDefined = startValueDefined;
    }

    public boolean isEndValueDefined() {
        return endValueDefined;
    }

    public void setEndValueDefined(boolean endValueDefined) {
        this.endValueDefined = endValueDefined;
    }

    public double getStartValue() {
        return startValue;
    }

    public void setStartValue(double startValue) {
        this.startValue = startValue;
    }

    public double getEndValue() {
        return endValue;
    }

    public void setEndValue(double endValue) {
        this.endValue = endValue;
    }

    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return new MaterializedFilterRangeDefinition(getField(), startValueDefined ? startValue : null, endValueDefined ? endValue : null);
    }

    public String toQuerySQL(String tableName) {
        StringBuilder queryBuilder = new StringBuilder();
        String columnName = getField().toKeySQL();
        queryBuilder.append(columnName);
        if (startValueDefined && endValueDefined) {
            queryBuilder.append(" > ? AND ");
            queryBuilder.append(columnName);
            queryBuilder.append(" < ?");
        } else if (startValueDefined) {
            queryBuilder.append(" > ?");
        } else if (endValueDefined) {
            queryBuilder.append(" < ?");
        }
        return queryBuilder.toString();
    }

    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        if (startValueDefined) {
            preparedStatement.setDouble(start++, startValue);
        }
        if (endValueDefined) {
            preparedStatement.setDouble(start++, endValue);
        }
        return start;
    }
}
