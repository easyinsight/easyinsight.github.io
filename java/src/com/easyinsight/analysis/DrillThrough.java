package com.easyinsight.analysis;

import com.easyinsight.core.XMLMetadata;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.dashboard.DashboardStorage;
import com.easyinsight.database.Database;
import nu.xom.Attribute;
import nu.xom.Element;
import org.hibernate.Session;

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

    @Column(name="add_all_filters")
    private boolean addAllFilters;

    @Column(name="mini_window")
    private boolean miniWindow;

    @Column(name="marmotscript")
    private String marmotScript;

    @Column(name="show_drillthrough_filters")
    private boolean showDrillThroughFilters;

    @Column(name="filter_row_groupings")
    private boolean filterRowGroupings;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="pass_through_field_id")
    private AnalysisItemHandle passThroughField;

    public void afterLoad() {
        super.afterLoad();
        if (passThroughField != null) {
            passThroughField = (AnalysisItemHandle) Database.deproxy(passThroughField);
        }
    }

    public AnalysisItemHandle getPassThroughField() {
        return passThroughField;
    }

    public void setPassThroughField(AnalysisItemHandle passThroughField) {
        this.passThroughField = passThroughField;
    }

    public boolean isShowDrillThroughFilters() {
        return showDrillThroughFilters;
    }

    public void setShowDrillThroughFilters(boolean showDrillThroughFilters) {
        this.showDrillThroughFilters = showDrillThroughFilters;
    }

    public boolean isFilterRowGroupings() {
        return filterRowGroupings;
    }

    public void setFilterRowGroupings(boolean filterRowGroupings) {
        this.filterRowGroupings = filterRowGroupings;
    }

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
    public void beforeSave(Session session) {
        super.beforeSave(session);
        if (reportID != null && reportID == 0) {
            reportID = null;
        }
        if (dashboardID != null && dashboardID == 0) {
            dashboardID = null;
        }
        if (passThroughField != null) {
            passThroughField.save();
            if (passThroughField.getHandleID() == null || passThroughField.getHandleID() == 0) {
                session.save(passThroughField);
            } else {
                session.update(passThroughField);
            }
        }
    }



    public boolean isMiniWindow() {
        return miniWindow;
    }

    public void setMiniWindow(boolean miniWindow) {
        this.miniWindow = miniWindow;
    }

    @Override
    public Link clone() throws CloneNotSupportedException {
        DrillThrough drillThrough = (DrillThrough) super.clone();
        if (passThroughField != null) {
            drillThrough.setPassThroughField(passThroughField.clone());
        }
        return drillThrough;
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
        if (reportID != null && reportID > 0) {
            element.addAttribute(new Attribute("reportID", xmlMetadata.urlKeyForReportID(reportID)));
        }
        if (dashboardID != null && dashboardID > 0) {
            element.addAttribute(new Attribute("dashbordID", xmlMetadata.urlKeyForDashboardID(dashboardID)));
        }
        return element;
    }

    public List<AnalysisItem> getFields(List<AnalysisItem> allItems) {
        List<AnalysisItem> retItems = new ArrayList<AnalysisItem>();

        if (passThroughField != null) {
            // reconcile item from allItems
            // return the field
            for (AnalysisItem field : allItems) {
                if ((passThroughField.getAnalysisItemID() != null && passThroughField.getAnalysisItemID() > 0 && field.getAnalysisItemID() == passThroughField.getAnalysisItemID()) ||
                        passThroughField.getName().equals(field.toDisplay())) {
                    retItems.add(field);
                    break;
                }
            }
        }

        return retItems;
    }
}
