package com.easyinsight.dashboard;

import com.easyinsight.analysis.*;
import com.easyinsight.core.EIDescriptor;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.database.EIConnection;
import com.easyinsight.scorecard.Scorecard;
import com.easyinsight.security.Roles;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: Nov 26, 2010
 * Time: 1:25:20 PM
 */
public class DashboardReport extends DashboardElement {
    private InsightDescriptor report;
    private boolean showLabel;
    private int labelPlacement;
    private boolean autoCalculateHeight;
    private boolean spaceSides;

    public boolean isSpaceSides() {
        return spaceSides;
    }

    public void setSpaceSides(boolean spaceSides) {
        this.spaceSides = spaceSides;
    }

    public boolean isAutoCalculateHeight() {
        return autoCalculateHeight;
    }

    public void setAutoCalculateHeight(boolean autoCalculateHeight) {
        this.autoCalculateHeight = autoCalculateHeight;
    }

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
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO DASHBOARD_REPORT (DASHBOARD_ELEMENT_ID, REPORT_ID, LABEL_PLACEMENT, SHOW_LABEL, auto_calculate_height, space_sides) " +
                "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
        insertStmt.setLong(1, getElementID());
        insertStmt.setLong(2, report.getId());
        insertStmt.setInt(3, labelPlacement);
        insertStmt.setBoolean(4, showLabel);
        insertStmt.setBoolean(5, autoCalculateHeight);
        insertStmt.setBoolean(6, spaceSides);
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
                "dashboard_report.label_placement, dashboard_report.show_label, dashboard_element.preferred_width, dashboard_element.preferred_height," +
                "dashboard_report.auto_calculate_height, dashboard_report.space_sides from " +
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
            dashboardReport.setAutoCalculateHeight(rs.getBoolean(10));
            dashboardReport.setSpaceSides(rs.getBoolean(11));
            dashboardReport.loadElement(elementID, conn);
        }
        queryStmt.close();
        return dashboardReport;
    }

    public void visit(IDashboardVisitor dashboardVisitor) {
        dashboardVisitor.accept(this);
    }

    public List<EIDescriptor> allItems(List<AnalysisItem> dataSourceItems) {
        List<EIDescriptor> descs = new ArrayList<EIDescriptor>();
        descs.add(report);
        descs.addAll(new AnalysisStorage().getAnalysisDefinition(report.getId()).allItems(dataSourceItems, new AnalysisItemRetrievalStructure(null)));
        return descs;
    }

    public String refreshFunction() {
        StringBuilder sb = new StringBuilder();
        sb.append("renderReport").append(report.getId()).append("()");
        return sb.toString();
    }

    public List<String> jsIncludes() {
        WSAnalysisDefinition reportDefinition = new AnalysisService().openAnalysisDefinition(report.getId());
        return reportDefinition.javaScriptIncludes();
    }

    public List<String> cssIncludes() {
        WSAnalysisDefinition reportDefinition = new AnalysisService().openAnalysisDefinition(report.getId());
        return reportDefinition.cssIncludes();
    }

    @Override
    public Collection<? extends FilterDefinition> filtersForReport(long reportID) {
        if (reportID == report.getId()) {
            List<FilterDefinition> filterDefinitions = new ArrayList<FilterDefinition>();
            populateFilters(filterDefinitions);
            return filterDefinitions;
        }
        return null;
    }

    public String toHTML(FilterHTMLMetadata filterHTMLMetadata) {

        // recurse back up to the top level...

        StringBuilder sb = new StringBuilder();
        WSAnalysisDefinition reportDefinition = new AnalysisService().openAnalysisDefinition(report.getId());
        String div = "reportTarget" + report.getId();
        sb.append("<script type=\"text/javascript\">\n").append("function renderReport").append(report.getId()).append("() {");

        // not only do we need the report's filters, we need all filters in the chain above this report

        sb.append("var strParams = \"\";\n" +
                "            for (var key in filterBase) {\n" +
                "                var keyedFilter = filterBase[key];\n" +
                "                for (var filterValue in keyedFilter) {\n" +
                "                    var value = keyedFilter[filterValue];\n" +
                "                    strParams += filterValue + \"=\" + value + \"&\";\n" +
                "                }\n" +
                "strParams += 'dashboardID="+filterHTMLMetadata.getDashboard().getId() + "&';\n"+
                "            }");
        sb.append(reportDefinition.toHTML(div));
        sb.append("}");
        // $(document).ready(refreshReport('reportTarget517'))
        // refreshReport('div,
        System.out.println("\t\t" + report.getName() + " = renderReport" + report.getId());
        sb.append("renderReport").append(report.getId()).append("();");
        sb.append("</script>\n");
        sb.append("<div style=\"font-size:12px;margin-right:10px\">");
        if (isShowLabel()) {
            sb.append("<div class=\"dashboardReportHeader\"><strong>").append(report.getName()).append("</strong></div>");
        }
        for (FilterDefinition filterDefinition : reportDefinition.getFilterDefinitions()) {
            if (filterDefinition.isShowOnReportView()) {
                System.out.println("\t\t" + filterDefinition.label(false) + " on report");
                sb.append(filterDefinition.toHTML(new FilterHTMLMetadata(filterHTMLMetadata.getDashboard(), reportDefinition)));
            }
        }
        sb.append("</div>");
        sb.append("<div class=\"dashboardReportDiv\" id=\"").append(div).append("\">").append("</div>");
        sb.append(reportDefinition.rootHTML());
        /*sb.append("<script type=\"text/javascript\">\n" +
                "                    $(document).ready(refreshReport('#reportTarget"+report.getId()+"', "+report.getId()+"));\n" +
                "                </script>\n" +
                "                <div id=\"reportTarget"+report.getId()+"\"></div>");*/
        return sb.toString();
    }
}
