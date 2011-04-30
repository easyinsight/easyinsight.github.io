package com.easyinsight.dashboard;

import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.database.EIConnection;
import com.easyinsight.scorecard.Scorecard;
import com.easyinsight.scorecard.ScorecardDescriptor;
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
public class DashboardScorecard extends DashboardElement {
    private ScorecardDescriptor scorecard;
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

    public ScorecardDescriptor getScorecard() {
        return scorecard;
    }

    public void setScorecard(ScorecardDescriptor scorecard) {
        this.scorecard = scorecard;
    }

    @Override
    public int getType() {
        return DashboardElement.SCORECARD;
    }

    @Override
    public long save(EIConnection conn) throws SQLException {
        long id = super.save(conn);
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO DASHBOARD_SCORECARD (DASHBOARD_ELEMENT_ID, SCORECARD_ID, LABEL_PLACEMENT, SHOW_LABEL) " +
                "VALUES (?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
        insertStmt.setLong(1, getElementID());
        insertStmt.setLong(2, scorecard.getId());
        insertStmt.setInt(3, labelPlacement);
        insertStmt.setBoolean(4, showLabel);
        insertStmt.execute();
        return id;
    }

    @Override
    public Set<Long> containedReports() {
        return new HashSet<Long>();
    }

    @Override
    public Set<Long> containedScorecards() {
        Set<Long> ids = new HashSet<Long>();
        ids.add(scorecard.getId());
        return ids;
    }

    @Override
    public void updateScorecardIDs(Map<Long, Scorecard> scorecardReplacementMap) {
        Scorecard replacement = scorecardReplacementMap.get(scorecard.getId());
        scorecard = new ScorecardDescriptor(replacement.getName(), replacement.getScorecardID(), replacement.getUrlKey(), replacement.getDataSourceID());
    }

    @Override
    public void updateReportIDs(Map<Long, AnalysisDefinition> reportReplacementMap) {
        /*AnalysisDefinition replacement = reportReplacementMap.get(report.getId());
        report = new InsightDescriptor(replacement.getAnalysisID(), replacement.getTitle(), replacement.getDataFeedID(), replacement.getReportType(),
                replacement.getUrlKey(), Roles.SUBSCRIBER);*/
    }

    public static DashboardElement loadReport(long elementID, EIConnection conn) throws SQLException {
        DashboardScorecard dashboardReport = null;
        PreparedStatement queryStmt = conn.prepareStatement("SELECT SCORECARD.scorecard_name, scorecard.data_source_id, scorecard.scorecard_id, scorecard.url_key, " +
                "dashboard_scorecard.label_placement, dashboard_scorecard.show_label from " +
                "scorecard, dashboard_scorecard where dashboard_scorecard.dashboard_element_id = ? and dashboard_scorecard.scorecard_id = scorecard.scorecard_id");
        queryStmt.setLong(1, elementID);
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            dashboardReport = new DashboardScorecard();
            dashboardReport.setScorecard(new ScorecardDescriptor(rs.getString(1), rs.getLong(3), rs.getString(4), rs.getLong(2)));
            dashboardReport.setLabelPlacement(rs.getInt(5));
            dashboardReport.setShowLabel(rs.getBoolean(6));
        }
        queryStmt.close();
        return dashboardReport;
    }
}
