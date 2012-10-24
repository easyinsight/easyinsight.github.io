package com.easyinsight.analysis;

import com.easyinsight.core.XMLMetadata;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.dashboard.DashboardStorage;
import nu.xom.Attribute;
import nu.xom.Element;

import javax.persistence.*;
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

    @Column(name="add_all_filters")
    private boolean addAllFilters;

    @Column(name="mini_window")
    private boolean miniWindow;

    @Column(name="marmotscript")
    private String marmotScript;

    public boolean isAddAllFilters() {
        return addAllFilters;
    }

    public void setAddAllFilters(boolean addAllFilters) {
        this.addAllFilters = addAllFilters;
    }

    public String getMarmotScript() {
        return marmotScript;
    }

    public void setMarmotScript(String marmotScript) {
        this.marmotScript = marmotScript;
    }

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
        if (reportID != null && reportID == 0) {
            reportID = null;
        }
        if (dashboardID != null && dashboardID == 0) {
            dashboardID = null;
        }
    }

    public boolean isMiniWindow() {
        return miniWindow;
    }

    public void setMiniWindow(boolean miniWindow) {
        this.miniWindow = miniWindow;
    }

    public void updateReportIDs(Map<Long, AnalysisDefinition> replacementMap, Map<Long, Dashboard> dashboardReplacementMap) {
        if (reportID != null && reportID > 0) {
            AnalysisDefinition report = replacementMap.get(reportID);
            setReportID(report.getAnalysisID());
        } else if (dashboardID != null && dashboardID > 0) {
            Dashboard dashboard = dashboardReplacementMap.get(dashboardID);
            setDashboardID(dashboard.getId());
        }
    }

    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = new Element("drillThrough");
        element.addAttribute(new Attribute("miniWindow", String.valueOf(miniWindow)));
        element.addAttribute(new Attribute("defaultLink", String.valueOf(isDefaultLink())));
        element.addAttribute(new Attribute("codeGenerated", String.valueOf(isCodeGenerated())));
        if (reportID != null) {
            element.addAttribute(new Attribute("reportID", xmlMetadata.urlKeyForReportID(reportID)));
        }
        if (dashboardID != null) {
            element.addAttribute(new Attribute("dashbordID", xmlMetadata.urlKeyForDashboardID(dashboardID)));
        }
        return element;
    }
}
