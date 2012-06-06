package com.easyinsight.analysis;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

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
        sb.append("<div id=\"").append(divID).append("\" class=\"modal hide\">");
        sb.append("<div class=\"modal-body\">");
        sb.append("<div class=\"control-group\">");
        sb.append("<label class=\"control-label\" for=\"multiSelect\">Available Values</label>");
        sb.append("<div class=\"controls\">");
        /*int size = Math.min(15, dimensionMetadata.getValues().size());
        sb.append("<select multiple=\"multiple\" id=\"multiSelect\" size=\""+size+"\" style=\"width:400px\"");
        for (Value value : dimensionMetadata.getValues()) {
            if (filteredValues.contains(value)) {
                sb.append("<option selected=\"selected\">").append(value).append("</option>");
            } else {
                sb.append("<option>").append(value).append("</option>");
            }
        }*/
        sb.append("</select>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("<div class=\"modal-footer\">\n" +
                "        <button class=\"btn\" onclick=\"\">Send</button>\n" +
                "        <button class=\"btn\" data-dismiss=\"modal\" type=\"button\">Cancel</button>\n" +
                "    </div>");
        sb.append("</div>");
        sb.append("<div style=\"margin-left:5px;margin-top:8px;margin-right:5px\">");
        if (!isToggleEnabled()) {
            sb.append(checkboxHTML());
        }
        sb.append("<a href=\"#"+divID+"\" data-toggle=\"modal\">").append(label()).append("</a></div>");
        return sb.toString();
    }
}
