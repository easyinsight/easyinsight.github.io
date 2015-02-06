package com.easyinsight.analysis;

import com.easyinsight.database.Database;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
@Table(name = "flat_date_filter")
@PrimaryKeyJoinColumn(name = "filter_id")
public class FlatDateFilter extends FilterDefinition {
    @Column(name = "date_level")
    private int dateLevel;

    @Column(name = "selected_value")
    private int value;

    @Column(name="start_year")
    private int startYear;

    @Column(name="all_option")
    private boolean allOption;

    @Transient
    private AnalysisItemResultMetadata cachedValues;

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public boolean isAllOption() {
        return allOption;
    }

    public void setAllOption(boolean allOption) {
        this.allOption = allOption;
    }

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
        return new MaterializedFlatDateFilter(getField(), dateLevel, value, insightRequestMetadata);
    }

    public boolean dateTime() {
        AnalysisDateDimension date = (AnalysisDateDimension) getField();
        return (date.isTimeshift());
    }

    @Override
    public String toQuerySQL(String tableName, Database database, InsightRequestMetadata insightRequestMetadata) {
        if (!dateTime()) {
            if (database.getDialect() == Database.MYSQL) {
                if (dateLevel == AnalysisDateDimension.MONTH_LEVEL)
                    return "month(" + getField().toKeySQL() + ") = (? + 1)";
                else
                    return "year(" + getField().toKeySQL() + ") = ?";
            } else {
                if (dateLevel == AnalysisDateDimension.MONTH_LEVEL)
                    return "EXTRACT(month FROM " + getField().toKeySQL() + ") = (? + 1)";
                else
                    return "EXTRACT(year FROM " + getField().toKeySQL() + ") = ?";
            }
        }
        return null;
    }

    @Override
    public boolean validForQuery() {
        return !dateTime();
    }

    @Override
    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        preparedStatement.setInt(start++, value);
        return start;
    }

    @Override
    public JSONObject toJSON(FilterHTMLMetadata filterHTMLMetadata) throws JSONException {
        JSONObject jo = super.toJSON(filterHTMLMetadata);
        jo.put("selected", String.valueOf(value));
        if (dateLevel == AnalysisDateDimension.MONTH_LEVEL) {
            jo.put("type", "flat_date_month");
        } else {
            jo.put("type", "flat_date_year");

            AnalysisDateDimensionResultMetadata metadata = (AnalysisDateDimensionResultMetadata) new DataService().getAnalysisItemMetadata(filterHTMLMetadata.getDataSourceID(),
                    getField(), 0, 0, 0, filterHTMLMetadata.getReport());
            Date earliestDate = metadata.getEarliestDate();
            Date latestDate = metadata.getLatestDate();
            Calendar cal = Calendar.getInstance();

            cal.setTime(earliestDate);
            int startYear = cal.get(Calendar.YEAR);
            cal.setTime(latestDate);
            int endYear = cal.get(Calendar.YEAR);
            List<String> stringList = new ArrayList<String>();
            for (int i = startYear; i <= endYear; i++) {
                stringList.add(String.valueOf(i));
            }
            jo.put("years", new JSONArray(stringList));
        }
        return jo;
    }

    @Override
    public void override(FilterDefinition overrideFilter) {
        FlatDateFilter f = (FlatDateFilter) overrideFilter;
        this.setValue(f.getValue());
    }

    @Override
    public String asString(InsightRequestMetadata insightRequestMetadata) {
        return String.valueOf(value);
    }
}
