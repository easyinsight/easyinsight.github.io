package com.easyinsight.html;

import com.easyinsight.analysis.FilterHTMLMetadata;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.dashboard.DashboardElement;
import com.easyinsight.dashboard.DashboardService;
import com.easyinsight.dashboard.DashboardStorage;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.security.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * User: jamesboe
 * Date: 6/12/12
 * Time: 10:05 AM
 */
public class DashboardPieceServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        SecurityUtil.populateThreadLocalFromSession(req);
        EIConnection conn = Database.instance().getConnection();
        try {
            String elementStringID = req.getParameter("dashboardElementID");
            if (elementStringID.startsWith("ds")) {
                elementStringID = elementStringID.substring(2);
            }
            long dashboardID = Long.parseLong(req.getParameter("dashboardID"));
            Dashboard dashboard = new DashboardService().getDashboard(dashboardID);
            long dashbboardElementID = Long.parseLong(elementStringID);
            PreparedStatement ps = conn.prepareStatement("SELECT ELEMENT_TYPE FROM DASHBOARD_ELEMENT WHERE DASHBOARD_ELEMENT_ID = ?");
            ps.setLong(1, dashbboardElementID);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int elementType = rs.getInt(1);
            DashboardElement element = DashboardStorage.getElement(conn, dashbboardElementID, elementType);
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            response.getOutputStream().write(element.toHTML(new FilterHTMLMetadata(dashboard)).getBytes());
            response.getOutputStream().flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
            SecurityUtil.clearThreadLocal();
        }
    }
}
