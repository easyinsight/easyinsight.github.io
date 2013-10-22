package com.easyinsight.dashboard;

import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.security.Roles;
import org.hibernate.Session;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 9/26/13
 * Time: 10:36 AM
 */
public class DashboardStackPositions {
    private Map<String, Integer> positions = new HashMap<String, Integer>();
    private Map<String, Map<String, FilterDefinition>> filterMap = new HashMap<String, Map<String, FilterDefinition>>();
    private Map<String, InsightDescriptor> reports = new HashMap<String, InsightDescriptor>();

    public Map<String, Map<String, FilterDefinition>> getFilterMap() {
        return filterMap;
    }

    public void setFilterMap(Map<String, Map<String, FilterDefinition>> filterMap) {
        this.filterMap = filterMap;
    }

    public Map<String, Integer> getPositions() {
        return positions;
    }

    public void setPositions(Map<String, Integer> positions) {
        this.positions = positions;
    }

    public Map<String, InsightDescriptor> getReports() {
        return reports;
    }

    public void setReports(Map<String, InsightDescriptor> reports) {
        this.reports = reports;
    }

    public long save(EIConnection conn, long dashboardID, long reportID) throws SQLException {
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO dashboard_state (dashboard_id, report_id) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
        if (dashboardID > 0) {
            insertStmt.setLong(1, dashboardID);
        } else {
            insertStmt.setNull(1, Types.BIGINT);
        }
        if (reportID > 0) {
            insertStmt.setLong(2, reportID);
        } else {
            insertStmt.setNull(2, Types.BIGINT);
        }
        insertStmt.execute();
        long id = Database.instance().getAutoGenKey(insertStmt);
        insertStmt.close();
        PreparedStatement savePositionStmt = conn.prepareStatement("INSERT INTO dashboard_state_stack_position (dashboard_state_id, url_key, stack_position) " +
                "values (?, ?, ?)");
        for (Map.Entry<String, Integer> entry : positions.entrySet()) {
            savePositionStmt.setLong(1, id);
            savePositionStmt.setString(2, entry.getKey());
            savePositionStmt.setInt(3, entry.getValue());
            savePositionStmt.execute();
        }
        savePositionStmt.close();
        PreparedStatement saveFilterStmt = conn.prepareStatement("INSERT INTO dashboard_state_to_filter (dashboard_state_id, source_filter_id, filter_key, filter_id) values (?, ?, ?, ?)");
        Session session = Database.instance().createSession(conn);
        for (Map.Entry<String, Map<String, FilterDefinition>> entry : filterMap.entrySet()) {
            String key = entry.getKey();
            for (Map.Entry<String, FilterDefinition> filterEntry : entry.getValue().entrySet()) {
                long sourceFilterID = Long.parseLong(filterEntry.getKey());
                FilterDefinition overridenFilter = filterEntry.getValue();
                try {
                    overridenFilter = overridenFilter.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
                overridenFilter.beforeSave(session);
                session.saveOrUpdate(overridenFilter);
                session.flush();
                saveFilterStmt.setLong(1, id);
                saveFilterStmt.setLong(2, sourceFilterID);
                saveFilterStmt.setString(3, key);
                saveFilterStmt.setLong(4, overridenFilter.getFilterID());
                saveFilterStmt.execute();
            }
        }

        session.close();
        saveFilterStmt.close();
        PreparedStatement saveReportStmt = conn.prepareStatement("INSERT INTO dashboard_state_to_report (dashboard_state_id, report_id, url_key) values (?, ?, ?)");
        for (Map.Entry<String, InsightDescriptor> entry : reports.entrySet()) {
            saveReportStmt.setLong(1, id);
            saveReportStmt.setLong(2, entry.getValue().getId());
            saveReportStmt.setString(3, entry.getKey());
            saveReportStmt.execute();
        }
        saveReportStmt.close();
        return id;
    }

    public void retrieve(EIConnection conn, long dashboardStateID) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT url_key, stack_position FROM dashboard_state_stack_position WHERE dashboard_state_id = ?");
        queryStmt.setLong(1, dashboardStateID);
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            String urlKey = rs.getString(1);
            int stackPosition = rs.getInt(2);
            positions.put(urlKey, stackPosition);
        }
        queryStmt.close();

        PreparedStatement queryFilterStmt = conn.prepareStatement("SELECT source_filter_id, filter_key, filter_id from dashboard_state_to_filter where dashboard_state_id = ?");
        queryFilterStmt.setLong(1, dashboardStateID);
        ResultSet filterRS = queryFilterStmt.executeQuery();
        Session session = Database.instance().createSession(conn);
        Map<String, Map<String, FilterDefinition>> filters = new HashMap<String, Map<String, FilterDefinition>>();
        while (filterRS.next()) {
            long sourceFilterID = filterRS.getLong(1);
            String filterKey = filterRS.getString(2);
            long filterID = filterRS.getLong(3);
            List results = session.createQuery("from FilterDefinition where filterID = ?").setLong(0, filterID).list();
            if (results.size() > 0) {
                FilterDefinition filterDefinition = (FilterDefinition) results.get(0);
                filterDefinition.afterLoad();
                Map<String, FilterDefinition> map = filters.get(filterKey);
                if (map == null) {
                    map = new HashMap<String, FilterDefinition>();
                    filters.put(filterKey, map);
                }
                map.put(String.valueOf(sourceFilterID), filterDefinition);
            }
        }
        session.close();
        queryFilterStmt.close();
        setFilterMap(filters);

        PreparedStatement queryReportStmt = conn.prepareStatement("SELECT report_id, dashboard_state_to_report.url_key, analysis.title, analysis.account_visible, " +
                "analysis.url_key, analysis.data_feed_id, analysis.report_type FROM dashboard_state_to_report, analysis WHERE dashboard_state_id = ? AND " +
                "dashboard_state_to_report.report_id = analysis.analysis_id");
        queryReportStmt.setLong(1, dashboardStateID);
        ResultSet reportRS = queryReportStmt.executeQuery();
        while (reportRS.next()) {
            long reportID = reportRS.getLong(1);
            String urlKey = reportRS.getString(2);
            String title = reportRS.getString(3);
            boolean accountVisible = reportRS.getBoolean(4);
            String reportURLKey = reportRS.getString(5);
            long dataSourceID = reportRS.getLong(6);
            int reportType = reportRS.getInt(7);
            reports.put(urlKey, new InsightDescriptor(reportID, title, dataSourceID, reportType, reportURLKey, Roles.OWNER, accountVisible));
        }
        queryReportStmt.close();
    }
}
