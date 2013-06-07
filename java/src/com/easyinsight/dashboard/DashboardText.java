package com.easyinsight.dashboard;

import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.FilterHTMLMetadata;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.scorecard.Scorecard;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: jamesboe
 * Date: Nov 26, 2010
 * Time: 1:25:20 PM
 */
public class DashboardText extends DashboardElement {
    private String text;
    private int fontSize;
    private int color;

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int getType() {
        return DashboardElement.TEXT;
    }

    @Override
    public long save(EIConnection conn) throws SQLException {
        long id = super.save(conn);
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO DASHBOARD_TEXT (DASHBOARD_ELEMENT_ID, dashboard_text, color, font_size) " +
                "VALUES (?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
        insertStmt.setLong(1, getElementID());
        insertStmt.setString(2, text);
        insertStmt.setInt(3, color);
        insertStmt.setInt(4, fontSize);
        insertStmt.execute();
        return id;
    }

    @Override
    public JSONObject toJSON(FilterHTMLMetadata metadata, List<FilterDefinition> parentFilters) throws JSONException {
        JSONObject textObject = super.toJSON(metadata, parentFilters);
        textObject.put("id", getElementID());
        textObject.put("type", "text");
        textObject.put("item", text);
        return textObject;
    }

    @Override
    public Set<Long> containedReports() {
        return new HashSet<Long>();
    }

    @Override
    public Set<Long> containedScorecards() {
        return new HashSet<Long>();
    }

    @Override
    public void updateScorecardIDs(Map<Long, Scorecard> scorecardReplacementMap) {
    }

    @Override
    public void visit(IDashboardVisitor dashboardVisitor) {
        dashboardVisitor.accept(this);
    }

    @Override
    public void updateReportIDs(Map<Long, AnalysisDefinition> reportReplacementMap, List<AnalysisItem> allFields, boolean changingDataSource, FeedDefinition dataSource) {

    }

    public static DashboardElement loadImage(long elementID, EIConnection conn) throws SQLException {
        DashboardText dashboardReport = null;
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DASHBOARD_TEXT.dashboard_text, color, font_size from dashboard_text " +
                "where dashboard_element_id = ?");
        queryStmt.setLong(1, elementID);
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            dashboardReport = new DashboardText();
            dashboardReport.setText(rs.getString(1));
            dashboardReport.setColor(rs.getInt(2));
            dashboardReport.setFontSize(rs.getInt(3));
            dashboardReport.loadElement(elementID, conn);
        }
        return dashboardReport;
    }
}
