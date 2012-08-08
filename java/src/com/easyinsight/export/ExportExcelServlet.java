package com.easyinsight.export;

import com.easyinsight.analysis.AnalysisService;
import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.database.EIConnection;
import com.easyinsight.html.HtmlServlet;
import com.easyinsight.security.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * User: jamesboe
 * Date: Oct 28, 2010
 * Time: 11:22:26 AM
 */
public class ExportExcelServlet extends HtmlServlet {

    @Override
    protected void doStuff(HttpServletRequest req, HttpServletResponse resp, InsightRequestMetadata insightRequestMetadata, EIConnection conn, WSAnalysisDefinition report) throws Exception {
       byte[] bytes = new ExportService().exportToExcel(report, insightRequestMetadata);
        resp.setContentType("application/excel");
        resp.setContentLength(bytes.length);
        String reportName = URLEncoder.encode(report.getName(), "UTF-8");
        resp.setHeader("Content-disposition","attachment; filename=" + reportName+".xls" );
        resp.getOutputStream().write(bytes);
        resp.getOutputStream().flush();
    }
}
