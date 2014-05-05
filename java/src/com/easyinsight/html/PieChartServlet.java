package com.easyinsight.html;

import com.easyinsight.analysis.*;

import com.easyinsight.analysis.definitions.WSPieChartDefinition;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.export.ExportMetadata;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * User: jamesboe
 * Date: 5/23/12
 * Time: 4:56 PM
 */
public class PieChartServlet extends HtmlServlet {
    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata,
                           EIConnection conn, WSAnalysisDefinition report, ExportMetadata md) throws Exception {
        DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);
        WSChartDefinition chart = (WSChartDefinition) report;
        JSONObject object = new JSONObject();
        // need series, need ticks
        AnalysisItem xAxisItem;

        List<AnalysisItem> measures;

        JSONObject rendererOptions = new JSONObject();

        WSPieChartDefinition pieChart = (WSPieChartDefinition) report;
        xAxisItem = pieChart.getXaxis();
        measures = pieChart.getMeasures();



        // drillthroughs
        Link l = xAxisItem.defaultLink();

        rendererOptions.put("highlightMouseOver", l != null);

        if (l != null && l instanceof DrillThrough) {
            JSONObject drillthrough = new JSONObject();
            drillthrough.put("reportID", report.getUrlKey());
            drillthrough.put("id", l.createID());
            drillthrough.put("source", xAxisItem.getAnalysisItemID());
            drillthrough.put("xaxis", xAxisItem.getAnalysisItemID());
            object.put("drillthrough", drillthrough);
        }

        List<String> colors = chart.createMultiColors();

        JSONArray points = new JSONArray();
        int i = 0;
        AnalysisItem measureItem = measures.get(i);
        for (IRow row : dataSet.getRows()) {
            String x = row.getValue(xAxisItem).toString();
            JSONObject point = new JSONObject();
            point.put("value", row.getValue(measureItem).toDouble());
            point.put("label", x);
            String color = colors.get(i % colors.size());
            point.put("color", color);
            points.put(point);
            i++;
        }

        object.put("values", points);
        response.setContentType("application/json");
        response.getOutputStream().write(object.toString().getBytes());
        response.getOutputStream().flush();
    }
}
