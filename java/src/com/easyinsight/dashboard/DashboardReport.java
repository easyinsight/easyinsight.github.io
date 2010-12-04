package com.easyinsight.dashboard;

import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.database.EIConnection;

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
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO DASHBOARD_REPORT (DASHBOARD_ELEMENT_ID, REPORT_ID) " +
                "VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
        insertStmt.setLong(1, getElementID());
        insertStmt.setLong(2, report.getId());
        insertStmt.execute();
        return id;
    }

    @Override
    public Set<Long> containedReports() {
        Set<Long> ids = new HashSet<Long>();
        ids.add(report.getId());
        return ids;
    }

    @Override
    public void updateReportIDs(Map<Long, AnalysisDefinition> reportReplacementMap) {
        AnalysisDefinition replacement = reportReplacementMap.get(report.getId());
        report = new InsightDescriptor(replacement.getAnalysisID(), replacement.getTitle(), replacement.getDataFeedID(), replacement.getReportType(),
                replacement.getUrlKey());
    }

    public static DashboardElement loadReport(long elementID, EIConnection conn) throws SQLException {
        DashboardReport dashboardReport = null;
        PreparedStatement queryStmt = conn.prepareStatement("SELECT ANALYSIS.title, analysis.data_feed_id, analysis.report_type, analysis.analysis_id, analysis.url_key from " +
                "analysis, dashboard_report where dashboard_report.dashboard_element_id = ? and dashboard_report.report_id = analysis.analysis_id");
        queryStmt.setLong(1, elementID);
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            dashboardReport = new DashboardReport();
            dashboardReport.setReport(new InsightDescriptor(rs.getLong(4), rs.getString(1), rs.getLong(2), rs.getInt(3), rs.getString(5)));
        }
        return dashboardReport;
    }
}
