package com.easyinsight.analysis;

import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;
import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.core.XMLMetadata;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import nu.xom.Attribute;
import nu.xom.Element;
import org.antlr.runtime.RecognitionException;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: James Boe
 * Date: Oct 28, 2008
 * Time: 11:08:41 AM
 */
@Entity
@Table(name="rolling_range_filter")
@PrimaryKeyJoinColumn(name="filter_id")
public class RollingFilterDefinition extends FilterDefinition {

    public static final int LAST = 0;
    public static final int NEXT = 1;
    public static final int BEFORE = 2;
    public static final int AFTER = 3;

    @Column(name="interval_value")
    private int interval;

    @Column(name="before_or_after")
    private int customBeforeOrAfter;

    @Column(name="interval_type")
    private int customIntervalType;

    @Column(name="interval_amount")
    private int customIntervalAmount;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "filter_to_custom_rolling_interval",
            joinColumns = @JoinColumn(name = "filter_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "custom_rolling_interval_id", nullable = false))
    private List<CustomRollingInterval> intervals = new ArrayList<CustomRollingInterval>();

    @Transient
    private transient Date startDate;
    @Transient
    private transient Date endDate;

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public FilterDefinition clone() throws CloneNotSupportedException {
        RollingFilterDefinition filter = (RollingFilterDefinition) super.clone();
        List<CustomRollingInterval> intervalList = new ArrayList<CustomRollingInterval>();
        for (CustomRollingInterval interval : intervals) {
            intervalList.add(interval.clone());
        }
        filter.setIntervals(intervalList);
        return filter;
    }

    @Override
    public void afterLoad() {
        super.afterLoad();
        List<CustomRollingInterval> intervals = new ArrayList<CustomRollingInterval>();
        for (CustomRollingInterval wrapper : this.intervals) {
            intervals.add(wrapper);
        }
        setIntervals(intervals);
    }

    public List<CustomRollingInterval> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<CustomRollingInterval> intervals) {
        this.intervals = intervals;
    }

    @Override
    public int type() {
        return FilterDefinition.ROLLING_DATE;
    }

    public void customFromXML(Element element, XMLImportMetadata xmlImportMetadata) {
        setInterval(Integer.parseInt(element.getAttribute("interval").getValue()));
        setCustomBeforeOrAfter(Integer.parseInt(element.getAttribute("customBeforeOrAfter").getValue()));
        setCustomIntervalType(Integer.parseInt(element.getAttribute("customIntervalType").getValue()));
        setCustomIntervalAmount(Integer.parseInt(element.getAttribute("customIntervalAmount").getValue()));

    }

    @Override
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = super.toXML(xmlMetadata);
        element.addAttribute(new Attribute("interval", String.valueOf(interval)));
        element.addAttribute(new Attribute("customBeforeOrAfter", String.valueOf(interval)));
        element.addAttribute(new Attribute("customIntervalType", String.valueOf(interval)));
        element.addAttribute(new Attribute("customIntervalAmount", String.valueOf(interval)));
        return element;
    }

    public int getCustomBeforeOrAfter() {
        return customBeforeOrAfter;
    }

    public void setCustomBeforeOrAfter(int customBeforeOrAfter) {
        this.customBeforeOrAfter = customBeforeOrAfter;
    }

    public int getCustomIntervalType() {
        return customIntervalType;
    }

    public void setCustomIntervalType(int customIntervalType) {
        this.customIntervalType = customIntervalType;
    }

    public int getCustomIntervalAmount() {
        return customIntervalAmount;
    }

    public void setCustomIntervalAmount(int customIntervalAmount) {
        this.customIntervalAmount = customIntervalAmount;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return new MaterializedRollingFilterDefinition(this, insightRequestMetadata == null ? null : insightRequestMetadata.getNow(), insightRequestMetadata);
    }

    @Override
    public boolean validForQuery() {
        return interval != MaterializedRollingFilterDefinition.ALL && super.validForQuery();
    }

    public void applyCalculationsBeforeRun(WSAnalysisDefinition report, List<AnalysisItem> allFields, Map<String, List<AnalysisItem>> keyMap, Map<String, List<AnalysisItem>> displayMap,
                                           Feed feed, EIConnection conn, List<FilterDefinition> dlsFilters, InsightRequestMetadata insightRequestMetadata) {
        try {
            for (CustomRollingInterval interval : intervals) {
                if (interval.isStartDefined()) {
                    Value value = new ReportCalculation(interval.getStartScript()).filterApply(report, allFields, keyMap, displayMap, feed, conn, dlsFilters, insightRequestMetadata);
                    if (value.type() == Value.DATE) {
                        DateValue dateValue = (DateValue) value;
                        startDate = dateValue.getDate();
                    } else if (value.type() == Value.NUMBER) {
                        startDate = new Date(value.toDouble().longValue());
                    } else {
                        startDate = null;
                    }
                } else {
                    startDate = null;
                }
                if (interval.isEndDefined()) {
                    Value value = new ReportCalculation(interval.getEndScript()).filterApply(report, allFields, keyMap, displayMap, feed, conn, dlsFilters, insightRequestMetadata);
                    if (value.type() == Value.DATE) {
                        DateValue dateValue = (DateValue) value;
                        endDate = dateValue.getDate();
                    } else if (value.type() == Value.NUMBER) {
                        endDate = new Date(value.toDouble().longValue());
                    } else {
                        endDate = null;
                    }
                } else {
                    endDate = null;
                }
            }
        } catch (RecognitionException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public String toQuerySQL(String tableName) {
        StringBuilder queryBuilder = new StringBuilder();
        if (interval == MaterializedRollingFilterDefinition.LAST_DAY) {
            queryBuilder.append("date(").append(getField().toKeySQL()).append(") = (select max(date(").append(getField().toKeySQL()).append(")) from ").append(tableName).append(")");
        } else if (interval > MaterializedRollingFilterDefinition.ALL) {
            if (startDate != null && endDate != null) {
                queryBuilder.append(getField().toKeySQL());
                queryBuilder.append(" >= ? AND ");
                queryBuilder.append(getField().toKeySQL());
                queryBuilder.append(" <= ?");
            } else if (startDate != null) {
                queryBuilder.append(getField().toKeySQL());
                queryBuilder.append(" >= ?");
            } else if (endDate != null) {
                queryBuilder.append(getField().toKeySQL());
                queryBuilder.append(" <= ?");
            }
        } else {
            if (customBeforeOrAfter == RollingFilterDefinition.AFTER) {
                queryBuilder.append(getField().toKeySQL());
                queryBuilder.append(" >= ?");
            } else if (customBeforeOrAfter == RollingFilterDefinition.BEFORE) {
                queryBuilder.append(getField().toKeySQL());
                queryBuilder.append(" <= ?");
            } else {
                queryBuilder.append(getField().toKeySQL());
                queryBuilder.append(" >= ? AND ");
                queryBuilder.append(getField().toKeySQL());
                queryBuilder.append(" <= ?");
            }
        }
        return queryBuilder.toString();
    }

    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        if (interval > MaterializedRollingFilterDefinition.ALL) {
            if (startDate != null) {
                preparedStatement.setTimestamp(start++, new java.sql.Timestamp(startDate.getTime()));
            }
            if (endDate != null) {
                preparedStatement.setTimestamp(start++, new java.sql.Timestamp(endDate.getTime()));
            }
        } else if (interval != MaterializedRollingFilterDefinition.LAST_DAY) {
            Date now = insightRequestMetadata.getNow();
            long startTime = MaterializedRollingFilterDefinition.findStartDate(this, now, insightRequestMetadata);
            long endTime = MaterializedRollingFilterDefinition.findEndDate(this, now, insightRequestMetadata);


            AnalysisDateDimension date = (AnalysisDateDimension) getField();
            long workingEndDate;
            long workingStartDate;
            /*workingEndDate = endTime + insightRequestMetadata.getUtcOffset() * 1000 * 60;
            workingStartDate = startTime + insightRequestMetadata.getUtcOffset() * 1000 * 60;*/
            workingEndDate = endTime;
            workingStartDate = startTime;
            /*if (date.isTimeshift()) {

            } else {*/

            //}
            if (customBeforeOrAfter == RollingFilterDefinition.AFTER) {
                preparedStatement.setTimestamp(start++, new java.sql.Timestamp(workingStartDate));
            } else if (customBeforeOrAfter == RollingFilterDefinition.BEFORE) {
                preparedStatement.setTimestamp(start++, new java.sql.Timestamp(workingEndDate));
            } else {
                preparedStatement.setTimestamp(start++, new java.sql.Timestamp(workingStartDate));
                preparedStatement.setTimestamp(start++, new java.sql.Timestamp(workingEndDate));
            }
        }
        return start;
    }

    @Override
    public String toHTML(FilterHTMLMetadata filterHTMLMetadata) {
        StringBuilder sb = new StringBuilder();
        String filterName = "filter"+getFilterID();
        String key = filterHTMLMetadata.getFilterKey();
        String function = filterHTMLMetadata.createOnChange();
        String onChange = "changeRolling"+getFilterID()+"();updateRollingFilter('filter" + getFilterID() + "','" + key + "', " + function + ")";
        String customOnChange = "updateRollingFilter(\\'filter" + getFilterID() + "\\', \\'"+key + "\\',\\'"+function+"\\')";
        sb.append("<script type=\"text/javascript\">\n");
        sb.append("function changeRolling"+getFilterID()+"() {");
        sb.append("var menu = document.getElementById('" + filterName + "');");
        sb.append("if (menu.value == '" + MaterializedRollingFilterDefinition.CUSTOM + "') {");
        String direction = "<select style=\"margin-left:10px;width:80px\" onchange=\""+customOnChange+"\" id=\"customDirection" + filterName + "\">" +
                "<option value=\""+RollingFilterDefinition.LAST+"\">Last</option>" +
                "<option value=\""+RollingFilterDefinition.NEXT+"\">Next</option>" +
                "<option value=\""+RollingFilterDefinition.BEFORE+"\">Before</option>" +
                "<option value=\""+RollingFilterDefinition.AFTER+"\">After</option>" +
                "</select>";
        direction = addSelected(direction, String.valueOf(getCustomBeforeOrAfter()));
        String intervalAmount = "<select style=\"margin-left:10px;width:80px\" onchange=\""+customOnChange + "\" id=\"customInterval" + filterName + "\">" +
                "<option value=\"2\">Days</option>" +
                "<option value=\"3\">Weeks</option>" +
                "<option value=\"4\">Months</option>" +
                "<option value=\"5\">Years</option></select>";
        intervalAmount = addSelected(intervalAmount, String.valueOf(getCustomIntervalType()));
        String customOptionHTML = direction +
                "<input style=\"margin-left:10px;width:50px;height:28px\" onchange=\""+customOnChange +"\" type=\"text\" id=\"customValue" + filterName + "\" value=\""+getCustomIntervalAmount()+"\"/>" +
                intervalAmount;
        sb.append("$('#rolling"+getFilterID()+"')").append(".html('"+customOptionHTML+"');");
        sb.append("} else {");
        sb.append("$('#rolling"+getFilterID()+"')").append(".html('');");
        sb.append("}");
        sb.append("}");
        sb.append("</script>");
        sb.append("<div>");
        sb.append(label(true));
        sb.append("<select class=\"filterSelect\" style=\"width:130px\" id=\""+filterName+"\" onchange=\""+onChange+"\">");
        StringBuilder optionBuilder = new StringBuilder();
        optionBuilder.append("<option value=\"19\">All</option>");
        optionBuilder.append("<option value=\""+MaterializedRollingFilterDefinition.DAY+"\">Last Day</option>");
        optionBuilder.append("<option value=\""+MaterializedRollingFilterDefinition.WEEK+"\">Last 7 Days</option>");
        optionBuilder.append("<option value=\""+MaterializedRollingFilterDefinition.MONTH+"\">Last 30 Days</option>");
        optionBuilder.append("<option value=\""+MaterializedRollingFilterDefinition.QUARTER+"\">Last 90 Days</option>");
        optionBuilder.append("<option value=\""+MaterializedRollingFilterDefinition.YEAR+"\">Last 365 Days</option>");
        optionBuilder.append("<option value=\""+MaterializedRollingFilterDefinition.DAY_TO_NOW+"\">Today</option>");
        optionBuilder.append("<option value=\""+MaterializedRollingFilterDefinition.WEEK_TO_NOW+"\">Week to Date</option>");
        optionBuilder.append("<option value=\""+MaterializedRollingFilterDefinition.MONTH_TO_NOW+"\">Month to Date</option>");
        optionBuilder.append("<option value=\""+MaterializedRollingFilterDefinition.QUARTER_TO_NOW+"\">Quarter to Date</option>");
        optionBuilder.append("<option value=\""+MaterializedRollingFilterDefinition.YEAR_TO_NOW+"\">Year to Date</option>");
        optionBuilder.append("<option value=\""+MaterializedRollingFilterDefinition.LAST_FULL_DAY+"\">Last Full Day</option>");
        optionBuilder.append("<option value=\""+MaterializedRollingFilterDefinition.LAST_FULL_WEEK+"\">Last Full Week</option>");
        optionBuilder.append("<option value=\""+MaterializedRollingFilterDefinition.LAST_FULL_MONTH+"\">Last Full Month</option>");
        optionBuilder.append("<option value=\""+MaterializedRollingFilterDefinition.CUSTOM+"\">Custom</option>");
        optionBuilder.append("</select>");
        sb.append(addSelected(optionBuilder.toString(), String.valueOf(getInterval())));
        sb.append("<div style=\"float:right\" id=\"rolling"+getFilterID()+"\">");
        sb.append("</div>");
        sb.append("<script type=\"text/javascript\">\n");
        sb.append("$(document).ready(changeRolling"+getFilterID()+"());\n");
        sb.append("</script>");
        sb.append("</div>");
        return sb.toString();
    }

    private String addSelected(String string, String value) {
        boolean found = true;
        int startIndex = 0;
        do {
            int optionIndex = string.indexOf("value=\"", startIndex);
            if (optionIndex == -1) {
                found = false;
            } else {
                startIndex = optionIndex + 1;
                int closeParanIndex = string.indexOf("\"", startIndex + 6);
                String valueString = string.substring(startIndex + 6, closeParanIndex);
                if (value.equals(valueString)) {
                    return string.substring(0, closeParanIndex + 1) + " selected=\"selected\" " + string.substring(closeParanIndex + 1);
                }
            }
        } while (found);
        return string;
    }
}
