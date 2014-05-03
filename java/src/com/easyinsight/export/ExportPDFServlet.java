package com.easyinsight.export;

import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.database.EIConnection;
import com.easyinsight.html.HtmlServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * User: jamesboe
 * Date: Oct 28, 2010
 * Time: 11:22:26 AM
 */
public class ExportPDFServlet extends HtmlServlet {

    @Override
    protected void doStuff(HttpServletRequest req, HttpServletResponse resp, InsightRequestMetadata insightRequestMetadata, EIConnection conn, WSAnalysisDefinition report, ExportMetadata md) throws Exception {
        byte[] bytes = new ExportService().exportToPDFBytes(report, insightRequestMetadata);
        resp.setContentType("application/pdf");
        resp.setContentLength(bytes.length);
        String reportName = URLEncoder.encode(report.getName(), "UTF-8");
        resp.setHeader("Content-Disposition","attachment; filename=" + reportName+".pdf" );
        resp.getOutputStream().write(bytes);
        resp.getOutputStream().flush();
    }
}
