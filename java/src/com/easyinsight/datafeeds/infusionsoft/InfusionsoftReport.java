package com.easyinsight.datafeeds.infusionsoft;

/**
 * User: jamesboe
 * Date: 9/12/14
 * Time: 1:59 PM
 */
public class InfusionsoftReport {
    private String reportName;
    private String reportID;
    private String userID;

    public InfusionsoftReport() {
    }

    public InfusionsoftReport(String reportName, String reportID, String userID) {
        this.reportName = reportName;
        this.reportID = reportID;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }
}
