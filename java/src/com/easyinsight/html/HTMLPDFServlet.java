package com.easyinsight.html;


import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.analysis.WSChartDefinition;
import com.easyinsight.analysis.WSGaugeDefinition;
import com.easyinsight.analysis.definitions.WSDiagramDefinition;
import com.easyinsight.analysis.definitions.WSMap;
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
public class HTMLPDFServlet extends HtmlServlet {
    @Override
    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata, EIConnection conn, WSAnalysisDefinition report, ExportMetadata md) throws Exception {
        // if it's a standard type of report, otherwise
        Element element = null;
        if (report instanceof WSChartDefinition || report instanceof WSGaugeDefinition || report instanceof WSDiagramDefinition || report instanceof WSMap) {
            int pdfWidth = (int) Double.parseDouble(request.getParameter("pdfWidth"));
            int pdfHeight = (int) Double.parseDouble(request.getParameter("pdfHeight"));
            /*double r = 1540.0 / pdfWidth;
            int height = (int) (r * pdfHeight);*/
            //System.out.println(height);
            element = DashboardPDF.generatePDF(report, pdfWidth, pdfHeight, conn);
        }
        String urlKey = new ExportService().toListPDFInDatabase(report, conn, insightRequestMetadata, request, element);
        JSONObject object = new JSONObject();
        object.put("urlKey", urlKey);
        response.setContentType("application/json");
        response.getOutputStream().write(object.toString().getBytes());
        response.getOutputStream().flush();
    }
}
