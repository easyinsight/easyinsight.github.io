package com.easyinsight.dashboard;

import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisStorage;
import com.easyinsight.analysis.ReportMetrics;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedConsumer;
import com.easyinsight.email.UserStub;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.util.RandomTextGenerator;
import org.hibernate.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: Nov 26, 2010
 * Time: 1:24:52 PM
 */
public class DashboardService {

    private DashboardStorage dashboardStorage = new DashboardStorage();

    public long canAccessDashboard(String urlKey) {
        try {
            return SecurityUtil.authorizeDashboard(urlKey);
        } catch (SecurityException e) {
            return 0;
        }
    }

    public void keepDashboard(long dashboardID) {
        SecurityUtil.authorizeDashboard(dashboardID);
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            Dashboard dashboard = getDashboard(dashboardID);
            Set<Long> reportIDs = dashboard.containedReports();
            List<AnalysisDefinition> reports = new ArrayList<AnalysisDefinition>();

            for (Long containedReportID : reportIDs) {
                AnalysisDefinition report = new AnalysisStorage().getPersistableReport(containedReportID, session);
                reports.add(report);
                Set<Long> containedReportIDs = report.containedReportIDs();
                for (Long childReportID : containedReportIDs) {
                    reports.add(new AnalysisStorage().getPersistableReport(childReportID, session));
                }
            }
            for (AnalysisDefinition report : reports) {
                report.setTemporaryReport(false);
                session.update(report);
            }
            session.flush();
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE DASHBOARD SET TEMPORARY_DASHBOARD = ? where dashboard_id = ?");
            updateStmt.setBoolean(1, false);
            updateStmt.setLong(2, dashboardID);
            updateStmt.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public Dashboard saveDashboard(Dashboard dashboard) {
        if (dashboard.getId() > 0) {
            SecurityUtil.authorizeDashboard(dashboard.getId());
        }
        try {
            if (dashboard.getUrlKey() == null) {
                dashboard.setUrlKey(RandomTextGenerator.generateText(15));
                dashboard.setCreationDate(new Date());
                dashboard.setAuthorName(SecurityUtil.getUserName());
            }
            dashboard.setUpdateDate(new Date());
            if (dashboard.getAdministrators() == null || dashboard.getAdministrators().isEmpty()) {
                UserStub userStub = new UserStub();
                userStub.setUserID(SecurityUtil.getUserID());
                dashboard.setAdministrators(Arrays.asList((FeedConsumer) userStub));
            }
            dashboardStorage.saveDashboard(dashboard);
            return dashboard;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public ReportMetrics rateDashboard(long dashboardID, int rating) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT dashboard_user_rating_id from dashboard_user_rating where " +
                    "user_id = ? and dashboard_id = ?");
            queryStmt.setLong(1, SecurityUtil.getUserID());
            queryStmt.setLong(2, dashboardID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long id = rs.getLong(1);
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE dashboard_user_rating set rating = ? where dashboard_user_rating_id = ?");
                updateStmt.setInt(1, rating);
                updateStmt.setLong(2, id);
                updateStmt.executeUpdate();
            } else {
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO dashboard_user_rating (rating, user_id, dashboard_id) values (?, ?, ?)");
                insertStmt.setInt(1, rating);
                insertStmt.setLong(2, SecurityUtil.getUserID());
                insertStmt.setLong(3, dashboardID);
                insertStmt.execute();
            }
            PreparedStatement ratingStmt = conn.prepareStatement("SELECT AVG(RATING), COUNT(RATING) FROM DASHBOARD_USER_RATING WHERE DASHBOARD_ID = ?");
            ratingStmt.setLong(1, dashboardID);
            ResultSet dashboardRS = ratingStmt.executeQuery();
            dashboardRS.next();
            double ratingAverage = dashboardRS.getDouble(1);
            int ratingCount = dashboardRS.getInt(2);
            return new ReportMetrics(ratingCount, ratingAverage, rating);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public Dashboard getDashboard(long dashboardID) {
        SecurityUtil.authorizeDashboard(dashboardID);
        try {
            return dashboardStorage.getDashboard(dashboardID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void deleteDashboard(long dashboardID) {
        SecurityUtil.authorizeDashboard(dashboardID);
        try {
            dashboardStorage.deleteDashboard(dashboardID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
