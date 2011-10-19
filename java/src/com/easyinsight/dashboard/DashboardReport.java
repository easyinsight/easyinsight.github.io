package com.easyinsight.dashboard;

import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.database.EIConnection;
import com.easyinsight.scorecard.Scorecard;
import com.easyinsight.security.Roles;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: jamesboe
 * Date: Nov 26, 2010
 * Time: 1:25:20 PM
 */
public class DashboardReport extends DashboardElement {
    private InsightDescriptor report;
    private boolean showLabel;
    private int labelPlacement;

    public boolean isShowLabel() {
        return showLabel;
    }

    public void setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
    }

    public int getLabelPlacement() {
        return labelPlacement;
    }

    public void setLabelPlacement(int labelPlacement) {
        this.labelPlacement = labelPlacement;
    }

    public InsightDescriptor getReport() {
        return report;
    }

    public void setReport(InsightDescriptor report) {
        this.report = report;
    }

    @Override
    public int getType() {
        return DashboardElement.REPORT;
    }

    @Override
    public long save(EIConnection conn) throws SQLException {
        long id = super.save(conn);
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO DASHBOARD_REPORT (DASHBOARD_ELEMENT_ID, REPORT_ID, LABEL_PLACEMENT, SHOW_LABEL) " +
                "VALUES (?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
        insertStmt.setLong(1, getElementID());
        insertStmt.setLong(2, report.getId());
        insertStmt.setInt(3, labelPlacement);
        insertStmt.setBoolean(4, showLabel);
        insertStmt.execute();
        insertStmt.close();
        return id;
    }

    @Override
    public Set<Long> containedReports() {
        Set<Long> ids = new HashSet<Long>();
        ids.add(report.getId());
        return ids;
    }

    @Override
    public Set<Long> containedScorecards() {
        return new HashSet<Long>();
    }

    @Override
    public void updateScorecardIDs(Map<Long, Scorecard> scorecardReplacementMap) {
    }

    @Override
    public void updateReportIDs(Map<Long, AnalysisDefinition> reportReplacementMap) {
        AnalysisDefinition replacement = reportReplacementMap.get(report.getId());
        report = new InsightDescriptor(replacement.getAnalysisID(), replacement.getTitle(), replacement.getDataFeedID(), replacement.getReportType(),
                replacement.getUrlKey(), Roles.SUBSCRIBER, false);
    }

    public static DashboardElement loadReport(long elementID, EIConnection conn) throws SQLException {
        DashboardReport dashboardReport = null;
        PreparedStatement queryStmt = conn.prepareStatement("SELECT ANALYSIS.title, analysis.data_feed_id, analysis.report_type, analysis.analysis_id, analysis.url_key, " +
                "dashboard_report.label_placement, dashboard_report.show_label, dashboard_element.preferred_width, dashboard_element.preferred_height from " +
                "analysis, dashboard_report, dashboard_element where dashboard_report.dashboard_element_id = dashboard_element.dashboard_element_id and " +
                "dashboard_report.report_id = analysis.analysis_id and dashboard_element.dashboard_element_id = ?");
        queryStmt.setLong(1, elementID);
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            dashboardReport = new DashboardReport();
            dashboardReport.setReport(new InsightDescriptor(rs.getLong(4), rs.getString(1), rs.getLong(2), rs.getInt(3), rs.getString(5), Roles.SUBSCRIBER, false));
            dashboardReport.setLabelPlacement(rs.getInt(6));
            dashboardReport.setShowLabel(rs.getBoolean(7));
            dashboardReport.setPreferredWidth(rs.getInt(8));
            dashboardReport.setPreferredHeight(rs.getInt(9));
            dashboardReport.loadElement(elementID, conn);
        }
        queryStmt.close();
        return dashboardReport;
    }

    public void visit(IDashboardVisitor dashboardVisitor) {
        dashboardVisitor.accept(this);
    }
}
