package com.easyinsight.html;

import com.easyinsight.analysis.FilterHTMLMetadata;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.dashboard.DashboardElement;
import com.easyinsight.dashboard.DashboardService;
import com.easyinsight.security.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: jamesboe
 * Date: 6/12/12
 * Time: 10:05 AM
 */
public class DashboardPieceServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        SecurityUtil.populateThreadLocalFromSession(req);
        try {
            String elementStringID = req.getParameter("dashboardElementID");
            if (elementStringID.startsWith("ds")) {
                elementStringID = elementStringID.substring(2);
            }
            long dashboardID = Long.parseLong(req.getParameter("dashboardID"));
            long dashboardElementID = Long.parseLong(elementStringID);
            String drillThroughKey = req.getParameter("drillThroughKey");
            boolean embedded = false;
            String embeddedString = req.getParameter("embedded");
            if (embeddedString != null) {
                embedded = Boolean.parseBoolean(embeddedString);
            }
            Dashboard dashboard = new DashboardService().getDashboardView(dashboardID);
            DashboardElement element = dashboard.findElement(dashboardElementID);
            /*PreparedStatement ps = conn.prepareStatement("SELECT ELEMENT_TYPE FROM DASHBOARD_ELEMENT WHERE DASHBOARD_ELEMENT_ID = ?");
            ps.setLong(1, dashbboardElementID);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int elementType = rs.getInt(1);
            DashboardElement element = DashboardStorage.getElement(conn, dashbboardElementID, elementType);*/

            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            String html = element.toHTML(new FilterHTMLMetadata(dashboard, req, drillThroughKey, embedded));
            System.out.println("Retrieved " + html);
            response.getOutputStream().write(html.getBytes());
            response.getOutputStream().flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            SecurityUtil.clearThreadLocal();
        }
    }
}
