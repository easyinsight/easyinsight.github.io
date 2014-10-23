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
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 9/3/13
 * Time: 1:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class DashboardFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        if (req instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) req;
            String path = request.getServletPath();
            String dashboardID;
            Pattern p1 = Pattern.compile("^/html/dashboard/drillthrough/([A-Za-z0-9]+)/?.*$");
            Matcher m1 = p1.matcher(path);
            if (m1.matches()) {
                dashboardID = m1.replaceAll("$1");
                boolean allPublic = false;
                boolean embedPublic = false;
                try {
                    DrillThroughData drillThroughData = Utils.drillThroughFiltersForDashboard(dashboardID);
                    long actualDashboardID = drillThroughData.getDashboardID();
                    EIConnection conn = Database.instance().getConnection();
                    try {
                        PreparedStatement queryStmt = conn.prepareStatement("SELECT public_visible FROM DASHBOARD WHERE dashboard_id = ?");
                        queryStmt.setLong(1, actualDashboardID);
                        ResultSet rs = queryStmt.executeQuery();
                        if (rs.next()) {
                            allPublic = rs.getBoolean(1);
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

                Pattern p = Pattern.compile("^/html/dashboard/([a-zA-Z0-9]+)/?.*$");
                Matcher m = p.matcher(path);
                if(m.matches()) {
                    dashboardID = m.replaceAll("$1");
                } else {
                    Pattern pp = Pattern.compile("^/app/(html/)?embeddedDashboard/([a-zA-Z0-9]+)/?.*$");
                    m = pp.matcher(path);
                    dashboardID = m.replaceAll("$2");
                }
                request.setAttribute("public", new DashboardService().isDashboardPublic(dashboardID));
            }


        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
