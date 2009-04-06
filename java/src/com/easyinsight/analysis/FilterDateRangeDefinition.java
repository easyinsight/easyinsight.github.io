package com.easyinsight.analysis;

import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Jul 8, 2008
 * Time: 2:24:20 PM
 */
public class FilterDateRangeDefinition extends FilterDefinition {
    private Date startDate;
    private Date endDate;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public PersistableFilterDefinition toPersistableFilterDefinition() {
        PersistableDateRangeFilterDefinition date = new PersistableDateRangeFilterDefinition();
        date.setFilterId(getFilterID());
        date.setApplyBeforeAggregation(isApplyBeforeAggregation());
        date.setField(getField());
        date.setLowDate(getStartDate());
        date.setHighDate(getEndDate());
        return date;
    }

    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return new MaterializedFilterDateRangeDefinition(getField(), startDate, endDate);
    }

    public String toQuerySQL() {
        StringBuilder queryBuilder = new StringBuilder();
        String columnName = "k" + getField().getKey().getKeyID();
        queryBuilder.append(columnName);
        queryBuilder.append(" >= ? AND ");
        queryBuilder.append(columnName);
        queryBuilder.append(" <= ?");
        return queryBuilder.toString();
    }

    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type) throws SQLException {
        preparedStatement.setDate(start++, new java.sql.Date(startDate.getTime()));
        preparedStatement.setDate(start++, new java.sql.Date(endDate.getTime()));
        return start;
    }
}
