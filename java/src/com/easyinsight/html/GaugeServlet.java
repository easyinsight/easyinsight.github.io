package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.export.ExportService;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.NumberFormat;
import java.util.Calendar;

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
        JSONObject jsonObject = new JSONObject();
        if(dataSet.getRows().size() > 0) {
            AnalysisMeasure analysisMeasure = (AnalysisMeasure) gaugeDefinition.getMeasure();
            Value value = dataSet.getRow(0).getValue(gaugeDefinition.getMeasure());
            jsonObject.put("value", value.toDouble());
            jsonObject.put("maxValue", gaugeDefinition.getMaxValue());
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(analysisMeasure.getPrecision());
            nf.setMinimumFractionDigits(analysisMeasure.getMinPrecision());
            String string = ExportService.createValue(0, analysisMeasure, value, Calendar.getInstance(), "$", false);
            jsonObject.put("formattedValue", string);
            if (gaugeDefinition.getBenchmarkMeasure() != null) {
                jsonObject.put("benchmark", "Benchmark: " + dataSet.getRow(0).getValue(gaugeDefinition.getBenchmarkMeasure()).toDouble());
            }
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getOutputStream().write(jsonObject.toString().getBytes());
        response.getOutputStream().flush();
    }
}
