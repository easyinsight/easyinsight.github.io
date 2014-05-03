package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSBubbleChartDefinition;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.export.ExportMetadata;
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
                           EIConnection conn, WSAnalysisDefinition report, ExportMetadata md) throws Exception {
        DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);

        JSONObject object = new JSONObject();
        WSBubbleChartDefinition bubbleDefinition = (WSBubbleChartDefinition) report;
        AnalysisItem dimension = bubbleDefinition.getDimension();

        Link l = dimension.defaultLink();
        if (l != null && l instanceof DrillThrough) {
            JSONObject drillthrough = new JSONObject();
            drillthrough.put("reportID", report.getUrlKey());
            drillthrough.put("id", l.createID());
            drillthrough.put("source", dimension.getAnalysisItemID());
            drillthrough.put("xaxis", dimension.getAnalysisItemID());
            object.put("drillthrough", drillthrough);
        }


        List<JSONObject> arrays = new ArrayList<JSONObject>();

        for (IRow row : dataSet.getRows()) {
            JSONObject point = new JSONObject();
            JSONArray points = new JSONArray();
            JSONObject p1 = new JSONObject();
            p1.put("x", row.getValue(bubbleDefinition.getXaxisMeasure()).toDouble());
            p1.put("y", row.getValue(bubbleDefinition.getYaxisMeasure()).toDouble());
            p1.put("size", row.getValue(bubbleDefinition.getZaxisMeasure()).toDouble());
            points.put(p1);
            point.put("values", points);
            String val = row.getValue(bubbleDefinition.getDimension()).toString();
            point.put("key", val);
            arrays.add(point);
        }



        object.put("values", arrays);
        configureAxes(object, bubbleDefinition, bubbleDefinition.getXaxisMeasure(), bubbleDefinition.getYaxisMeasure());

        response.setContentType("application/json");
        String argh = object.toString();
        response.getOutputStream().write(argh.getBytes());
        response.getOutputStream().flush();
    }
}
