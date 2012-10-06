package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSBubbleChartDefinition;
import com.easyinsight.analysis.definitions.WSPlotChartDefinition;
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
public class BubbleChartServlet extends HtmlServlet {
    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata,
                           EIConnection conn, WSAnalysisDefinition report) throws Exception {
        DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);

        JSONObject object = new JSONObject();
        // need series, need ticks
        AnalysisMeasure xAxisMeasure;
        AnalysisMeasure yAxisMeasure;
        AnalysisMeasure zAxisMeasure = null;
        AnalysisItem dimension;
        if (report instanceof WSBubbleChartDefinition) {
            WSBubbleChartDefinition bubbleDefinition = (WSBubbleChartDefinition) report;
            xAxisMeasure = (AnalysisMeasure) bubbleDefinition.getXaxisMeasure();
            yAxisMeasure = (AnalysisMeasure) bubbleDefinition.getYaxisMeasure();
            zAxisMeasure = (AnalysisMeasure) bubbleDefinition.getZaxisMeasure();
            dimension = bubbleDefinition.getDimension();
        } else {
            WSPlotChartDefinition plotDefinition = (WSPlotChartDefinition) report;
            xAxisMeasure = (AnalysisMeasure) plotDefinition.getXaxisMeasure();
            yAxisMeasure = (AnalysisMeasure) plotDefinition.getYaxisMeasure();
            dimension = plotDefinition.getDimension();
        }


        List<JSONArray> arrays = new ArrayList<JSONArray>();

        for (IRow row : dataSet.getRows()) {
            JSONArray point = new JSONArray();
            point.put(row.getValue(xAxisMeasure).toDouble());
            point.put(row.getValue(yAxisMeasure).toDouble());
            if (zAxisMeasure == null) {
                point.put(20);
            } else {
                point.put(row.getValue(zAxisMeasure).toDouble());
            }
            point.put(row.getValue(dimension).toString());
            arrays.add(point);
        }


        // object.put("ticks", ticks);
        JSONArray a = new JSONArray();
        a.put(arrays);
        object.put("values", a);

        response.setContentType("application/json");
        String argh = object.toString();
        System.out.println(argh);
        response.getOutputStream().write(argh.getBytes());
        response.getOutputStream().flush();
    }
}
