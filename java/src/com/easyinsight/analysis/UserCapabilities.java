package com.easyinsight.analysis;

/**
 * User: James Boe
 * Date: Oct 5, 2008
 * Time: 10:56:33 AM
 */
public class UserCapabilities {
    private int analysisRole;
    private int feedRole;
    private boolean groupMember;
    private boolean reportingAvailable;

    public UserCapabilities() {
    }

    public UserCapabilities(int analysisRole, int feedRole, boolean groupMember, boolean reportingAvailable) {
        this.analysisRole = analysisRole;
        this.feedRole = feedRole;
        this.groupMember = groupMember;
        this.reportingAvailable = reportingAvailable;
    }

    public boolean isGroupMember() {
        return groupMember;
    }

    public void setGroupMember(boolean groupMember) {
        this.groupMember = groupMember;
    }

    public int getAnalysisRole() {
        return analysisRole;
    }

    public void setAnalysisRole(int analysisRole) {
        this.analysisRole = analysisRole;
    }

    public int getFeedRole() {
        return feedRole;
    }

    public void setFeedRole(int feedRole) {
        this.feedRole = feedRole;
    }

    public boolean isReportingAvailable() {
        return reportingAvailable;
    }

    public void setReportingAvailable(boolean reportingAvailable) {
        this.reportingAvailable = reportingAvailable;
    }
}
