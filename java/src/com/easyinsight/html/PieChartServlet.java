package com.easyinsight.html;

import com.easyinsight.analysis.*;

import com.easyinsight.analysis.definitions.WSPieChartDefinition;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        double sum = 0;

        Map<IRow, Double> percentOfTotals = new HashMap<>();
        for (IRow row : dataSet.getRows()) {
            sum += row.getValue(measureItem).toDouble();
        }

        for (IRow row : dataSet.getRows()) {
            percentOfTotals.put(row, row.getValue(measureItem).toDouble() / sum * 100);
        }

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(1);
        nf.setMinimumFractionDigits(1);

        for (IRow row : dataSet.getRows()) {
            Value dimValue = row.getValue(xAxisItem);
            String x = dimValue.toString();
            JSONObject point = new JSONObject();
            Value value = row.getValue(measureItem);
            point.put("value", value.toDouble());
            point.put("label", x);
            point.put("total", sum);
            point.put("percent", nf.format(percentOfTotals.get(row)));
            String color = colors.get(i % colors.size());
            if (value.getValueExtension() != null && value.getValueExtension() instanceof TextValueExtension) {
                TextValueExtension textValueExtension = (TextValueExtension) value.getValueExtension();
                if (textValueExtension.getColor() > 0) {
                    color = ExportService.createHexString(textValueExtension.getColor());
                }
            } else if (dimValue.getValueExtension() != null && dimValue.getValueExtension() instanceof TextValueExtension) {
                TextValueExtension textValueExtension = (TextValueExtension) dimValue.getValueExtension();
                if (textValueExtension.getColor() > 0) {
                    color = String.format("#%06X", (0xFFFFFF & textValueExtension.getColor()));
                }
            }
            point.put("color", color);
            points.put(point);
            i++;
        }
        object.put("yFormat", createFormatObject(measureItem, md));
        object.put("values", points);
        object.put("pieLabelStyle", pieChart.getLabelType());
        object.put("showLegend", pieChart.isShowLegend());
        if (pieChart.getDonutRatio() > 0) {
            object.put("donut", true);
            object.put("donutRatio", pieChart.getDonutRatio());
        }
        response.setContentType("application/json");
        response.getOutputStream().write(object.toString().getBytes());
        response.getOutputStream().flush();
    }
}
