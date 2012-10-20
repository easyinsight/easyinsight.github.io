package com.easyinsight.analysis;

/**
 * User: James Boe
 * Date: Jun 21, 2009
 * Time: 11:54:19 AM
 */
public class ReportInfo {
    private WSAnalysisDefinition report;
    private boolean admin;
    private boolean accessDenied;

    public boolean isAccessDenied() {
        return accessDenied;
    }

    public void setAccessDenied(boolean accessDenied) {
        this.accessDenied = accessDenied;
    }

    public WSAnalysisDefinition getReport() {
        return report;
    }

    public void setReport(WSAnalysisDefinition report) {
        this.report = report;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
