package com.easyinsight.export;

import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.database.EIConnection;
import com.easyinsight.html.HtmlServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: jamesboe
 * Date: 6/6/12
 * Time: 1:07 PM
 */
public class EmailServlet extends HtmlServlet {
    @Override
    protected void doStuff(HttpServletRequest req, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata, EIConnection conn, WSAnalysisDefinition report) throws Exception {
        int format = Integer.parseInt(req.getParameter("format"));
        String recipient = req.getParameter("recipient");
        String subject = req.getParameter("subject");
        String body = req.getParameter("body");
        new ExportService().emailReport(report, format, insightRequestMetadata, recipient, subject, body, null, 0, 0);
    }
}
