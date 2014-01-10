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
public class PlotChartServlet extends HtmlServlet {
    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata,
                           EIConnection conn, WSAnalysisDefinition report) throws Exception {
        DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);

        JSONObject object = new JSONObject();
        // need series, need ticks
        WSPlotChartDefinition plotDefinition = (WSPlotChartDefinition) report;

        JSONObject params = new JSONObject();
        JSONObject axes = ((WSChartDefinition) report).getAxes();
        params.put("axes", axes);
        object.put("params", params);
        List<JSONArray> arrays = new ArrayList<JSONArray>();

        for (IRow row : dataSet.getRows()) {
            JSONArray point = new JSONArray();
            point.put(row.getValue(plotDefinition.getXaxisMeasure()).toDouble());
            point.put(row.getValue(plotDefinition.getYaxisMeasure()).toDouble());
            point.put(3);
            point.put(row.getValue(plotDefinition.getDimension()).toString());
            arrays.add(point);
        }


        // object.put("ticks", ticks);
        JSONArray a = new JSONArray();
        a.put(arrays);
        object.put("values", a);

        response.setContentType("application/json");
        String argh = object.toString();
        response.getOutputStream().write(argh.getBytes());
        response.getOutputStream().flush();
    }
}
