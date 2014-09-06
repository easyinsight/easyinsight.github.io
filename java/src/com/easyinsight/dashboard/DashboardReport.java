package com.easyinsight.dashboard;

import com.easyinsight.analysis.*;
import com.easyinsight.core.EIDescriptor;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.scorecard.Scorecard;
import com.easyinsight.security.Roles;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

/**
 * User: jamesboe
 * Date: Nov 26, 2010
 * Time: 1:25:20 PM
 */
public class DashboardReport extends DashboardElement {
    private InsightDescriptor report;
    private String tagName;
    private boolean showLabel;
    private int labelPlacement;
    private boolean autoCalculateHeight;
    private boolean spaceSides;
    private long recommendedTag;
    private Map<String, FilterDefinition> overridenFilters = new HashMap<String, FilterDefinition>();

    public long getRecommendedTag() {
        return recommendedTag;
    }

    public void setRecommendedTag(long recommendedTag) {
        this.recommendedTag = recommendedTag;
    }

    public Map<String, FilterDefinition> getOverridenFilters() {
        return overridenFilters;
    }

    public void setOverridenFilters(Map<String, FilterDefinition> overridenFilters) {
        this.overridenFilters = overridenFilters;
    }

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
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO DASHBOARD_REPORT (DASHBOARD_ELEMENT_ID, REPORT_ID, " +
                "LABEL_PLACEMENT, SHOW_LABEL, auto_calculate_height, space_sides, preferred_tag) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
        insertStmt.setLong(1, getElementID());
        insertStmt.setLong(2, report.getId());
        insertStmt.setInt(3, labelPlacement);
        insertStmt.setBoolean(4, showLabel);
        insertStmt.setBoolean(5, autoCalculateHeight);
        insertStmt.setBoolean(6, spaceSides);
        if (recommendedTag == 0) {
            insertStmt.setNull(7, Types.BIGINT);
        } else {
            insertStmt.setLong(7, recommendedTag);
        }
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

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public void updateReportIDs(Map<Long, AnalysisDefinition> reportReplacementMap, List<AnalysisItem> allFields, boolean changingDataSource, FeedDefinition dataSource) {
        AnalysisDefinition replacement = reportReplacementMap.get(report.getId());
        report = new InsightDescriptor(replacement.getAnalysisID(), replacement.getTitle(), replacement.getDataFeedID(), replacement.getReportType(),
                replacement.getUrlKey(), Roles.VIEWER, false);
    }

    public static DashboardElement loadReport(long elementID, EIConnection conn) throws SQLException {
        DashboardReport dashboardReport = null;
        PreparedStatement queryStmt = conn.prepareStatement("SELECT ANALYSIS.title, analysis.data_feed_id, analysis.report_type, analysis.analysis_id, analysis.url_key, " +
                "dashboard_report.label_placement, dashboard_report.show_label, dashboard_element.preferred_width, dashboard_element.preferred_height," +
                "dashboard_report.auto_calculate_height, dashboard_report.space_sides, dashboard_report.preferred_tag FROM " +
                "analysis, dashboard_report, dashboard_element WHERE dashboard_report.dashboard_element_id = dashboard_element.dashboard_element_id AND " +
                "dashboard_report.report_id = analysis.analysis_id AND dashboard_element.dashboard_element_id = ?");
        PreparedStatement tagNameStmt = conn.prepareStatement("SELECT ACCOUNT_TAG.TAG_NAME FROM ACCOUNT_TAG WHERE ACCOUNT_TAG_ID = ?");
        queryStmt.setLong(1, elementID);
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            dashboardReport = new DashboardReport();
            dashboardReport.setReport(new InsightDescriptor(rs.getLong(4), rs.getString(1), rs.getLong(2), rs.getInt(3), rs.getString(5), Roles.VIEWER, false));
            dashboardReport.setLabelPlacement(rs.getInt(6));
            dashboardReport.setShowLabel(rs.getBoolean(7));
            dashboardReport.setPreferredWidth(rs.getInt(8));
            dashboardReport.setPreferredHeight(rs.getInt(9));
            dashboardReport.setAutoCalculateHeight(rs.getBoolean(10));
            dashboardReport.setSpaceSides(rs.getBoolean(11));
            dashboardReport.setRecommendedTag(rs.getLong(12));
            dashboardReport.loadElement(elementID, conn);
            if (dashboardReport.getRecommendedTag() > 0) {
                tagNameStmt.setLong(1, dashboardReport.getRecommendedTag());
                ResultSet tagRS = tagNameStmt.executeQuery();
                if (tagRS.next()) {
                    String tagName = tagRS.getString(1);
                    dashboardReport.setTagName(tagName);
                }
            }
        }
        tagNameStmt.close();
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

    @Override
    public JSONObject toJSON(FilterHTMLMetadata filterHTMLMetadata, List<FilterDefinition> parentFilters) throws JSONException {
        WSAnalysisDefinition reportDefinition = new AnalysisService().openAnalysisDefinition(report.getId());
        if (getOverridenFilters() != null) {
            for (FilterDefinition filter : reportDefinition.getFilterDefinitions()) {
                FilterDefinition overrideFilter = getOverridenFilters().get(String.valueOf(filter.getFilterID()));
                if (overrideFilter != null) {
                    filter.override(overrideFilter);
                    filter.setEnabled(overrideFilter.isEnabled());
                }
            }
        }
        JSONObject reportJSON = super.toJSON(filterHTMLMetadata, parentFilters);
        reportJSON.put("type", "report");
        JSONObject reportDataJSON = new JSONObject();
        reportDataJSON.put("name", report.getName());
        reportDataJSON.put("id", report.getUrlKey());
        HTMLReportMetadata md = new HTMLReportMetadata();
        md.setEmbedded(filterHTMLMetadata.isEmbedded());
        reportDataJSON.put("metadata", reportDefinition.toJSON(md, parentFilters));
        reportJSON.put("report", reportDataJSON);
        reportJSON.put("show_label", isShowLabel());
        reportJSON.put("tag", tagName);

        return reportJSON;
    }

    public int requiredInitCount() {
        return 1;
    }
}
