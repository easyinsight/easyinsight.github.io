package com.easyinsight.analysis;

import java.io.Serializable;

/**
 * User: jamesboe
 * Date: 2/11/13
 * Time: 10:20 AM
 */
public class AddonReport implements Serializable {
    private long reportID;
    private String reportName;

    public long getReportID() {
        return reportID;
    }

    public void setReportID(long reportID) {
        this.reportID = reportID;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }
}
