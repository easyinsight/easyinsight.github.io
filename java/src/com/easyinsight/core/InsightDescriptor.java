package com.easyinsight.core;

import java.util.Date;

/**
 * User: James Boe
 * Date: Jan 24, 2009
 * Time: 11:23:48 PM
 */
public class InsightDescriptor extends EIDescriptor {       

    private long dataFeedID;
    private int reportType;
    private Date lastDataTime;

    @Override
    public int getType() {
        return EIDescriptor.REPORT;
    }

    public InsightDescriptor() {
    }

    public InsightDescriptor(long id, String name, long dataFeedID, int reportType, String urlKey) {
        super(name, id, urlKey);
        this.dataFeedID = dataFeedID;
        this.reportType = reportType;
    }

    public InsightDescriptor(long id, String name, long dataFeedID, int reportType, String urlKey, Date lastDataTime) {
        super(name, id, urlKey);
        this.dataFeedID = dataFeedID;
        this.reportType = reportType;
        this.lastDataTime = lastDataTime;
    }

    public Date getLastDataTime() {
        return lastDataTime;
    }

    public void setLastDataTime(Date lastDataTime) {
        this.lastDataTime = lastDataTime;
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
