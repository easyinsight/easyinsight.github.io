package com.easyinsight.html;

import com.easyinsight.analysis.DataService;
import com.easyinsight.analysis.InsightRequestMetadata;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.analysis.WSGaugeDefinition;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: jamesboe
 * Date: 7/5/12
 * Time: 3:29 PM
 */
public class GaugeServlet extends HtmlServlet {
    @Override
    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata, EIConnection conn, WSAnalysisDefinition report) throws Exception {
        DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);
        WSGaugeDefinition gaugeDefinition = (WSGaugeDefinition) report;
        Value value = dataSet.getRow(0).getValue(gaugeDefinition.getMeasure());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("value", value.toDouble());
        if (gaugeDefinition.getBenchmarkMeasure() != null) {
            jsonObject.put("benchmark", "Benchmark: " + dataSet.getRow(0).getValue(gaugeDefinition.getBenchmarkMeasure()).toDouble());
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getOutputStream().write(jsonObject.toString().getBytes());
        response.getOutputStream().flush();
    }
}
