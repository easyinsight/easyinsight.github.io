package com.easyinsight.html;


import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.analysis.WSChartDefinition;
import com.easyinsight.analysis.WSGaugeDefinition;
import com.easyinsight.database.EIConnection;
import com.easyinsight.export.DashboardPDF;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.itextpdf.text.Element;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: jamesboe
 * Date: 5/3/14
 * Time: 2:03 PM
 */
public class HTMLPNGServlet extends HtmlServlet {
    @Override
    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata, EIConnection conn, WSAnalysisDefinition report, ExportMetadata md) throws Exception {
        // if it's a standard type of report, otherwise
        int pdfWidth = (int) Double.parseDouble(request.getParameter("pdfWidth"));
        int pdfHeight = (int) Double.parseDouble(request.getParameter("pdfHeight"));
        byte[] bytes = DashboardPDF.generatePNG(report, pdfWidth, pdfHeight, conn);
        String urlKey = new ExportService().exportToPNG(report.getName(), bytes, conn, request);
        JSONObject object = new JSONObject();
        object.put("urlKey", urlKey);
        response.setContentType("application/json");
        response.getOutputStream().write(object.toString().getBytes());
        response.getOutputStream().flush();
    }
}
