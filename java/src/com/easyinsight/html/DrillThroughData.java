package com.easyinsight.html;

import com.easyinsight.analysis.FilterDefinition;

import java.util.List;

/**
 * User: jamesboe
 * Date: 10/3/12
 * Time: 4:00 PM
 */
public class DrillThroughData {
    private long reportID;
    private long dashboardID;
    private List<FilterDefinition> filters;

    public DrillThroughData(long reportID, long dashboardID, List<FilterDefinition> filters) {
        this.reportID = reportID;
        this.dashboardID = dashboardID;
        this.filters = filters;
    }

    public long getReportID() {
        return reportID;
    }

    public long getDashboardID() {
        return dashboardID;
    }

    public List<FilterDefinition> getFilters() {
        return filters;
    }
}
