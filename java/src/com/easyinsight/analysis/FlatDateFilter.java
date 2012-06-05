package com.easyinsight.analysis;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 7/15/11
 * Time: 11:03 PM
 */
@Entity
@Table(name="flat_date_filter")
@PrimaryKeyJoinColumn(name="filter_id")
public class FlatDateFilter extends FilterDefinition {
    @Column(name="date_level")
    private int dateLevel;

    @Column(name="selected_value")
    private int value;

    @Transient
    private AnalysisItemResultMetadata cachedValues;

    public int getDateLevel() {
        return dateLevel;
    }

    public void setDateLevel(int dateLevel) {
        this.dateLevel = dateLevel;
    }

    public AnalysisItemResultMetadata getCachedValues() {
        return cachedValues;
    }

    public void setCachedValues(AnalysisItemResultMetadata cachedValues) {
        this.cachedValues = cachedValues;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return new MaterializedFlatDateFilter(getField(), dateLevel, value);
    }

    @Override
    public String toQuerySQL(String tableName) {
        return "year(" + getField().toKeySQL() + ") = ?";
    }

    @Override
    public boolean validForQuery() {
        return true;
    }

    @Override
    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        preparedStatement.setInt(start++, value);
        return start;
    }

    @Override
    public String toHTML(WSAnalysisDefinition report) {
        StringBuilder sb = new StringBuilder();
        String filterName = "filter"+getFilterID();
        String onChange = "updateFilter('filter" + getFilterID() + "')";
        sb.append(label());
        sb.append("<select id=\"").append(filterName).append("\" onchange=\"").append(onChange).append("\">");
        AnalysisDateDimensionResultMetadata metadata = (AnalysisDateDimensionResultMetadata) new DataService().getAnalysisItemMetadata(report.getDataFeedID(), getField(), 0, 0, 0, report);
        Date earliestDate = metadata.getEarliestDate();
        Date latestDate = metadata.getEarliestDate();
        Calendar cal = Calendar.getInstance();

        cal.setTime(earliestDate);
        int startYear = cal.get(Calendar.YEAR);
        cal.setTime(latestDate);
        int endYear = cal.get(Calendar.YEAR);
        List<String> stringList = new ArrayList<String>();
        for (int i = startYear; i <= endYear; i++) {
            stringList.add(String.valueOf(i));
        }

        for (String value : stringList) {
            if (value.equals(String.valueOf(value))) {
                sb.append("<option selected=\"selected\">").append(value).append("</option>");
            } else {
                sb.append("<option>").append(value).append("</option>");
            }
        }
        sb.append("</select>");
        return sb.toString();
    }
}
