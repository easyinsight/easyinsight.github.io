package com.easyinsight.core;

import com.easyinsight.dashboard.SavedConfiguration;
import com.easyinsight.tag.Tag;

import java.util.Date;
import java.util.List;

/**
 * User: James Boe
 * Date: Jan 24, 2009
 * Time: 11:23:48 PM
 */
public class InsightDescriptor extends EIDescriptor {       

    private long dataFeedID;
    private int reportType;
    private Date lastDataTime;
    private List<Tag> tags;
    private List<SavedConfiguration> configs;
    private boolean dataSourceReport;

    @Override
    public int getType() {
        return EIDescriptor.REPORT;
    }

    public InsightDescriptor() {
    }

    public InsightDescriptor(long id, String name, long dataFeedID, int reportType, String urlKey, int role, boolean accountVisible) {
        super(name, id, urlKey, accountVisible);
        this.dataFeedID = dataFeedID;
        this.reportType = reportType;
        setRole(role);
    }

    public InsightDescriptor(long id, String name, long dataFeedID, int reportType, String urlKey, Date creationDate, String ownerName, int role, boolean accountVisible) {
        super(name, id, urlKey, accountVisible);
        this.dataFeedID = dataFeedID;
        this.reportType = reportType;
        setCreationDate(creationDate);
        setAuthor(ownerName);
        setRole(role);
    }

    public InsightDescriptor(long id, String name, long dataFeedID, int reportType, String urlKey, Date creationDate, String ownerName, int role,
                             boolean accountVisible, int folder, String description) {
        super(name, id, urlKey, accountVisible);
        this.dataFeedID = dataFeedID;
        this.reportType = reportType;
        setCreationDate(creationDate);
        setAuthor(ownerName);
        setRole(role);
        setFolder(folder);
        setDescription(description);
    }

    public InsightDescriptor(long id, String name, long dataFeedID, int reportType, String urlKey, Date creationDate, String ownerName, int role,
                             boolean accountVisible, int folder, String description, Date dateModified) {
        super(name, id, urlKey, accountVisible);
        this.dataFeedID = dataFeedID;
        this.reportType = reportType;
        setCreationDate(creationDate);
        setAuthor(ownerName);
        setRole(role);
        setFolder(folder);
        setDescription(description);
        setModifiedDate(dateModified);
    }

    public boolean isDataSourceReport() {
        return dataSourceReport;
    }

    public void setDataSourceReport(boolean dataSourceReport) {
        this.dataSourceReport = dataSourceReport;
    }

    public List<SavedConfiguration> getConfigs() {
        return configs;
    }

    public void setConfigs(List<SavedConfiguration> configs) {
        this.configs = configs;
    }

    public Date getLastDataTime() {
        return lastDataTime;
    }

    public void setLastDataTime(Date lastDataTime) {
        this.lastDataTime = lastDataTime;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public int getReportType() {
        return reportType;
    }

    public void setReportType(int reportType) {
        this.reportType = reportType;
    }

    public long getDataFeedID() {
        return dataFeedID;
    }

    public void setDataFeedID(long dataFeedID) {
        this.dataFeedID = dataFeedID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        InsightDescriptor that = (InsightDescriptor) o;

        if (dataFeedID != that.dataFeedID) return false;
        if (reportType != that.reportType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (dataFeedID ^ (dataFeedID >>> 32));
        result = 31 * result + reportType;
        return result;
    }
}
