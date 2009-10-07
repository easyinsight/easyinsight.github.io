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
    @Column(name="low_value", nullable = true)
    private Double startValue;
    @Transient
    private boolean startValueDefined;
    @Column(name="high_value", nullable = true)
    private Double endValue;
    @Transient
    private boolean endValueDefined;

    public FilterRangeDefinition() {
        setApplyBeforeAggregation(false);
    }

    @Override
    public void beforeSave() {
        if (!startValueDefined) {
            startValue = null;
        }
        if (!endValueDefined) {
            endValue = null;
        }
    }

    @Override
    public void afterLoad() {
        if (startValue == null) {
            startValue = 0.;
            startValueDefined = false;
        } else {
            startValueDefined = true;
        }
        if (endValue == null) {
            endValue = 0.;
            endValueDefined = false;
        } else {
            endValueDefined = true;
        }
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
        String columnName = "k" + getField().getKey().getKeyID();
        queryBuilder.append(columnName);
        queryBuilder.append(" > ? AND ");
        queryBuilder.append(columnName);
        queryBuilder.append(" < ?");
        return queryBuilder.toString();
    }

    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        preparedStatement.setDouble(start++, startValue);
        preparedStatement.setDouble(start++, endValue);
        return start;
    }
}
