package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 11/21/13
 * Time: 10:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReportInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SecurityUtil.populateThreadLocalFromSession(req);
        try {
            String reportIDString = req.getParameter("reportID");
            InsightResponse insightResponse = new AnalysisService().openAnalysisIfPossible(reportIDString);
            Long reportID = insightResponse.getInsightDescriptor().getId();
            WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(reportID);
            HTMLReportMetadata reportMetadata = new HTMLReportMetadata();
            try {
                JSONObject jo = report.toJSON(reportMetadata, new ArrayList<FilterDefinition>());
                resp.setContentType("application/json");
                resp.getOutputStream().write(jo.toString().getBytes());
                resp.getOutputStream().flush();
            } catch (JSONException e) {
                LogClass.error(e);
            }
        } finally {
            SecurityUtil.clearThreadLocal();
        }

    }
}
