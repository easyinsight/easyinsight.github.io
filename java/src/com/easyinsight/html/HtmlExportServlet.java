package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.export.DeliveryScheduledTask;
import com.easyinsight.export.ExportProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: jamesboe
 * Date: 5/23/12
 * Time: 4:56 PM
 */
public class HtmlExportServlet extends HtmlServlet {

    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata,
                           EIConnection conn, WSAnalysisDefinition report) throws Exception {
        String html;
        try {
            String embeddedString = request.getParameter("embedded");
            boolean embedded = false;
            if (embeddedString != null) {
                embedded = Boolean.parseBoolean(embeddedString);
            }
            ExportProperties exportProperties = new ExportProperties();
            exportProperties.setEmbedded(embedded);
            exportProperties.setEmailed(false);
            html = DeliveryScheduledTask.createHTMLTable(conn, report, insightRequestMetadata, false, false, exportProperties);
        } catch (ReportException re) {
            html = re.getReportFault().toHTML();
        }
        if(html == null) {
            html = "";
        }
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.getOutputStream().write(html.getBytes());
        response.getOutputStream().flush();
    }
}
