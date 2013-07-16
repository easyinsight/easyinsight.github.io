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
import java.util.*;

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

        JSONObject params = new JSONObject();
        JSONObject seriesDefaults = new JSONObject();
        JSONObject rendererOptions = new JSONObject();
        JSONObject pointLabels = new JSONObject();
        int fontSize = 0, fontColor = 0;

        seriesDefaults.put("rendererOptions", rendererOptions);

        params.put("seriesDefaults", seriesDefaults);
        object.put("params", params);
        Comparator c = null;

        JSONObject axes = new JSONObject();
        JSONObject yAxis = new JSONObject();
        JSONObject xAxis = new JSONObject();
        axes.put("yaxis", yAxis);
        axes.put("xaxis", xAxis);


        if (report instanceof WSColumnChartDefinition) {
            WSColumnChartDefinition columnChartDefinition = (WSColumnChartDefinition) report;
            xAxisItem = columnChartDefinition.getXaxis();
            measures = columnChartDefinition.getMeasures();
            String sortType = columnChartDefinition.getColumnSort();
            if ("X-Axis Ascending".equals(sortType)) {
                c = new RowComparator(xAxisItem, true);
            } else if ("X-Axis Descending".equals(sortType)) {
                c = new RowComparator(xAxisItem, false);
            } else if ("Y-Axis Ascending".equals(sortType)) {
                c = new RowComparator(measures.get(0), true);
            } else if ("Y-Axis Descending".equals(sortType)) {
                c = new RowComparator(measures.get(0), false);
            }
            xAxis.put("label", xAxisItem.toDisplay());
            yAxis.put("label", measures.get(0).toDisplay());
            params.put("axes", axes);

            if ("auto".equals(columnChartDefinition.getLabelPosition())) {
                seriesDefaults.put("pointLabels", pointLabels);
                pointLabels.put("labels", new JSONArray());
                fontColor = columnChartDefinition.getLabelOutsideFontColor();
                fontSize = columnChartDefinition.getLabelFontSize();
            }
        } else if (report instanceof WSBarChartDefinition) {
            WSBarChartDefinition columnChartDefinition = (WSBarChartDefinition) report;
            xAxisItem = columnChartDefinition.getYaxis();
            measures = columnChartDefinition.getMeasures();
            String sortType = columnChartDefinition.getColumnSort();
            if ("X-Axis Ascending".equals(sortType)) {
                c = new RowComparator(measures.get(0), true);
            } else if ("X-Axis Descending".equals(sortType)) {
                c = new RowComparator(measures.get(0), false);
            } else if ("Y-Axis Ascending".equals(sortType)) {
                c = new RowComparator(xAxisItem, true);
            } else if ("Y-Axis Descending".equals(sortType)) {
                c = new RowComparator(xAxisItem, false);
            }
            xAxis.put("label", measures.get(0).toDisplay());
            yAxis.put("label", xAxisItem.toDisplay());
            params.put("axes", axes);
            if ("auto".equals(columnChartDefinition.getLabelPosition())) {
                seriesDefaults.put("pointLabels", pointLabels);
                pointLabels.put("labels", new JSONArray());
                fontColor = columnChartDefinition.getLabelOutsideFontColor();
                fontSize = columnChartDefinition.getLabelFontSize();
            }
        } else if (report instanceof WSPieChartDefinition) {
            WSPieChartDefinition pieChart = (WSPieChartDefinition) report;
            xAxisItem = pieChart.getXaxis();
            measures = pieChart.getMeasures();
            if ("auto".equals(pieChart.getLabelPosition())) {
                seriesDefaults.put("pointLabels", pointLabels);
                pointLabels.put("labels", new JSONArray());
            }
        } else {
            throw new RuntimeException();
        }

        // drillthroughs
        Link l = xAxisItem.defaultLink();

        rendererOptions.put("highlightMouseOver", l != null);

        if (l != null && l instanceof DrillThrough) {
            JSONObject drillthrough = new JSONObject();
            drillthrough.put("reportID", report.getUrlKey());
            drillthrough.put("id", l.getLinkID());
            drillthrough.put("source", xAxisItem.getAnalysisItemID());
            drillthrough.put("xaxis", xAxisItem.getAnalysisItemID());
            object.put("drillthrough", drillthrough);
        }

        for (AnalysisItem measure : measures) {
            blahArray.put(new JSONArray());
        }

        List<String> ticks = new ArrayList<String>();

        if (c != null)
            Collections.sort(dataSet.getRows(), c);

        for (IRow row : dataSet.getRows()) {

            ticks.add(row.getValue(xAxisItem).toString());
            for (int i = 0; i < measures.size(); i++) {
                AnalysisItem measureItem = measures.get(i);
                JSONArray array = blahArray.getJSONArray(i);
                JSONArray val = new JSONArray();
                array.put(val);
                if (report instanceof WSBarChartDefinition) {
                    val.put(row.getValue(measureItem).toDouble());
                    val.put(row.getValue(xAxisItem).toString());
                } else {
                    val.put(row.getValue(xAxisItem).toString());
                    val.put(row.getValue(measureItem).toDouble());
                }
                if (seriesDefaults.get("pointLabels") != null && seriesDefaults.has("pointLabels")) {
                    ((JSONArray) ((JSONObject) seriesDefaults.get("pointLabels")).get("labels")).put(row.getValue(measureItem).toDouble());
                }
            }

        }
        object.put("ticks", ticks);

        object.put("values", blahArray);
        response.setContentType("application/json");
        response.getOutputStream().write(object.toString().getBytes());
        response.getOutputStream().flush();
    }
}
