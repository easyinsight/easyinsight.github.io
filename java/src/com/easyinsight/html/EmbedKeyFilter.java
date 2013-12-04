package com.easyinsight.html;

import com.easyinsight.analysis.AnalysisService;
import com.easyinsight.dashboard.DashboardService;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Alan
 * Date: 11/4/13
 * Time: 11:49 AM
 */
public class EmbedKeyFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        if (req instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) req;
            String path = request.getServletPath();
            String embedKey = null;

            if (path.contains("report")) {
                String reportID;
                Pattern p1 = Pattern.compile("^/html/user/([A-Za-z0-9]+)/report/drillthrough/([A-Za-z0-9]+)/?.*$");
                Matcher m1 = p1.matcher(path);
                if (m1.matches()) {
                    embedKey = m1.replaceAll("$1");
                    reportID = m1.replaceAll("$2");
                    boolean allPublic = false;
                    boolean embedPublic = false;
                    try {
                        DrillThroughData drillThroughData = Utils.drillThroughFiltersForReport(reportID);
                        long actualReportID = drillThroughData.getReportID();
                        EIConnection conn = Database.instance().getConnection();
                        try {
                            PreparedStatement queryStmt = conn.prepareStatement("SELECT publicly_visible, public_with_key FROM ANALYSIS WHERE analysis_id = ?");
                            queryStmt.setLong(1, actualReportID);
                            ResultSet rs = queryStmt.executeQuery();
                            if (rs.next()) {
                                allPublic = rs.getBoolean(1);
                                embedPublic = rs.getBoolean(2);
                            }
                            queryStmt.close();
                        } finally {
                            Database.closeConnection(conn);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    request.setAttribute("public", allPublic || embedPublic);
                } else {
                    Pattern p = Pattern.compile("^/html/user/([a-zA-Z0-9]+)/report/([a-zA-Z0-9]+)/?.*$");
                    Matcher m = p.matcher(path);
                    if(m.matches()) {
                        embedKey = m.replaceAll("$1");
                        reportID = m.replaceAll("$2");
                        request.setAttribute("public", new AnalysisService().isReportPublic(reportID) || (embedKey != null && new AnalysisService().isReportEmbedKeyVisible(reportID)));
                    }
                }
            } else if (path.contains("dashboard")) {
                String dashboardID;
                Pattern p1 = Pattern.compile("^/html/user/([A-Za-z0-9]+)/dashboard/drillthrough/([A-Za-z0-9]+)/?.*$");
                Matcher m1 = p1.matcher(path);
                if (m1.matches()) {
                    embedKey = m1.replaceAll("$1");
                    dashboardID = m1.replaceAll("$2");
                    boolean allPublic = false;
                    boolean embedPublic = false;
                    try {
                        DrillThroughData drillThroughData = Utils.drillThroughFiltersForDashboard(dashboardID);
                        long actualDashboardID = drillThroughData.getDashboardID();
                        EIConnection conn = Database.instance().getConnection();
                        try {
                            PreparedStatement queryStmt = conn.prepareStatement("SELECT public_visible, embed_with_key FROM DASHBOARD WHERE dashboard_id = ?");
                            queryStmt.setLong(1, actualDashboardID);
                            ResultSet rs = queryStmt.executeQuery();
                            if (rs.next()) {
                                allPublic = rs.getBoolean(1);
                                embedPublic = rs.getBoolean(2);
                            }
                            queryStmt.close();
                        } finally {
                            Database.closeConnection(conn);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    request.setAttribute("public", allPublic || embedPublic);
                } else {
                    Pattern p = Pattern.compile("^/html/user/([a-zA-Z0-9]+)/dashboard/([a-zA-Z0-9]+)/?.*$");
                    Matcher m = p.matcher(path);
                    if(m.matches()) {
                        embedKey = m.replaceAll("$1");
                        dashboardID = m.replaceAll("$2");
                        request.setAttribute("public", new DashboardService().isDashboardPublic(dashboardID) || (embedKey != null && new DashboardService().isDashboardPublicWithKey(dashboardID)));
                    }
                }
            }
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
