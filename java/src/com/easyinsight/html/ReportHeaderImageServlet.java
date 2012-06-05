package com.easyinsight.html;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.preferences.ApplicationSkin;
import com.easyinsight.preferences.PreferencesService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: jamesboe
 * Date: 5/24/12
 * Time: 2:50 PM
 */
public class ReportHeaderImageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ApplicationSkin applicationSkin = (ApplicationSkin) req.getSession().getAttribute("uiSettings");
        if (applicationSkin.getReportHeaderImage() != null) {
            EIConnection conn = Database.instance().getConnection();
            try {
                byte[] bytes = new PreferencesService().getImage(applicationSkin.getReportHeaderImage().getId(), conn);
                resp.setContentLength(bytes.length);
                resp.setContentType("image/png");
                resp.setHeader("Content-disposition","inline; filename=" + applicationSkin.getReportHeaderImage().getName() );
                resp.getOutputStream().write(bytes);
                resp.getOutputStream().flush();
            } finally {
                Database.closeConnection(conn);
            }
        }
    }
}
