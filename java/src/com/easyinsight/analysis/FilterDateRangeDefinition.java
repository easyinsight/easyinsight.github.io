package com.easyinsight.analysis;

import javax.persistence.*;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Jul 8, 2008
 * Time: 2:24:20 PM
 */
@Entity
@Table(name="date_range_filter")
@PrimaryKeyJoinColumn(name="filter_id")
public class FilterDateRangeDefinition extends FilterDefinition {
    @Column(name="low_value")
    private Date startDate;

    @Column(name="bounding_start_date")
    private Date boundingStartDate;

    @OneToOne
    @JoinColumn(name="start_dimension")
    private AnalysisDateDimension startDateDimension;

    @Column(name="bounding_end_date")
    private Date boundingEndDate;

    @OneToOne
    @JoinColumn(name="end_dimension")
    private AnalysisDateDimension endDateDimension;

    @Column(name="high_value")
    private Date endDate;
    @Column(name="sliding")
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

    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return new MaterializedFilterDateRangeDefinition(getField(), startDate, endDate, sliding);
    }

    public String toQuerySQL(String tableName) {
        StringBuilder queryBuilder = new StringBuilder();
        String columnName = "k" + getField().getKey().toBaseKey().getKeyID();
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
