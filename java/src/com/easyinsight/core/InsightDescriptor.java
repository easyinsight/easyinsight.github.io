package com.easyinsight.core;

/**
 * User: James Boe
 * Date: Jan 24, 2009
 * Time: 11:23:48 PM
 */
public class InsightDescriptor extends EIDescriptor {       

    private long dataFeedID;
    private int reportType;

    public InsightDescriptor() {
    }

    public InsightDescriptor(long id, String name, long dataFeedID, int reportType) {
        super(name, id);
        this.dataFeedID = dataFeedID;
        this.reportType = reportType;
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
}
