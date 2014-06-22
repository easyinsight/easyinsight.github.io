package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSPlotChartDefinition;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.export.ExportMetadata;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * User: jamesboe
 * Date: 5/23/12
 * Time: 4:56 PM
 */
public class PlotChartServlet extends HtmlServlet {
    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata,
                           EIConnection conn, WSAnalysisDefinition report, ExportMetadata md) throws Exception {
        DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);

        JSONObject object = new JSONObject();
        // need series, need ticks
        WSPlotChartDefinition plotDefinition = (WSPlotChartDefinition) report;

        AnalysisItem dimension = plotDefinition.getDimension();
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
        Map<String, JSONArray> pointMap = new HashMap<>();
        for (IRow row : dataSet.getRows()) {


            String key;
            if (plotDefinition.getIconGrouping() == null) {
                key = "";
            } else {
                key = row.getValue(plotDefinition.getIconGrouping()).toString();
            }

            JSONArray points;
            points = pointMap.get(key);
            if (points == null) {
                points = new JSONArray();
                pointMap.put(key, points);
            }

            JSONObject p1 = new JSONObject();
            p1.put("x", row.getValue(plotDefinition.getXaxisMeasure()).toDouble());
            p1.put("y", row.getValue(plotDefinition.getYaxisMeasure()).toDouble());
            points.put(p1);
        }

        for (Map.Entry<String, JSONArray> entry : pointMap.entrySet()) {
            JSONObject point = new JSONObject();
            point.put("values", entry.getValue());
            point.put("key", entry.getKey());
            arrays.add(point);
        }

        object.put("point", true);

        object.put("values", arrays);
        configureAxes(object, plotDefinition, plotDefinition.getXaxisMeasure(), plotDefinition.getYaxisMeasure());


        response.setContentType("application/json");
        String jsonString = object.toString();
        response.getOutputStream().write(jsonString.getBytes());
        response.getOutputStream().flush();
    }

}
