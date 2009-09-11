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
    private boolean sliding;

    public boolean isSliding() {
        return sliding;
    }

    public void setSliding(boolean sliding) {
        this.sliding = sliding;
    }

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
        date.setEnabled(isEnabled());
        date.setApplyBeforeAggregation(isApplyBeforeAggregation());
        date.setField(getField());
        date.setLowDate(getStartDate());
        date.setHighDate(getEndDate());
        date.setIntrinsic(isIntrinsic());
        date.setSliding(isSliding());
        return date;
    }

    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return new MaterializedFilterDateRangeDefinition(getField(), startDate, endDate, sliding);
    }

    public String toQuerySQL(String tableName) {
        StringBuilder queryBuilder = new StringBuilder();
        String columnName = "k" + getField().getKey().getKeyID();
        queryBuilder.append(columnName);
        queryBuilder.append(" >= ? AND ");
        queryBuilder.append(columnName);
        queryBuilder.append(" <= ?");
        return queryBuilder.toString();
    }

    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        Date workingEndDate;
        Date workingStartDate;
        workingEndDate = endDate;
        workingStartDate = startDate;
        preparedStatement.setTimestamp(start++, new java.sql.Timestamp(workingStartDate.getTime()));
        preparedStatement.setTimestamp(start++, new java.sql.Timestamp(workingEndDate.getTime()));
        return start;
    }
}
