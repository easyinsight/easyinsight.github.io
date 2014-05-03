package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSBarChartDefinition;
import com.easyinsight.analysis.definitions.WSColumnChartDefinition;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.export.ExportMetadata;
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
                           EIConnection conn, WSAnalysisDefinition report, ExportMetadata md) throws Exception {
        DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);
        WSChartDefinition chart = (WSChartDefinition) report;
        JSONObject object = new JSONObject();
        // need series, need ticks
        AnalysisItem xAxisItem;

        List<AnalysisItem> measures;
        JSONArray blahArray = new JSONArray();

        Comparator c = null;

        int baseColor;
        boolean valueLabel;
        boolean logarithmic = false;
        if (report instanceof WSColumnChartDefinition) {
            WSColumnChartDefinition columnChartDefinition = (WSColumnChartDefinition) report;
            valueLabel = "auto".equals(columnChartDefinition.getLabelPosition());
            baseColor = columnChartDefinition.getChartColor();
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
            if ("Logarithmic".equals(columnChartDefinition.getAxisType())) {
                logarithmic = true;
            }
        } else if (report instanceof WSBarChartDefinition) {
            WSBarChartDefinition columnChartDefinition = (WSBarChartDefinition) report;
            valueLabel = "auto".equals(columnChartDefinition.getLabelPosition());
            baseColor = columnChartDefinition.getChartColor();
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
            if ("Logarithmic".equals(columnChartDefinition.getAxisType())) {
                logarithmic = true;
            }
        } else {
            throw new RuntimeException();
        }


        // drillthroughs
        Link l = xAxisItem.defaultLink();

        if (l != null && l instanceof DrillThrough) {
            JSONObject drillthrough = new JSONObject();
            drillthrough.put("reportID", report.getUrlKey());
            drillthrough.put("id", l.createID());
            drillthrough.put("source", xAxisItem.getAnalysisItemID());
            drillthrough.put("xaxis", xAxisItem.getAnalysisItemID());
            object.put("drillthrough", drillthrough);
        }

        List<String> colors;
        if (measures.size() == 1) {
            colors = Arrays.asList(String.format("#%06X", (0xFFFFFF & baseColor)));
        } else {
            colors = chart.createMultiColors();
        }

        if (c != null)
            Collections.sort(dataSet.getRows(), c);

        for (int i = 0; i < measures.size(); i++) {
            JSONObject axisObject = new JSONObject();
            JSONArray points = new JSONArray();
            AnalysisItem measureItem = measures.get(i);
            for (IRow row : dataSet.getRows()) {
                String x = row.getValue(xAxisItem).toString();
                JSONObject point = new JSONObject();
                point.put("x", x);
                Value measureValue = row.getValue(measureItem);
                point.put("y", measureValue.toDouble());
                if (colors.size() == 1) {
                    String color = null;
                    if (measureValue.getValueExtension() != null && measureValue.getValueExtension() instanceof TextValueExtension) {
                        TextValueExtension textValueExtension = (TextValueExtension) measureValue.getValueExtension();
                        if (textValueExtension.getColor() > 0) {
                            color = String.format("#%06X", (0xFFFFFF & textValueExtension.getColor()));
                        }
                    }
                    if (color == null) {
                        color = colors.get(0);
                    }
                    point.put("color", color);
                }
                points.put(point);
            }
            axisObject.put("key", measureItem.toUnqualifiedDisplay());
            String color = colors.get(i % colors.size());
            axisObject.put("color", color);
            axisObject.put("values", points);
            blahArray.put(axisObject);
        }
        if (measures.size() == 1) {
            configureAxes(object, chart, xAxisItem, measures.get(0));
        } else {
            configureAxes(object, chart, xAxisItem, measures);
        }
        if (valueLabel) {
            object.put("valueLabel", true);
        }
        if (measures.size() == 1) {
            object.put("showLegend", false);
            object.put("oneMeasure", true);
        }
        if (logarithmic) {
            object.put("yLog", true);
        }

        object.put("values", blahArray);
        response.setContentType("application/json");
        response.getOutputStream().write(object.toString().getBytes());
        response.getOutputStream().flush();
    }
}
