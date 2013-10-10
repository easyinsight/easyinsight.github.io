package com.easyinsight.dashboard;

import com.easyinsight.core.InsightDescriptor;

/**
* User: jamesboe
* Date: 9/26/13
* Time: 1:52 PM
*/
public class DashboardInfo {
    private long dashboardID;
    private DashboardStackPositions dashboardStackPositions;
    private InsightDescriptor report;

    public InsightDescriptor getReport() {
        return report;
    }

    public void setReport(InsightDescriptor report) {
        this.report = report;
    }

    public long getDashboardID() {
        return dashboardID;
    }

    public void setDashboardID(long dashboardID) {
        this.dashboardID = dashboardID;
    }

    public DashboardStackPositions getDashboardStackPositions() {
        return dashboardStackPositions;
    }

    public void setDashboardStackPositions(DashboardStackPositions dashboardStackPositions) {
        this.dashboardStackPositions = dashboardStackPositions;
    }
}
