package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.export.DeliveryScheduledTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

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
            html = DeliveryScheduledTask.createHTMLTable(conn, report, insightRequestMetadata, true, false);
        } catch (ReportException re) {
            html = re.getReportFault().toHTML();
        }
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.getOutputStream().write(html.getBytes());
        response.getOutputStream().flush();
    }
}
