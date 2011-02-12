package com.easyinsight.export;

import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * User: jamesboe
 * Date: Oct 28, 2010
 * Time: 11:22:26 AM
 */
public class HTMLImageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("hrm");
        int width = Integer.parseInt(req.getParameter("width"));
        int height = Integer.parseInt(req.getParameter("height")) - 100;
        WSAnalysisDefinition report = (WSAnalysisDefinition) req.getSession().getAttribute("report");
        long reportID = report.getAnalysisID();
        req.getSession().removeAttribute("imageID");
        Long userID = (Long) req.getSession().getAttribute("userID");
        Long accountID = (Long) req.getSession().getAttribute("accountID");
        EIConnection conn = Database.instance().getConnection();
        try {
            long requestID = new SeleniumLauncher().requestSeleniumDrawForMobile(reportID,
                    userID, accountID, conn, width, height);
            byte[] bytes = HtmlResultCache.getInstance().waitForResults(requestID);
            if (bytes != null) {
                resp.setContentType("application/x-download");
                resp.setContentLength(bytes.length);

                resp.setHeader("Content-disposition","inline; filename=" + report.getName()+".png" );
                resp.getOutputStream().write(bytes);
                resp.getOutputStream().flush();
            }
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }
}
