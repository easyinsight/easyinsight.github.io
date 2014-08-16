package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.security.SecurityUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.msgpack.util.json.JSON;

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
    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata, EIConnection conn, WSAnalysisDefinition report, ExportMetadata md) throws Exception {
        DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);
        WSGaugeDefinition gaugeDefinition = (WSGaugeDefinition) report;
        JSONObject jsonObject = new JSONObject();
        if (!"Bullet".equals(gaugeDefinition.getGaugeModel())) {
            if (dataSet.getRows().size() > 0) {
                AnalysisMeasure analysisMeasure = (AnalysisMeasure) gaugeDefinition.getMeasure();
                Value value = dataSet.getRow(0).getValue(gaugeDefinition.getMeasure());
                jsonObject.put("value", value.toDouble());
                double maxValue;
                if (gaugeDefinition.getMaxValueMeasure() != null) {
                    maxValue = dataSet.getRow(0).getValue(gaugeDefinition.getMaxValueMeasure()).toDouble();
                } else {
                    maxValue = gaugeDefinition.getMaxValue();
                }
                jsonObject.put("maxValue", maxValue);

                double alert1;
                double alert2;
                if (gaugeDefinition.getAlert1Measure() != null) {
                    Value alert1Value = dataSet.getRow(0).getValue(gaugeDefinition.getAlert1Measure());
                    alert1 = alert1Value.toDouble();
                } else {
                    alert1 = gaugeDefinition.getAlertPoint1();
                }
                if (gaugeDefinition.getAlert2Measure() != null) {
                    Value alert2Value = dataSet.getRow(0).getValue(gaugeDefinition.getAlert2Measure());
                    alert2 = alert2Value.toDouble();
                } else {
                    alert2 = gaugeDefinition.getAlertPoint2();
                }

                jsonObject.put("colorBands", gaugeDefinition.createColorBands(alert1, alert2, maxValue));

                NumberFormat nf = NumberFormat.getInstance();
                nf.setMaximumFractionDigits(analysisMeasure.getPrecision());
                nf.setMinimumFractionDigits(analysisMeasure.getMinPrecision());
                ExportMetadata exportMetadata = ExportService.createExportMetadata(SecurityUtil.getAccountID(false), conn, insightRequestMetadata);
                String string = ExportService.createValue(0, analysisMeasure, value, Calendar.getInstance(), exportMetadata.currencySymbol, exportMetadata.locale, false);
                jsonObject.put("formattedValue", string);
                if (gaugeDefinition.getBenchmarkMeasure() != null) {
                    Value benchmarkValue = dataSet.getRow(0).getValue(gaugeDefinition.getBenchmarkMeasure());
                    jsonObject.put("benchmark", gaugeDefinition.getBenchmarkLabel() + ": <b>" + ExportService.createValue(0, analysisMeasure, benchmarkValue, Calendar.getInstance(), exportMetadata.currencySymbol,
                            exportMetadata.locale, false) + "</b>");
                }
                jsonObject.put("gaugePrecision", ((AnalysisMeasure) gaugeDefinition.getMeasure()).getPrecision());
            }
        } else {
            if (dataSet.getRows().size() > 0) {
                JSONObject valuesObject = new JSONObject();

                Value value = dataSet.getRow(0).getValue(gaugeDefinition.getMeasure());
                JSONArray measureArray = new JSONArray();
                measureArray.put(value.toDouble());
                valuesObject.put("measures", measureArray);

                if (gaugeDefinition.getBenchmarkMeasure() != null) {
                    Value benchmarkValue = dataSet.getRow(0).getValue(gaugeDefinition.getBenchmarkMeasure());
                    JSONArray benchmarkArray = new JSONArray();
                    benchmarkArray.put(benchmarkValue.toDouble());
                    valuesObject.put("markers", benchmarkArray);
                } else {
                    JSONArray benchmarkArray = new JSONArray();
                    benchmarkArray.put(0);
                    valuesObject.put("markers", benchmarkArray);
                }

                JSONArray ranges = new JSONArray();
                if (gaugeDefinition.getAlert1Measure() != null) {
                    Value alert1Value = dataSet.getRow(0).getValue(gaugeDefinition.getAlert1Measure());
                    ranges.put(alert1Value.toDouble());
                } else {
                    ranges.put(gaugeDefinition.getAlertPoint1());
                }
                if (gaugeDefinition.getAlert2Measure() != null) {
                    Value alert2Value = dataSet.getRow(0).getValue(gaugeDefinition.getAlert2Measure());
                    ranges.put(alert2Value.toDouble());
                } else {
                    ranges.put(gaugeDefinition.getAlertPoint2());
                }

                if (gaugeDefinition.getMaxValueMeasure() != null) {
                    Value maxValue = dataSet.getRow(0).getValue(gaugeDefinition.getMaxValueMeasure());
                    ranges.put(maxValue.toDouble());
                } else {
                    ranges.put(gaugeDefinition.getMaxValue());
                }

                valuesObject.put("ranges", ranges);

                valuesObject.put("title", gaugeDefinition.getMeasure().toUnqualifiedDisplay());

                JSONArray array = new JSONArray();
                array.put(valuesObject);

                jsonObject.put("values", array);
            }
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getOutputStream().write(jsonObject.toString().getBytes());
        response.getOutputStream().flush();
    }
}
