package com.easyinsight.analysis;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Aug 28, 2009
 * Time: 10:44:28 AM
 */
@Entity
@Table(name="drill_through")
@PrimaryKeyJoinColumn(name="link_id")
public class DrillThrough extends Link {
    @Column(name="report_id")
    private Long reportID;

    @Column(name="dashboard_id")
    private Long dashboardID;

    @Column(name="mini_window")
    private boolean miniWindow;

    public Long getDashboardID() {
        return dashboardID;
    }

    public void setDashboardID(Long dashboardID) {
        this.dashboardID = dashboardID;
    }

    public Long getReportID() {
        return reportID;
    }

    public void setReportID(Long reportID) {
        this.reportID = reportID;
    }

    @Override
    public void beforeSave() {
        super.beforeSave();
        if (reportID == 0) {
            reportID = null;
        }
        if (dashboardID == 0) {
            dashboardID = null;
        }
    }

    public boolean isMiniWindow() {
        return miniWindow;
    }

    public void setMiniWindow(boolean miniWindow) {
        this.miniWindow = miniWindow;
    }

    public void updateReportIDs(Map<Long, AnalysisDefinition> replacementMap) {
        AnalysisDefinition report = replacementMap.get(reportID);
        setReportID(report.getAnalysisID());
    }

    public String toXML() {
        return "<drillThrough/>";
    }
}
