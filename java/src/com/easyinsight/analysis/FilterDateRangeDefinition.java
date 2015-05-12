package com.easyinsight.analysis;

import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.core.XMLMetadata;
import com.easyinsight.database.Database;
import com.easyinsight.pipeline.DateRangePluginComponent;
import com.easyinsight.pipeline.IComponent;
import com.easyinsight.security.SecurityUtil;
import nu.xom.Attribute;
import nu.xom.Element;
import org.hibernate.Session;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.*;
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

    @OneToOne(cascade = CascadeType.MERGE, fetch=FetchType.LAZY)
    @JoinColumn(name="start_dimension")
    private AnalysisDateDimension startDateDimension;

    @Column(name="bounding_end_date")
    private Date boundingEndDate;

    @OneToOne(cascade = CascadeType.MERGE, fetch=FetchType.LAZY)
    @JoinColumn(name="end_dimension")
    private AnalysisDateDimension endDateDimension;

    @Column(name="high_value")
    private Date endDate;
    @Column(name="sliding")
    private boolean sliding;

    @Column(name="start_date_enabled")
    private boolean startDateEnabled = true;

    @Column(name="end_date_enabled")
    private boolean endDateEnabled = true;

    @Column(name="slider_range")
    private boolean sliderRange = true;

    @Transient
    private int startDateDay;

    @Transient
    private int startDateMonth;

    @Transient
    private int startDateYear;

    @Transient
    private int endDateDay;

    @Transient
    private int endDateMonth;

    @Transient
    private int endDateYear;
    /*
    public var startDateDay:int;
    public var startDateYear:int;
    public var startDateMonth:int;
    public var endDateDay:int;
    public var endDateYear:int;
    public var endDateMonth:int;
     */

    @Override
    public int type() {
        return FilterDefinition.DATE;
    }

    public void customFromXML(Element element, XMLImportMetadata xmlImportMetadata) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            startDate = df.parse(element.getAttribute("startDate").getValue());
            endDate = df.parse(element.getAttribute("endDate").getValue());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isStartDateEnabled() {
        return startDateEnabled;
    }

    public void setStartDateEnabled(boolean startDateEnabled) {
        this.startDateEnabled = startDateEnabled;
    }

    public boolean isEndDateEnabled() {
        return endDateEnabled;
    }

    public void setEndDateEnabled(boolean endDateEnabled) {
        this.endDateEnabled = endDateEnabled;
    }

    public boolean isSliderRange() {
        return sliderRange;
    }

    public void setSliderRange(boolean sliderRange) {
        this.sliderRange = sliderRange;
    }

    @Override
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = super.toXML(xmlMetadata);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        element.addAttribute(new Attribute("startDate", df.format(startDate)));
        element.addAttribute(new Attribute("endDate", df.format(endDate)));
        return element;
    }

    @Override
    public void beforeSave(Session session) {
        super.beforeSave(session);
        if (startDateDimension != null) {
            startDateDimension.reportSave(session);
            if (startDateDimension.getKey().getKeyID() == 0) {
                session.save(getField().getKey());
            }
            if (startDateDimension.getAnalysisItemID() == 0) {
                session.save(startDateDimension);
            }
        }
        if (endDateDimension != null) {
            endDateDimension.reportSave(session);
            if (endDateDimension.getKey().getKeyID() == 0) {
                session.save(getField().getKey());
            }
            if (endDateDimension.getAnalysisItemID() == 0) {
                session.save(endDateDimension);
            }
        }
    }

    @Override
    public void afterLoad() {
        super.afterLoad();
        if (startDateDimension != null) {
            startDateDimension = (AnalysisDateDimension) Database.deproxy(startDateDimension);
            startDateDimension.afterLoad();
        }
        if (endDateDimension != null) {
            endDateDimension = (AnalysisDateDimension) Database.deproxy(endDateDimension);
            endDateDimension.afterLoad();
        }
    }

    @Override
    public void updateIDs(ReplacementMap replacementMap) {
        super.updateIDs(replacementMap);
        if (startDateDimension != null) {
            startDateDimension = (AnalysisDateDimension) replacementMap.getField(startDateDimension);
        }
        if (endDateDimension != null) {
            endDateDimension = (AnalysisDateDimension) replacementMap.getField(endDateDimension);
        }
    }

    public Date getBoundingStartDate() {
        return boundingStartDate;
    }

    public void setBoundingStartDate(Date boundingStartDate) {
        this.boundingStartDate = boundingStartDate;
    }

    public AnalysisDateDimension getStartDateDimension() {
        return startDateDimension;
    }

    public void setStartDateDimension(AnalysisDateDimension startDateDimension) {
        this.startDateDimension = startDateDimension;
    }

    public Date getBoundingEndDate() {
        return boundingEndDate;
    }

    public void setBoundingEndDate(Date boundingEndDate) {
        this.boundingEndDate = boundingEndDate;
    }

    public AnalysisDateDimension getEndDateDimension() {
        return endDateDimension;
    }

    public void setEndDateDimension(AnalysisDateDimension endDateDimension) {
        this.endDateDimension = endDateDimension;
    }

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

    public boolean dateTime(InsightRequestMetadata insightRequestMetadata) {
        AnalysisDateDimension date = (AnalysisDateDimension) getField();
        return (date.isTimeshift(insightRequestMetadata));
    }

    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        /*Date workingEndDate = new Date(endDate.getTime() - insightRequestMetadata.getUtcOffset() * 1000 * 60);
        Date workingStartDate = new Date(startDate.getTime() - insightRequestMetadata.getUtcOffset() * 1000 * 60);*/

        Temporal workingStartDate;
        Temporal workingEndDate;
        if (dateTime(insightRequestMetadata)) {
            long accountID = SecurityUtil.getAccountID(false);
            ZonedDateTime startZDT;
            ZonedDateTime endZDT;
            if (accountID == 6941) {
                startZDT = startDate.toInstant().atZone(insightRequestMetadata.createZoneID()).withHour(0).withMinute(0).withSecond(0).withNano(0);
                endZDT = endDate.toInstant().atZone(insightRequestMetadata.createZoneID()).withHour(23).withMinute(59).withSecond(59);
            } else {
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                LocalDate localDate = LocalDate.of(cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH) + 1,
                        cal.get(Calendar.DAY_OF_MONTH));
                startZDT = localDate.atStartOfDay(insightRequestMetadata.createZoneID());
                cal = Calendar.getInstance();
                cal.setTime(endDate);
                localDate = LocalDate.of(cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH) + 1,
                        cal.get(Calendar.DAY_OF_MONTH));
                endZDT = localDate.atStartOfDay(insightRequestMetadata.createZoneID());
                endZDT = endZDT.withHour(23).withMinute(59).withSecond(59).withNano(999);
            }
            insightRequestMetadata.addAudit(this, "Start date/time on in memory is " + startZDT);
            insightRequestMetadata.addAudit(this, "End date/time on in memory query is " + endZDT);
            workingStartDate = startZDT;
            workingEndDate = endZDT;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            LocalDate startLocalDate = LocalDate.of(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH));
            cal.setTime(endDate);
            LocalDate endLocalDate = LocalDate.of(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH));
            insightRequestMetadata.addAudit(this, "Start date on in memory is " + startLocalDate);
            insightRequestMetadata.addAudit(this, "End date on in memory is " + endLocalDate);
            workingStartDate = startLocalDate;
            workingEndDate = endLocalDate;
        }

        return new MaterializedFilterDateRangeDefinition(getField(), workingStartDate, workingEndDate, this, insightRequestMetadata);
    }

    @Override
    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, Collection<AnalysisItem> analysisItemSet, AnalysisItemRetrievalStructure structure) {
        List<AnalysisItem> items = super.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, analysisItemSet, structure);
        if (getStartDateDimension() != null) {
            items.add(getStartDateDimension());
        }
        if (getEndDateDimension() != null) {
            items.add(getEndDateDimension());
        }
        return items;
    }

    public String toQuerySQL(String tableName, Database database, InsightRequestMetadata insightRequestMetadata) {
        StringBuilder queryBuilder = new StringBuilder();
        String columnName = "k" + getField().getKey().toBaseKey().getKeyID();


        if (dateTime(insightRequestMetadata)) {
            queryBuilder.append(columnName);
            queryBuilder.append(" >= ? AND ");
            queryBuilder.append(columnName);
            queryBuilder.append(" <= ?");
        } else {
            if (database.getDialect() == Database.MYSQL) {
                queryBuilder.append("date(" + columnName + ")");
                queryBuilder.append(" >= ? AND ");
                queryBuilder.append("date(" + columnName + ")");
                queryBuilder.append(" <= ?");
            } else {
                queryBuilder.append(columnName + "::date");
                queryBuilder.append(" >= ? AND ");
                queryBuilder.append(columnName + "::date");
                queryBuilder.append(" <= ?");
            }
        }
        return queryBuilder.toString();
    }

    public Date toOldJava(InsightRequestMetadata insightRequestMetadata, java.time.temporal.Temporal startDate) {
        if (startDate instanceof ZonedDateTime) {
            Instant instant = ((ZonedDateTime) startDate).toInstant();
            return Date.from(instant);
        } else if (startDate instanceof LocalDate) {
            Instant instant = ((LocalDate) startDate).atStartOfDay().atZone(insightRequestMetadata.createZoneID()).toInstant();
            return Date.from(instant);
        }
        throw new RuntimeException();
    }

    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {

        if (endDate == null) {
            endDate = new Date();
        }
        if (startDate == null) {
            startDate = new Date();
        }

        /*if (startDateYear > 0) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, startDateYear);
            cal.set(Calendar.MONTH, startDateMonth);
            cal.set(Calendar.DAY_OF_MONTH, startDateDay);
            startDate = cal.getTime();
            cal.set(Calendar.YEAR, endDateYear);
            cal.set(Calendar.MONTH, endDateMonth);
            cal.set(Calendar.DAY_OF_MONTH, endDateDay);
            endDate = cal.getTime();
        }*/

        insightRequestMetadata.addAudit(this, "Actual date/time on database query is " + startDate);
        insightRequestMetadata.addAudit(this, "Actual date/time on database query is " + endDate);

        if (dateTime(insightRequestMetadata)) {
            long accountID = SecurityUtil.getAccountID(false);
            ZonedDateTime startZDT;
            ZonedDateTime endZDT;
            if (accountID == 6941) {
                startZDT = startDate.toInstant().atZone(insightRequestMetadata.createZoneID()).withHour(0).withMinute(0).withSecond(0).withNano(0);
                endZDT = endDate.toInstant().atZone(insightRequestMetadata.createZoneID()).withHour(23).withMinute(59).withSecond(59);
            } else {
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                LocalDate localDate = LocalDate.of(cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH) + 1,
                        cal.get(Calendar.DAY_OF_MONTH));
                startZDT = localDate.atStartOfDay(insightRequestMetadata.createZoneID());
                cal = Calendar.getInstance();
                cal.setTime(endDate);
                localDate = LocalDate.of(cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH) + 1,
                        cal.get(Calendar.DAY_OF_MONTH));
                endZDT = localDate.atStartOfDay(insightRequestMetadata.createZoneID());
                endZDT = endZDT.withHour(23).withMinute(59).withSecond(59).withNano(999);
            }
            insightRequestMetadata.addAudit(this, "Start date/time on database query is " + startZDT);
            insightRequestMetadata.addAudit(this, "End date/time on database query is " + endZDT);
            preparedStatement.setTimestamp(start++, new java.sql.Timestamp(toOldJava(insightRequestMetadata, startZDT).getTime()));
            preparedStatement.setTimestamp(start++, new java.sql.Timestamp(toOldJava(insightRequestMetadata, endZDT).getTime()));
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            LocalDate startLocalDate = LocalDate.of(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH));
            cal.setTime(endDate);
            LocalDate endLocalDate = LocalDate.of(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH));
            insightRequestMetadata.addAudit(this, "Start date on database query is " + startLocalDate);
            insightRequestMetadata.addAudit(this, "End date on database query is " + endLocalDate);
            preparedStatement.setDate(start++, new java.sql.Date(toOldJava(insightRequestMetadata, startLocalDate).getTime()));
            preparedStatement.setDate(start++, new java.sql.Date(toOldJava(insightRequestMetadata, endLocalDate).getTime()));
        }
        return start;
    }

    @Override
    public List<IComponent> createComponents(String pipelineName, IFilterProcessor filterProcessor, AnalysisItem sourceItem, boolean columnLevel) {
        if (getStartDateDimension() != null || getEndDateDimension() != null) {
            return Arrays.asList((IComponent) new DateRangePluginComponent(this));
        } else {
            return super.createComponents(pipelineName, filterProcessor, sourceItem, columnLevel);
        }
    }

    @Override
    public JSONObject toJSON(FilterHTMLMetadata filterHTMLMetadata) throws JSONException {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        JSONObject jo = super.toJSON(filterHTMLMetadata);

        jo.put("type", "date_range");
        jo.put("start", df.format(getStartDate()));
        jo.put("end", df.format(getEndDate()));
        return jo;
    }

    @Override
    public void override(FilterDefinition overrideFilter) {
        FilterDateRangeDefinition f = (FilterDateRangeDefinition) overrideFilter;
        this.setStartDate(f.getStartDate());
        f.setEndDate(f.getEndDate());
    }

    @Override
    public String asString(InsightRequestMetadata insightRequestMetadata) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String startString = df.format(getStartDate());
        String endString = df.format(getEndDate());
        return startString + " to " + endString;
    }

    public int getStartDateDay() {
        return startDateDay;
    }

    public void setStartDateDay(int startDateDay) {
        this.startDateDay = startDateDay;
    }

    public int getStartDateMonth() {
        return startDateMonth;
    }

    public void setStartDateMonth(int startDateMonth) {
        this.startDateMonth = startDateMonth;
    }

    public int getStartDateYear() {
        return startDateYear;
    }

    public void setStartDateYear(int startDateYear) {
        this.startDateYear = startDateYear;
    }

    public int getEndDateDay() {
        return endDateDay;
    }

    public void setEndDateDay(int endDateDay) {
        this.endDateDay = endDateDay;
    }

    public int getEndDateMonth() {
        return endDateMonth;
    }

    public void setEndDateMonth(int endDateMonth) {
        this.endDateMonth = endDateMonth;
    }

    public int getEndDateYear() {
        return endDateYear;
    }

    public void setEndDateYear(int endDateYear) {
        this.endDateYear = endDateYear;
    }
}
