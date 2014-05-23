package com.easyinsight.analysis;

import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;
import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.core.XMLMetadata;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import nu.xom.Attribute;
import nu.xom.Element;
import org.antlr.runtime.RecognitionException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

/**
 * User: James Boe
 * Date: Oct 28, 2008
 * Time: 11:08:41 AM
 */
@Entity
@Table(name = "rolling_range_filter")
@PrimaryKeyJoinColumn(name = "filter_id")
public class RollingFilterDefinition extends FilterDefinition {

    public static final int LAST = 0;
    public static final int NEXT = 1;
    public static final int BEFORE = 2;
    public static final int AFTER = 3;

    @Column(name = "interval_value")
    private int interval;

    @Column(name = "before_or_after")
    private int customBeforeOrAfter;

    @Column(name = "interval_type")
    private int customIntervalType;

    @Column(name = "interval_amount")
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

    private static CustomRollingInterval createInterval(int number, String name, String start, String end) {
        CustomRollingInterval interval = new CustomRollingInterval();
        interval.setFilterLabel(name);
        interval.setStartScript(start);
        interval.setStartDefined(start != null);
        interval.setEndScript(end);
        interval.setEndDefined(end != null);
        interval.setIntervalNumber(number);
        return interval;
    }

    public static List<CustomRollingInterval> createAdditionalIntervals() {
        List<CustomRollingInterval> additionalIntervals = new ArrayList<CustomRollingInterval>();
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.LAST_FULL_QUARTER, "Last Full Quarter", "dayofquarter(nowdate() - quarters(1), 1)", "dayofquarter(nowdate() - quarters(1), daysinquarter(nowdate() - quarters(1)))"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.LAST_FULL_YEAR, "Last Full Year", "dayofyear(nowdate() - years(1), 1)", "dayofyear(nowdate() - years(1), daysinyear(nowdate() - years(1)))"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.PREVIOUS_FULL_WEEK, "Previous Full Week", "dayofweek(nowdate() - weeks(2), 1)", "dayofweek(nowdate() - weeks(2), 7))"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.PREVIOUS_FULL_MONTH, "Previous Full Month", "dayofmonth(nowdate() - months(2), 1)", "dayofmonth(nowdate() - months(2), daysinmonth(nowdate() - months(2)))"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.PREVIOUS_FULL_QUARTER, "Previous Full Quarter", "dayofquarter(nowdate() - quarters(2), 1)", "dayofquarter(nowdate() - quarters(2), daysinquarter(nowdate() - quarters(2)))"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.PREVIOUS_FULL_YEAR, "Prior Full Year", "dayofyear(nowdate() - years(2), 1)", "dayofyear(nowdate() - years(2), daysinyear(nowdate() - years(2)))"));

        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.LAST_WEEK_TO_NOW, "Last Week to Now", "dayofweek(nowdate() - weeks(1), 1)", "dayofweek(nowdate() - weeks(1), dayofweek(nowdate()))"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.LAST_MONTH_TO_NOW, "Last Month to Now", "dayofmonth(nowdate() - months(1), 1)", "dayofmonth(nowdate() - months(1), dayofmonth(nowdate()))"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.LAST_QUARTER_TO_NOW, "Last Quarter to Now", "dayofquarter(nowdate() - quarters(1), 1)", "dayofquarter(nowdate() - quarters(1), dayofquarter(nowdate()))"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.LAST_YEAR_TO_NOW, "Last Year to Now", "dayofyear(nowdate() - years(1), 1)", "dayofyear(nowdate() - years(1), dayofyear(nowdate()))"));

        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.NEXT_FULL_WEEK, "Next Full Week", "dayofweek(nowdate() + weeks(1), 1)", "dayofweek(nowdate() + weeks(1), 7)"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.NEXT_FULL_MONTH, "Next Full Month", "dayofmonth(nowdate() + months(1), 1)", "dayofmonth(nowdate() + months(1), daysinmonth(nowdate()))"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.NEXT_FULL_QUARTER, "Next Full Quarter", "dayofquarter(nowdate() + quarters(1), 1)", "dayofquarter(nowdate() + quarters(1), daysinquarter(nowdate()))"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.NEXT_FULL_YEAR, "Next Full Year", "dayofyear(nowdate() + years(1), 1)", "dayofyear(nowdate() + years(1), daysinyear(nowdate()))"));

        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.THIS_WEEK, "This Week", "dayofweek(nowdate(), 1)", "dayofweek(nowdate(), 7)"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.THIS_MONTH, "This Month", "dayofmonth(nowdate(), 1)", "dayofmonth(nowdate(), daysinmonth(nowdate()))"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.THIS_QUARTER, "This Quarter", "dayofquarter(nowdate(), 1)", "dayofquarter(nowdate(), daysinquarter(nowdate()))"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.THIS_YEAR, "This Year", "dayofyear(nowdate(), 1)", "dayofyear(nowdate(), daysinyear(nowdate()))"));

        return additionalIntervals;
    }

    public void applyCalculationsBeforeRun(WSAnalysisDefinition report, List<AnalysisItem> allFields, Map<String, List<AnalysisItem>> keyMap, Map<String, List<AnalysisItem>> displayMap,
                                           Feed feed, EIConnection conn, List<FilterDefinition> dlsFilters, InsightRequestMetadata insightRequestMetadata) {
        try {

            /*
            public static final int LAST_FULL_QUARTER = -1;
    public static final int LAST_FULL_YEAR = -2;
    public static final int LAST_MONTH_TO_NOW = -3;
    public static final int PREVIOUS_FULL_MONTH = -4;
    public static final int LAST_WEEK_TO_NOW = -5;
    public static final int PREVIOUS_FULL_WEEK = -6;
    public static final int LAST_QUARTER_TO_NOW = -7;
    public static final int PREVIOUS_FULL_QUARTER = -8;
    public static final int PREVIOUS_FULL_YEAR = -9;
    public static final int LAST_YEAR_TO_NOW = -10;
             */
            List<CustomRollingInterval> intervalList = new ArrayList<CustomRollingInterval>(intervals);
            intervalList.addAll(createAdditionalIntervals());
            for (CustomRollingInterval interval : intervalList) {
                if (interval.getIntervalNumber() == this.getInterval()) {
                    if (interval.isStartDefined()) {
                        Value value = new ReportCalculation(interval.getStartScript()).filterApply(report, allFields, keyMap, displayMap, displayMap, feed, conn, dlsFilters, insightRequestMetadata,
                                ((AnalysisDateDimension) getField()).isTimeshift());
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
                    if (startDate != null) {
                        if (((AnalysisDateDimension) getField()).isTimeshift()) {

                            Instant instant = startDate.toInstant();
                            ZoneId zoneId = ZoneId.ofOffset("", ZoneOffset.ofHours(insightRequestMetadata.getUtcOffset() / 60));
                            ZonedDateTime zdt = instant.atZone(zoneId);
                            zdt = zdt.withHour(0).withMinute(0).withSecond(0).withNano(0);
                            instant = zdt.toInstant();
                            startDate = Date.from(instant);
                        } else {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(startDate);
                            cal.setTimeZone(TimeZone.getTimeZone("GMT"));
                            cal.set(Calendar.HOUR_OF_DAY, 0);
                            cal.set(Calendar.MINUTE, 0);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            startDate = cal.getTime();
                        }
                    }
                    if (interval.isEndDefined()) {
                        Value value = new ReportCalculation(interval.getEndScript()).filterApply(report, allFields, keyMap, displayMap, displayMap, feed, conn, dlsFilters, insightRequestMetadata,
                                ((AnalysisDateDimension) getField()).isTimeshift());
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
                    if (endDate != null && !((AnalysisDateDimension) getField()).isTimeshift()) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(endDate);
                        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
                        cal.set(Calendar.HOUR_OF_DAY, 23);
                        cal.set(Calendar.MINUTE, 59);
                        cal.set(Calendar.SECOND, 59);
                        cal.set(Calendar.MILLISECOND, 0);
                        endDate = cal.getTime();
                    } else if (endDate != null) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(endDate);
                        cal.set(Calendar.HOUR_OF_DAY, 23);
                        cal.set(Calendar.MINUTE, 59);
                        cal.set(Calendar.SECOND, 59);
                        cal.set(Calendar.MILLISECOND, 0);
                        int time = insightRequestMetadata.getUtcOffset() / 60;
                        String string;
                        if (time > 0) {
                            string = "GMT-"+Math.abs(time);
                        } else if (time < 0) {
                            string = "GMT+"+Math.abs(time);
                        } else {
                            string = "GMT";
                        }
                        TimeZone timeZone = TimeZone.getTimeZone(string);
                        cal.setTimeZone(timeZone);
                        endDate = cal.getTime();
                    }
                }
            }
        } catch (RecognitionException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public String toQuerySQL(String tableName, Database database) {
        StringBuilder queryBuilder = new StringBuilder();
        if (interval == MaterializedRollingFilterDefinition.LAST_DAY) {
            queryBuilder.append("date(").append(getField().toKeySQL()).append(") = (select max(date(").append(getField().toKeySQL()).append(")) from ").append(tableName).append(")");
        } else if ((interval > MaterializedRollingFilterDefinition.ALL) || (interval < 0)) {
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
        if ((interval > MaterializedRollingFilterDefinition.ALL) || (interval < 0)) {
            if (startDate != null) {
                preparedStatement.setTimestamp(start++, new java.sql.Timestamp(startDate.getTime()));
                insightRequestMetadata.addAudit(this, "Start date on database for " + (((AnalysisDateDimension) getField()).isTimeshift() ? " time shifted " : " not time shifted  ") + " at query to " + new Date(startDate.getTime()));
            }
            if (endDate != null) {
                preparedStatement.setTimestamp(start++, new java.sql.Timestamp(endDate.getTime()));
                insightRequestMetadata.addAudit(this, "End date on database query is " + (((AnalysisDateDimension) getField()).isTimeshift() ? " time shifted " : " not time shifted  ") + " at query to " + new Date(endDate.getTime()));
            }
        } else if (interval != MaterializedRollingFilterDefinition.LAST_DAY) {
            Date now = insightRequestMetadata.getNow();
            long startTime = MaterializedRollingFilterDefinition.findStartDate(this, now, insightRequestMetadata);
            long endTime = MaterializedRollingFilterDefinition.findEndDate(this, now, insightRequestMetadata);

            /*System.out.println("Database using start date " + new Date(startTime));
            System.out.println("Database using end date " + new Date(endTime));*/

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
                insightRequestMetadata.addAudit(this, "Start date on database query is " + (((AnalysisDateDimension) getField()).isTimeshift() ? " time shifted " : " not time shifted ") + " at query to " + new Date(workingStartDate));
            } else if (customBeforeOrAfter == RollingFilterDefinition.BEFORE) {
                preparedStatement.setTimestamp(start++, new java.sql.Timestamp(workingEndDate));
                insightRequestMetadata.addAudit(this, "End date on database query is " + (((AnalysisDateDimension) getField()).isTimeshift() ? " time shifted " : " not time shifted ") + " at query to " +  new Date(workingEndDate));
            } else {
                preparedStatement.setTimestamp(start++, new java.sql.Timestamp(workingStartDate));
                insightRequestMetadata.addAudit(this, "Start date on database query is " + (((AnalysisDateDimension) getField()).isTimeshift() ? " time shifted " : " not time shifted ") + " at query to " +  new Date(workingStartDate));
                preparedStatement.setTimestamp(start++, new java.sql.Timestamp(workingEndDate));
                insightRequestMetadata.addAudit(this, "End date on database query is " + (((AnalysisDateDimension) getField()).isTimeshift() ? " time shifted " : " not time shifted ") + " at query to " +  new Date(workingEndDate));
            }
        }
        return start;
    }

    @Override
    public JSONObject toJSON(FilterHTMLMetadata filterHTMLMetadata) throws JSONException {
        JSONObject jo = super.toJSON(filterHTMLMetadata);
        jo.put("type", "rolling");
        jo.put("interval_type", interval);
        jo.put("direction", customBeforeOrAfter);
        jo.put("value", customIntervalAmount);
        jo.put("interval", customIntervalType);
        JSONArray customIntervalList = new JSONArray();
        for (CustomRollingInterval interval : createAdditionalIntervals()) {
            JSONObject jj = new JSONObject();
            jj.put("name", interval.getFilterLabel());
            jj.put("interval", interval.getIntervalNumber());
            customIntervalList.put(jj);
        }
        for (CustomRollingInterval interval : getIntervals()) {
            JSONObject jj = new JSONObject();
            jj.put("name", interval.getFilterLabel());
            jj.put("interval", interval.getIntervalNumber());
            customIntervalList.put(jj);
        }
        jo.put("custom_intervals", customIntervalList);
        return jo;
    }

    @Override
    public void override(FilterDefinition overrideFilter) {
        RollingFilterDefinition f = (RollingFilterDefinition) overrideFilter;
        setInterval(f.getInterval());
        setCustomBeforeOrAfter(f.getCustomBeforeOrAfter());
        setCustomIntervalAmount(f.getCustomIntervalAmount());
        setCustomIntervalType(f.getCustomIntervalType());
        setIntervals(f.getIntervals());
    }

    @Override
    public String toHTML(FilterHTMLMetadata filterHTMLMetadata) {
        StringBuilder sb = new StringBuilder();
        String filterName = "filter" + getFilterID();
        String key = filterHTMLMetadata.getFilterKey();
        String function = filterHTMLMetadata.createOnChange();
        String onChange = "changeRolling" + getFilterID() + "();updateRollingFilter('filter" + getFilterID() + "','" + key + "', " + function + ")";
        String customOnChange = "updateRollingFilter(\\'filter" + getFilterID() + "\\', \\'" + key + "\\',\\'" + function + "\\')";
        sb.append("<script type=\"text/javascript\">\n");
        sb.append("function changeRolling" + getFilterID() + "() {");
        sb.append("var menu = document.getElementById('" + filterName + "');");
        sb.append("if (menu.value == '" + MaterializedRollingFilterDefinition.CUSTOM + "') {");
        String direction = "<select style=\"margin-left:10px;width:80px\" onchange=\"" + customOnChange + "\" id=\"customDirection" + filterName + "\">" +
                "<option value=\"" + RollingFilterDefinition.LAST + "\">Last</option>" +
                "<option value=\"" + RollingFilterDefinition.NEXT + "\">Next</option>" +
                "<option value=\"" + RollingFilterDefinition.BEFORE + "\">Before</option>" +
                "<option value=\"" + RollingFilterDefinition.AFTER + "\">After</option>" +
                "</select>";
        direction = addSelected(direction, String.valueOf(getCustomBeforeOrAfter()));
        String intervalAmount = "<select style=\"margin-left:10px;width:80px\" onchange=\"" + customOnChange + "\" id=\"customInterval" + filterName + "\">" +
                "<option value=\"2\">Days</option>" +
                "<option value=\"3\">Weeks</option>" +
                "<option value=\"4\">Months</option>" +
                "<option value=\"5\">Years</option></select>";
        intervalAmount = addSelected(intervalAmount, String.valueOf(getCustomIntervalType()));
        String customOptionHTML = direction +
                "<input style=\"margin-left:10px;width:50px;height:28px\" onchange=\"" + customOnChange + "\" type=\"text\" id=\"customValue" + filterName + "\" value=\"" + getCustomIntervalAmount() + "\"/>" +
                intervalAmount;
        sb.append("$('#rolling" + getFilterID() + "')").append(".html('" + customOptionHTML + "');");
        sb.append("} else {");
        sb.append("$('#rolling" + getFilterID() + "')").append(".html('');");
        sb.append("}");
        sb.append("}");
        sb.append("</script>");
        sb.append("<div>");
        sb.append(label(true));
        sb.append("<select class=\"filterSelect\" style=\"width:140px\" id=\"" + filterName + "\" onchange=\"" + onChange + "\">");
        StringBuilder optionBuilder = new StringBuilder();
        optionBuilder.append("<option value=\"19\">All</option>");
        optionBuilder.append("<option value=\"" + MaterializedRollingFilterDefinition.DAY + "\">Last Day</option>");
        optionBuilder.append("<option value=\"" + MaterializedRollingFilterDefinition.WEEK + "\">Last 7 Days</option>");
        optionBuilder.append("<option value=\"" + MaterializedRollingFilterDefinition.MONTH + "\">Last 30 Days</option>");
        optionBuilder.append("<option value=\"" + MaterializedRollingFilterDefinition.QUARTER + "\">Last 90 Days</option>");
        optionBuilder.append("<option value=\"" + MaterializedRollingFilterDefinition.YEAR + "\">Last 365 Days</option>");
        optionBuilder.append("<option value=\"" + MaterializedRollingFilterDefinition.DAY_TO_NOW + "\">Today</option>");
        optionBuilder.append("<option value=\"" + MaterializedRollingFilterDefinition.WEEK_TO_NOW + "\">Week to Date</option>");
        optionBuilder.append("<option value=\"" + MaterializedRollingFilterDefinition.MONTH_TO_NOW + "\">Month to Date</option>");
        optionBuilder.append("<option value=\"" + MaterializedRollingFilterDefinition.QUARTER_TO_NOW + "\">Quarter to Date</option>");
        optionBuilder.append("<option value=\"" + MaterializedRollingFilterDefinition.YEAR_TO_NOW + "\">Year to Date</option>");
        optionBuilder.append("<option value=\"" + MaterializedRollingFilterDefinition.LAST_FULL_DAY + "\">Last Full Day</option>");
        optionBuilder.append("<option value=\"" + MaterializedRollingFilterDefinition.LAST_FULL_WEEK + "\">Last Full Week</option>");
        optionBuilder.append("<option value=\"" + MaterializedRollingFilterDefinition.LAST_FULL_MONTH + "\">Last Full Month</option>");
        optionBuilder.append("<option value=\"" + MaterializedRollingFilterDefinition.CUSTOM + "\">Custom</option>");
        optionBuilder.append("</select>");
        sb.append(addSelected(optionBuilder.toString(), String.valueOf(getInterval())));
        sb.append("<div style=\"float:right\" id=\"rolling" + getFilterID() + "\">");
        sb.append("</div>");
        sb.append("<script type=\"text/javascript\">\n");
        sb.append("$(document).ready(changeRolling" + getFilterID() + "());\n");
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

    public String asString(InsightRequestMetadata insightRequestMetadata) {

        // Showing data for [Months] of [Year] for describe([Location], [Provider], [Discipline])
        //
        // need to find start date and end date
        if (insightRequestMetadata == null) {
            insightRequestMetadata = new InsightRequestMetadata();
        }
        Date now = insightRequestMetadata.getNow();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String startString = null;
        String endString = null;

        if (interval == MaterializedRollingFilterDefinition.ALL) {
            return "";
        } else if ((interval > MaterializedRollingFilterDefinition.ALL) || (interval < 0)) {
            applyCalculationsBeforeRun(null, null, null, null, null, null, null, insightRequestMetadata);
            if (startDate != null) {
                startString = df.format(startDate);
            }

            if (endDate != null) {
                endString = df.format(endDate);
            }
        } else {
            long startTime = MaterializedRollingFilterDefinition.findStartDate(this, now, insightRequestMetadata);
            long endTime = MaterializedRollingFilterDefinition.findEndDate(this, now, insightRequestMetadata);
            startString = df.format(new Date(startTime));
            endString = df.format(new Date(endTime));

        }

        if (startString != null && endString != null) {
            return startString + " to " + endString;
        } else if (startString != null) {
            return "after " + startString;
        } else {
            return "before " + endString;
        }
    }
}
