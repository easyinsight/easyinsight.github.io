package com.easyinsight.analysis;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Jul 8, 2008
 * Time: 2:23:40 PM
 */
public class FilterRangeDefinition extends FilterDefinition {
    private double startValue;
    private boolean startValueDefined;
    private double endValue;
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

    public PersistableFilterDefinition toPersistableFilterDefinition() {
        PersistableRangeFilterDefinition range = new PersistableRangeFilterDefinition();
        range.setIntrinsic(isIntrinsic());
        range.setFilterId(getFilterID());
        range.setField(getField());
        range.setApplyBeforeAggregation(isApplyBeforeAggregation());
        range.setLowValue(startValueDefined ? startValue : null);
        range.setHighValue(endValueDefined ? endValue : null);
        return range;
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
