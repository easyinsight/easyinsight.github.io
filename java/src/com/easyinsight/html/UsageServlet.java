package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 4/15/14
 * Time: 10:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class UsageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reportIDString = req.getParameter("reportID");
        if (req.getSession().getAttribute("userID") != null) {
            SecurityUtil.populateThreadLocalFromSession(req);
        }
        InsightResponse insightResponse = new AnalysisService().openAnalysisIfPossible(reportIDString);
        long reportID;
        if (insightResponse.getStatus() == InsightResponse.SUCCESS) {
            reportID = insightResponse.getInsightDescriptor().getId();
        } else {
            throw new com.easyinsight.security.SecurityException();
        }
        EIConnection conn = Database.instance().getConnection();
        try {
            WSAnalysisDefinition report = new AnalysisService().openAnalysisDefinition(reportID);
            Usage u = new AnalysisService().whatUsesReport(report);
            ExportMetadata md = ExportService.createExportMetadata(SecurityUtil.getAccountID(), conn, new InsightRequestMetadata());
            JSONObject jo = u.toJSON(md);
            resp.setContentType("application/json");
            resp.getOutputStream().write(jo.toString().getBytes());
            resp.getOutputStream().flush();
            resp.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
            resp.setHeader("Pragma", "no-cache"); //HTTP 1.0
            resp.setDateHeader("Expires", 0); //prevents caching at the proxy server
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }

    }
}
