package com.easyinsight.html;

import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.database.EIConnection;
import com.easyinsight.export.ExportMetadata;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Alan on 1/28/15.
 */
@WebServlet(value = "/reportData", asyncSupported = true)
public class ReportDataServlet extends HtmlServlet {

    @Override
    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata, EIConnection conn, WSAnalysisDefinition report, Object jsonObject, ExportMetadata md) throws Exception {
        JSONObject object = processData(insightRequestMetadata, conn, report, md);
        response.setContentType("application/json");
        response.getOutputStream().write(object.toString().getBytes());
        response.getOutputStream().flush();
    }

    public static JSONObject processData(InsightRequestMetadata insightRequestMetadata, EIConnection conn, WSAnalysisDefinition report, ExportMetadata md) throws JSONException {
        return ColumnChartServlet.processData(insightRequestMetadata, conn, report, md);
    }
}
