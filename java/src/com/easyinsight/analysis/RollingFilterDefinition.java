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
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
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
    private transient java.time.temporal.Temporal startDate;
    @Transient
    private transient java.time.temporal.Temporal endDate;

    public Temporal getStartDate() {
        return startDate;
    }

    public Temporal getEndDate() {
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
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.PREVIOUS_FULL_WEEK, "Previous Full Week", "dayofweek(nowdateforfilter() - weeks(2), 1)", "dayofweek(nowdateforfilter() - weeks(2), 7))"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.PREVIOUS_FULL_MONTH, "Previous Full Month", "dayofmonth(nowdate() - months(2), 1)", "dayofmonth(nowdate() - months(2), daysinmonth(nowdate() - months(2)))"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.PREVIOUS_FULL_QUARTER, "Previous Full Quarter", "dayofquarter(nowdate() - quarters(2), 1)", "dayofquarter(nowdate() - quarters(2), daysinquarter(nowdate() - quarters(2)))"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.PREVIOUS_FULL_YEAR, "Prior Full Year", "dayofyear(nowdate() - years(2), 1)", "dayofyear(nowdate() - years(2), daysinyear(nowdate() - years(2)))"));

        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.LAST_WEEK_TO_NOW, "Last Week to Now", "dayofweek(nowdateforfilter() - weeks(1), firstdayofweek())", "dayofweek(nowdateforfilter() - weeks(1), dayofweek(now()))"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.LAST_MONTH_TO_NOW, "Last Month to Now", "dayofmonth(nowdate() - months(1), 1)", "dayofmonth(nowdate() - months(1), dayofmonth(nowdate()))"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.LAST_QUARTER_TO_NOW, "Last Quarter to Now", "dayofquarter(nowdate() - quarters(1), 1)", "dayofquarter(nowdate() - quarters(1), dayofquarter(nowdate()))"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.LAST_YEAR_TO_NOW, "Last Year to Now", "dayofyear(nowdate() - years(1), 1)", "dayofyear(nowdate() - years(1), dayofyear(nowdate()))"));

        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.NEXT_FULL_WEEK, "Next Full Week", "dayofweek(nowdateforfilter() + weeks(1), 1)", "dayofweek(nowdateforfilter() + weeks(1), 7)"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.NEXT_FULL_MONTH, "Next Full Month", "dayofmonth(nowdate() + months(1), 1)", "dayofmonth(nowdate() + months(1), daysinmonth(nowdate() + months(1)))"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.NEXT_FULL_QUARTER, "Next Full Quarter", "dayofquarter(nowdate() + quarters(1), 1)", "dayofquarter(nowdate() + quarters(1), daysinquarter(nowdate() + quarters(1)))"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.NEXT_FULL_YEAR, "Next Full Year", "dayofyear(nowdate() + years(1), 1)", "dayofyear(nowdate() + years(1), daysinyear(nowdate() + years(1)))"));

        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.THIS_WEEK, "This Week", "dayofweek(nowdateforfilter(), firstdayofweek())", "dayofweek(nowdateforfilter(), lastdayofweek())"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.THIS_MONTH, "This Month", "dayofmonth(nowdate(), 1)", "dayofmonth(nowdate(), daysinmonth(nowdate()))"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.THIS_QUARTER, "This Quarter", "dayofquarter(nowdate(), 1)", "dayofquarter(nowdate(), daysinquarter(nowdate()))"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.THIS_YEAR, "This Year", "dayofyear(nowdate(), 1)", "dayofyear(nowdate(), daysinyear(nowdate()))"));

        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.THIS_FISCAL_YEAR, "This Fiscal Year", "fiscalyearstart(nowdate())", "fiscalyearend(nowdate())"));
        additionalIntervals.add(createInterval(MaterializedRollingFilterDefinition.PRIOR_FISCAL_YEAR, "Prior Fiscal Year", "fiscalyearstart(nowdate() - years(1))", "fiscalyearend(nowdate() - years(1))"));

        return additionalIntervals;
    }

    public void applyCalculationsBeforeRun(WSAnalysisDefinition report, List<AnalysisItem> allFields, Map<String, List<AnalysisItem>> keyMap, Map<String, List<AnalysisItem>> displayMap,
                                           Feed feed, EIConnection conn, List<FilterDefinition> dlsFilters, InsightRequestMetadata insightRequestMetadata) {
        try {

            List<CustomRollingInterval> intervalList = new ArrayList<CustomRollingInterval>(intervals);
            intervalList.addAll(createAdditionalIntervals());
            for (CustomRollingInterval interval : intervalList) {
                if (interval.getIntervalNumber() == this.getInterval()) {
                    if (interval.isStartDefined()) {
                        Value value = new ReportCalculation(interval.getStartScript()).filterApply(report, allFields, keyMap, displayMap, displayMap, feed, conn, dlsFilters, insightRequestMetadata,
                                dateTime(insightRequestMetadata));
                        if (value.type() == Value.DATE) {
                            DateValue dateValue = (DateValue) value;
                            if (dateTime(insightRequestMetadata)) {
                                startDate = dateValue.getDate().toInstant().atZone(insightRequestMetadata.createZoneID());
                            } else {
                                startDate = dateValue.getDate().toInstant().atZone(insightRequestMetadata.createZoneID()).toLocalDate();
                            }
                        } else if (value.type() == Value.NUMBER) {
                            if (dateTime(insightRequestMetadata)) {
                                startDate = new Date(value.toDouble().longValue()).toInstant().atZone(insightRequestMetadata.createZoneID());
                            } else {
                                startDate = new Date(value.toDouble().longValue()).toInstant().atZone(insightRequestMetadata.createZoneID()).toLocalDate();
                            }
                        } else {
                            startDate = null;
                        }
                    } else {
                        startDate = null;
                    }
                    /*if (startDate != null) {
                        if (((AnalysisDateDimension) getField()).isTimeshift(insightRequestMetadata)) {

                            Instant instant = startDate.toInstant();
                            ZoneId zoneId = insightRequestMetadata.createZoneID();
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
                    }*/
                    if (interval.isEndDefined()) {
                        Value value = new ReportCalculation(interval.getEndScript()).filterApply(report, allFields, keyMap, displayMap, displayMap, feed, conn, dlsFilters, insightRequestMetadata,
                                dateTime(insightRequestMetadata));
                        if (value.type() == Value.DATE) {
                            DateValue dateValue = (DateValue) value;
                            if (dateTime(insightRequestMetadata)) {
                                endDate = dateValue.getDate().toInstant().atZone(insightRequestMetadata.createZoneID());
                            } else {
                                endDate = dateValue.getDate().toInstant().atZone(insightRequestMetadata.createZoneID()).toLocalDate();
                            }
                        } else if (value.type() == Value.NUMBER) {
                            if (dateTime(insightRequestMetadata)) {
                                endDate = new Date(value.toDouble().longValue()).toInstant().atZone(insightRequestMetadata.createZoneID());
                            } else {
                                endDate = new Date(value.toDouble().longValue()).toInstant().atZone(insightRequestMetadata.createZoneID()).toLocalDate();
                            }
                        } else {
                            endDate = null;
                        }
                    } else {
                        endDate = null;
                    }
                    if (endDate != null) {
                        if (((AnalysisDateDimension) getField()).isTimeshift(insightRequestMetadata)) {
                            ZonedDateTime zdt = (ZonedDateTime) endDate;
                            endDate = zdt.plusDays(1).minusNanos(1);
                        }
                    }
                    /*if (endDate != null) {
                        Instant instant = endDate.toInstant();
                        if (((AnalysisDateDimension) getField()).isTimeshift(insightRequestMetadata)) {
                            ZoneId zoneId = ZoneId.ofOffset("", ZoneOffset.ofHours(-(insightRequestMetadata.getUtcOffset() / 60)));
                            ZonedDateTime zdt = instant.atZone(zoneId);
                            zdt = zdt.withHour(0).withMinute(0).withSecond(0).withNano(0).plusDays(1).minusNanos(1);
                            instant = zdt.toInstant();
                            endDate = Date.from(instant);
                        } else {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(endDate);
                            cal.setTimeZone(TimeZone.getTimeZone("GMT"));
                            cal.set(Calendar.HOUR_OF_DAY, 0);
                            cal.set(Calendar.MINUTE, 0);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            endDate = cal.getTime();
                        }
                    }*/
                }
            }
        } catch (RecognitionException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public String toQuerySQL(String tableName, Database database, InsightRequestMetadata insightRequestMetadata) {
        StringBuilder queryBuilder = new StringBuilder();
        if (interval == MaterializedRollingFilterDefinition.LAST_DAY) {
            queryBuilder.append("date(").append(getField().toKeySQL()).append(") = (select max(date(").append(getField().toKeySQL()).append(")) from ").append(tableName).append(")");
        } else if ((interval > MaterializedRollingFilterDefinition.ALL) || (interval < 0)) {
            if (dateTime(insightRequestMetadata)) {
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
                if (database.getDialect() == Database.POSTGRES) {
                    if (startDate != null && endDate != null) {
                        queryBuilder.append( getField().toKeySQL() + "::date");
                        queryBuilder.append(" >= ? AND ");
                        queryBuilder.append(getField().toKeySQL() + "::date");
                        queryBuilder.append(" <= ?");
                    } else if (startDate != null) {
                        queryBuilder.append(getField().toKeySQL() + "::date");
                        queryBuilder.append(" >= ?");
                    } else if (endDate != null) {
                        queryBuilder.append(getField().toKeySQL() + "::date");
                        queryBuilder.append(" <= ?");
                    }
                } else {
                    if (startDate != null && endDate != null) {
                        queryBuilder.append("date(" + getField().toKeySQL() + ")");
                        queryBuilder.append(" >= ? AND ");
                        queryBuilder.append("date(" + getField().toKeySQL() + ")");
                        queryBuilder.append(" <= ?");
                    } else if (startDate != null) {
                        queryBuilder.append("date(" + getField().toKeySQL() + ")");
                        queryBuilder.append(" >= ?");
                    } else if (endDate != null) {
                        queryBuilder.append("date(" + getField().toKeySQL() + ")");
                        queryBuilder.append(" <= ?");
                    }
                }
            }
        } else {
            if (dateTime(insightRequestMetadata)) {
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
            } else {
                if (database.getDialect() == Database.POSTGRES) {
                    if (customBeforeOrAfter == RollingFilterDefinition.AFTER) {
                        queryBuilder.append(getField().toKeySQL() + "::date");
                        queryBuilder.append(" >= ?");
                    } else if (customBeforeOrAfter == RollingFilterDefinition.BEFORE) {
                        queryBuilder.append(getField().toKeySQL() + "::date");
                        queryBuilder.append(" <= ?");
                    } else {
                        queryBuilder.append(getField().toKeySQL() + "::date");
                        queryBuilder.append(" >= ? AND ");
                        queryBuilder.append(getField().toKeySQL() + "::date");
                        queryBuilder.append(" <= ?");
                    }
                } else {
                    if (customBeforeOrAfter == RollingFilterDefinition.AFTER) {
                        queryBuilder.append("date(" + getField().toKeySQL() + ")");
                        queryBuilder.append(" >= ?");
                    } else if (customBeforeOrAfter == RollingFilterDefinition.BEFORE) {
                        queryBuilder.append("date(" + getField().toKeySQL() + ")");
                        queryBuilder.append(" <= ?");
                    } else {
                        queryBuilder.append("date(" + getField().toKeySQL() + ")");
                        queryBuilder.append(" >= ? AND ");
                        queryBuilder.append("date(" + getField().toKeySQL() + ")");
                        queryBuilder.append(" <= ?");
                    }
                }
            }
        }
        return queryBuilder.toString();
    }



    public Date startTime(InsightRequestMetadata insightRequestMetadata) {
        if (startDate == null) {
            if (dateTime(insightRequestMetadata)) {
                startDate = MaterializedRollingFilterDefinition.findStartDateTime(this, new Date(), insightRequestMetadata);
            } else {
                startDate = MaterializedRollingFilterDefinition.findStartDate(this, new Date(), insightRequestMetadata);
            }
        }
        if (startDate instanceof ZonedDateTime) {
            Instant instant = ((ZonedDateTime) startDate).toInstant();
            return Date.from(instant);
        } else if (startDate instanceof LocalDate) {
            Instant instant = ((LocalDate) startDate).atStartOfDay().atZone(insightRequestMetadata.createZoneID()).toInstant();
            return Date.from(instant);
        }
        throw new RuntimeException();
    }

    public Date startTime(InsightRequestMetadata insightRequestMetadata, Temporal startDate) {
        if (startDate instanceof ZonedDateTime) {
            Instant instant = ((ZonedDateTime) startDate).toInstant();
            return Date.from(instant);
        } else if (startDate instanceof LocalDate) {
            Instant instant = ((LocalDate) startDate).atStartOfDay().atZone(insightRequestMetadata.createZoneID()).toInstant();
            return Date.from(instant);
        }
        throw new RuntimeException();
    }

    public int periodTo(Date date, InsightRequestMetadata insightRequestMetadata) {
        if (dateTime(insightRequestMetadata)) {
            LocalDate zdtStart = MaterializedRollingFilterDefinition.findStartDateTime(this, new Date(), insightRequestMetadata).toLocalDate();
            LocalDate zdtEnd = MaterializedRollingFilterDefinition.findEndDateTime(this, new Date(), insightRequestMetadata).toLocalDate();
            return (int) ChronoUnit.DAYS.between(zdtStart, zdtEnd);
        } else {
            LocalDate zdtStart = MaterializedRollingFilterDefinition.findStartDate(this, new Date(), insightRequestMetadata);
            LocalDate zdtEnd = MaterializedRollingFilterDefinition.findEndDate(this, new Date(), insightRequestMetadata);
            return (int) ChronoUnit.DAYS.between(zdtStart, zdtEnd);
        }
    }

    public Date endTime(InsightRequestMetadata insightRequestMetadata) {
        if (endDate == null) {
            if (dateTime(insightRequestMetadata)) {
                endDate = MaterializedRollingFilterDefinition.findEndDateTime(this, new Date(), insightRequestMetadata);
            } else {
                endDate = MaterializedRollingFilterDefinition.findEndDate(this, new Date(), insightRequestMetadata);
            }
        }
        if (endDate instanceof ZonedDateTime) {
            Instant instant = ((ZonedDateTime) endDate).toInstant();
            return Date.from(instant);
        } else if (endDate instanceof LocalDate) {
            Instant instant = ((LocalDate) endDate).atStartOfDay().atZone(insightRequestMetadata.createZoneID()).toInstant();
            return Date.from(instant);
        }
        throw new RuntimeException();
    }

    public Date endTime(InsightRequestMetadata insightRequestMetadata, Temporal endDate) {
        if (endDate instanceof ZonedDateTime) {
            Instant instant = ((ZonedDateTime) endDate).toInstant();
            return Date.from(instant);
        } else if (endDate instanceof LocalDate) {
            Instant instant = ((LocalDate) endDate).atStartOfDay().atZone(insightRequestMetadata.createZoneID()).toInstant();
            return Date.from(instant);
        }
        throw new RuntimeException();
    }

    public boolean dateTime(InsightRequestMetadata insightRequestMetadata) {
        AnalysisDateDimension date = (AnalysisDateDimension) getField();
        return (date.isTimeshift(insightRequestMetadata));
    }

    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        if ((interval > MaterializedRollingFilterDefinition.ALL) || (interval < 0)) {
            if (dateTime(insightRequestMetadata)) {
                if (startDate != null) {
                    insightRequestMetadata.addAudit(this, "Start date/time on database at query is " + startDate);
                    preparedStatement.setTimestamp(start++, new java.sql.Timestamp(startTime(insightRequestMetadata).getTime()));
                }
                if (endDate != null) {
                    insightRequestMetadata.addAudit(this, "End date/time on database at query is " + endDate);
                    preparedStatement.setTimestamp(start++, new java.sql.Timestamp(endTime(insightRequestMetadata).getTime()));
                }
            } else {
                if (startDate != null) {
                    insightRequestMetadata.addAudit(this, "Start date on database at query is " + startDate);
                    preparedStatement.setDate(start++, java.sql.Date.valueOf((LocalDate) startDate));
                }
                if (endDate != null) {
                    insightRequestMetadata.addAudit(this, "End date on database at query is " + endDate);
                    preparedStatement.setDate(start++, java.sql.Date.valueOf((LocalDate) endDate));
                }
            }
        } else if (interval != MaterializedRollingFilterDefinition.LAST_DAY) {
            Date now = insightRequestMetadata.getNow();


            /*System.out.println("Database using start date " + new Date(startTime));
            System.out.println("Database using end date " + new Date(endTime));*/

            /*AnalysisDateDimension date = (AnalysisDateDimension) getField();
            long workingEndDate;
            long workingStartDate;
            *//*workingEndDate = endTime + insightRequestMetadata.getUtcOffset() * 1000 * 60;
            workingStartDate = startTime + insightRequestMetadata.getUtcOffset() * 1000 * 60;*//*
            workingEndDate = endTime;
            workingStartDate = startTime;*/
            /*if (date.isTimeshift()) {

            } else {*/

            //}
            if (dateTime(insightRequestMetadata)) {
                ZonedDateTime startTime = MaterializedRollingFilterDefinition.findStartDateTime(this, now, insightRequestMetadata);
                ZonedDateTime endTime = MaterializedRollingFilterDefinition.findEndDateTime(this, now, insightRequestMetadata);
                if (customBeforeOrAfter == RollingFilterDefinition.AFTER) {
                    insightRequestMetadata.addAudit(this, "Start date/time on database at query is " + startTime);
                    preparedStatement.setTimestamp(start++, new java.sql.Timestamp(startTime(insightRequestMetadata, startTime).getTime()));
                } else if (customBeforeOrAfter == RollingFilterDefinition.BEFORE) {
                    insightRequestMetadata.addAudit(this, "End date/time on database at query is " + endTime);
                    preparedStatement.setTimestamp(start++, new java.sql.Timestamp(endTime(insightRequestMetadata, endTime).getTime()));
                } else {
                    insightRequestMetadata.addAudit(this, "Start date/time on database at query is " + startTime);
                    insightRequestMetadata.addAudit(this, "End date/time on database at query is " + endTime);
                    preparedStatement.setTimestamp(start++, new java.sql.Timestamp(startTime(insightRequestMetadata, startTime).getTime()));
                    preparedStatement.setTimestamp(start++, new java.sql.Timestamp(endTime(insightRequestMetadata, endTime).getTime()));
                }
            } else {
                LocalDate startTime = MaterializedRollingFilterDefinition.findStartDate(this, now, insightRequestMetadata);
                LocalDate endTime = MaterializedRollingFilterDefinition.findEndDate(this, now, insightRequestMetadata);
                if (customBeforeOrAfter == RollingFilterDefinition.AFTER) {
                    insightRequestMetadata.addAudit(this, "Start date on database at query is " + startTime);
                    preparedStatement.setDate(start++, java.sql.Date.valueOf( startTime));
                } else if (customBeforeOrAfter == RollingFilterDefinition.BEFORE) {
                    insightRequestMetadata.addAudit(this, "End date on database at query is " + endTime);
                    preparedStatement.setDate(start++, java.sql.Date.valueOf( endTime));
                } else {
                    insightRequestMetadata.addAudit(this, "Start date on database at query is " + startTime);
                    preparedStatement.setDate(start++, java.sql.Date.valueOf( startTime));
                    insightRequestMetadata.addAudit(this, "Start date on database at query is " + endTime);
                    preparedStatement.setDate(start++, java.sql.Date.valueOf( endTime));
                }
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

    public String asString(InsightRequestMetadata insightRequestMetadata) {

        // Showing data for [Months] of [Year] for describe([Location], [Provider], [Discipline])
        //
        // need to find start date and end date
        if (insightRequestMetadata == null) {
            insightRequestMetadata = new InsightRequestMetadata();
        }
        Date now = insightRequestMetadata.getNow();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
            /*long startTime = MaterializedRollingFilterDefinition.findStartDate(this, now, insightRequestMetadata);
            long endTime = MaterializedRollingFilterDefinition.findEndDate(this, now, insightRequestMetadata);
            startString = df.format(new Date(startTime));
            endString = df.format(new Date(endTime));*/

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
