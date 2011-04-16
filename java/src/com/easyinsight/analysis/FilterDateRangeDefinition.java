package com.easyinsight.analysis;

import com.easyinsight.database.Database;
import com.easyinsight.pipeline.DateRangePluginComponent;
import com.easyinsight.pipeline.IComponent;
import org.hibernate.Session;

import javax.persistence.*;
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
    public void updateIDs(Map<Long, AnalysisItem> replacementMap) {
        super.updateIDs(replacementMap);
        if (startDateDimension != null) {
            startDateDimension = (AnalysisDateDimension) replacementMap.get(startDateDimension.getAnalysisItemID());
        }
        if (endDateDimension != null) {
            endDateDimension = (AnalysisDateDimension) replacementMap.get(endDateDimension.getAnalysisItemID());
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

    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        Date workingEndDate = new Date(endDate.getTime() - insightRequestMetadata.getUtcOffset() * 1000 * 60);
        Date workingStartDate = new Date(startDate.getTime() - insightRequestMetadata.getUtcOffset() * 1000 * 60);
        // but now it's in the app transformed into the user time!
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(workingStartDate);
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);
        workingStartDate = startCal.getTime();
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(workingEndDate);
        endCal.set(Calendar.HOUR_OF_DAY, 23);
        endCal.set(Calendar.MINUTE, 59);
        endCal.set(Calendar.SECOND, 59);
        endCal.set(Calendar.MILLISECOND, 0);
        workingEndDate = endCal.getTime();
        AnalysisDateDimension date = (AnalysisDateDimension) getField();
        if (date.isTimeshift()) {
            workingEndDate = new Date(workingEndDate.getTime() + insightRequestMetadata.getUtcOffset() * 1000 * 60);
            workingStartDate = new Date(workingStartDate.getTime() + insightRequestMetadata.getUtcOffset() * 1000 * 60);
        }
        return new MaterializedFilterDateRangeDefinition(getField(), workingStartDate, workingEndDate, sliding);
    }

    @Override
    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, int criteria) {
        List<AnalysisItem> items = super.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, criteria);
        if (getStartDateDimension() != null) {
            items.add(getStartDateDimension());
        }
        if (getEndDateDimension() != null) {
            items.add(getEndDateDimension());
        }
        return items;
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
        // scale the query time back to UTC because it's in the database as UTC

        AnalysisDateDimension date = (AnalysisDateDimension) getField();
        /*System.out.println("shift = " + date.isTimeshift());
        System.out.println("initial dates = " + endDate + " and " + startDate);
        System.out.println("utc offset = " + insightRequestMetadata.getUtcOffset());*/

        workingEndDate = new Date(endDate.getTime() - insightRequestMetadata.getUtcOffset() * 1000 * 60);
        workingStartDate = new Date(startDate.getTime() - insightRequestMetadata.getUtcOffset() * 1000 * 60);
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(workingStartDate);
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);
        workingStartDate = startCal.getTime();
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(workingEndDate);
        endCal.set(Calendar.HOUR_OF_DAY, 23);
        endCal.set(Calendar.MINUTE, 59);
        endCal.set(Calendar.SECOND, 59);
        endCal.set(Calendar.MILLISECOND, 0);
        workingEndDate = endCal.getTime();
        /*System.out.println("end date = " + new Date(workingEndDate.getTime()));
        System.out.println("start date = " + new Date(workingStartDate.getTime()));*/
        /*if (date.isTimeshift()) {

        } else {
            workingEndDate = endDate;
            workingStartDate = startDate;
        }*/
        preparedStatement.setTimestamp(start++, new java.sql.Timestamp(workingStartDate.getTime()));
        preparedStatement.setTimestamp(start++, new java.sql.Timestamp(workingEndDate.getTime()));
        return start;
    }

    @Override
    public List<IComponent> createComponents(boolean beforeAggregation, IFilterProcessor filterProcessor, AnalysisItem sourceItem, boolean columnLevel) {
        if (getStartDateDimension() != null || getEndDateDimension() != null) {
            return Arrays.asList((IComponent) new DateRangePluginComponent(this));
        } else {
            return super.createComponents(beforeAggregation, filterProcessor, sourceItem, columnLevel);
        }
    }

    @Override
    public String toXML() {
        String xml = "<dateRangeFilter>";
        xml += getField().toXML();
        xml += "</dateRangeFilter>";
        return xml;
    }
}
