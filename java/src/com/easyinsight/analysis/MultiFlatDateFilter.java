package com.easyinsight.analysis;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;

/**
 * User: jamesboe
 * Date: 7/15/11
 * Time: 11:03 PM
 */
@Entity
@Table(name="multi_date_filter")
@PrimaryKeyJoinColumn(name="filter_id")
public class MultiFlatDateFilter extends FilterDefinition {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "multi_date_filter_to_date_level_wrapper",
            joinColumns = @JoinColumn(name = "filter_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "date_level_wrapper_id", nullable = false))
    private Collection<DateLevelWrapper> levels = new ArrayList<DateLevelWrapper>();

    @Column(name="end_date_property")
    private String endDateProperty;

    public String getEndDateProperty() {
        return endDateProperty;
    }

    public void setEndDateProperty(String endDateProperty) {
        this.endDateProperty = endDateProperty;
    }

    public Collection<DateLevelWrapper> getLevels() {
        return levels;
    }

    public void setLevels(Collection<DateLevelWrapper> dateLevels) {
        this.levels = dateLevels;
    }

    @Override
    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return new MaterializedMultiFlatDateFilter(getField(), levels);
    }

    @Override
    public void afterLoad() {
        super.afterLoad();
        Collection<DateLevelWrapper> wrappers = new ArrayList<DateLevelWrapper>();
        for (DateLevelWrapper wrapper : getLevels()) {
            wrappers.add(wrapper);
        }
        setLevels(wrappers);
    }

    @Override
    public String toQuerySQL(String tableName) {
        /*return "year(" + getField().toKeySQL() + ") = ?";*/
        return null;
    }

    @Override
    public boolean validForQuery() {
        return false;
    }

    @Override
    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        /*preparedStatement.setInt(start++, value);
        return start;*/
        return start;
    }

    @Override
    public String toHTML(WSAnalysisDefinition report) {
        StringBuilder sb = new StringBuilder();
        String divID = "filter" + getFilterID() + "div";
        String filterName = "filter" + getFilterID();
        sb.append("<div id=\"").append(divID).append("\" class=\"modal hide\">");
        sb.append("<div class=\"modal-body\">");
        sb.append("<div class=\"control-group\">");
        sb.append("<div class=\"controls\">");

        int minLevel = Calendar.DECEMBER;
        int maxLevel = Calendar.JANUARY;
        for (DateLevelWrapper wrapper : getLevels()) {
            minLevel = Math.min(minLevel, wrapper.getDateLevel());
            maxLevel = Math.max(maxLevel, wrapper.getDateLevel());
        }
        Calendar calendar = Calendar.getInstance();
        sb.append("<select id=\"" + filterName + "start\">");
        for (int i = Calendar.JANUARY; i <= Calendar.DECEMBER; i++) {
            calendar.set(Calendar.MONTH, i);
            String displayName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
            if (i == minLevel) {
                sb.append("<option selected=\"selected\" value=\"").append(i).append("\">").append(displayName).append("</option>");
            } else {
                sb.append("<option value=\"").append(i).append("\">").append(displayName).append("</option>");
            }
        }
        sb.append("</select>");
        sb.append("<select id=\""+filterName+"end\">");
        for (int i = Calendar.JANUARY; i <= Calendar.DECEMBER; i++) {
            calendar.set(Calendar.MONTH, i);
            String displayName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
            if (i == maxLevel) {
                sb.append("<option selected=\"selected\" value=\"").append(i).append("\">").append(displayName).append("</option>");
            } else {
                sb.append("<option value=\"").append(i).append("\">").append(displayName).append("</option>");
            }
        }
        sb.append("</select>");

        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("<div class=\"modal-footer\">\n" +
                "        <button class=\"btn\" data-dismiss=\"modal\" onclick=\"updateMultiMonth('"+filterName+"')\">Save</button>\n" +
                "        <button class=\"btn\" data-dismiss=\"modal\" type=\"button\">Cancel</button>\n" +
                "    </div>");
        sb.append("</div>");
        sb.append("<div style=\"margin-left:5px;margin-top:8px;margin-right:5px\">");
        if (!isToggleEnabled()) {
            sb.append(checkboxHTML());
        }
        /*String flatLabel;
        if (getLevels().isEmpty()) {
            flatLabel = "Click to Configure";
        } else {
            if (minLevel == maxLevel) {
                calendar.set(Calendar.MONTH, minLevel);
                flatLabel = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
            } else {
                calendar.set(Calendar.MONTH, minLevel);
                String firstMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
                calendar.set(Calendar.MONTH, maxLevel);
                String endMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
                flatLabel = firstMonth + " to " + endMonth;
            }
        }*/
        sb.append("<a href=\"#"+divID+"\" data-toggle=\"modal\">").append(label()).append("</a></div>");
        return sb.toString();
    }
}
