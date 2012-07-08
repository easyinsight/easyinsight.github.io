package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSBarChartDefinition;
import com.easyinsight.analysis.definitions.WSColumnChartDefinition;
import com.easyinsight.analysis.definitions.WSPieChartDefinition;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 5/23/12
 * Time: 4:56 PM
 */
public class ColumnChartServlet extends HtmlServlet {
    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata,
                           EIConnection conn, WSAnalysisDefinition report) throws Exception {
        DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);

        JSONObject object = new JSONObject();
        // need series, need ticks
        AnalysisItem xAxisItem;

        List<AnalysisItem> measures;
        JSONArray blahArray = new JSONArray();

        if (report instanceof WSColumnChartDefinition) {
            WSColumnChartDefinition columnChartDefinition = (WSColumnChartDefinition) report;
            xAxisItem = columnChartDefinition.getXaxis();
            measures = columnChartDefinition.getMeasures();
        } else if (report instanceof WSBarChartDefinition) {
            WSBarChartDefinition columnChartDefinition = (WSBarChartDefinition) report;
            xAxisItem = columnChartDefinition.getYaxis();
            measures = columnChartDefinition.getMeasures();
        } else if (report instanceof WSPieChartDefinition) {
            WSPieChartDefinition pieChart = (WSPieChartDefinition) report;
            xAxisItem = pieChart.getXaxis();
            measures = pieChart.getMeasures();
        } else {
            throw new RuntimeException();
        }

        for (AnalysisItem measure : measures) {
            blahArray.put(new JSONArray());
        }

        List<String> ticks = new ArrayList<String>();


        for (IRow row : dataSet.getRows()) {

            if (report instanceof WSColumnChartDefinition) {
                ticks.add(row.getValue(xAxisItem).toString());
                for (int i = 0; i < measures.size(); i++) {
                    AnalysisItem measureItem = measures.get(i);
                    JSONArray array = blahArray.getJSONArray(i);
                    JSONArray val = new JSONArray();
                    array.put(val);
                    val.put(row.getValue(xAxisItem).toString());
                    val.put(row.getValue(measureItem).toDouble());
                }
            } else if (report instanceof WSBarChartDefinition) {
                ticks.add(row.getValue(xAxisItem).toString());
                for (int i = 0; i < measures.size(); i++) {
                    AnalysisItem measureItem = measures.get(i);
                    JSONArray array = blahArray.getJSONArray(i);
                    array.put(row.getValue(measureItem).toDouble());
                    array.put(row.getValue(xAxisItem).toString());
                }
            } else if (report instanceof WSPieChartDefinition) {
                ticks.add(row.getValue(xAxisItem).toString());
                for (int i = 0; i < measures.size(); i++) {
                    AnalysisItem measureItem = measures.get(i);
                    JSONArray array = blahArray.getJSONArray(i);
                    array.put(row.getValue(xAxisItem).toString());
                    array.put(row.getValue(measureItem).toDouble());
                }
            }
        }
        object.put("ticks", ticks);

        object.put("values", blahArray);
        System.out.println(blahArray);
        response.setContentType("application/json");
        response.getOutputStream().write(object.toString().getBytes());
        response.getOutputStream().flush();
    }
}
