package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSBubbleChartDefinition;
import com.easyinsight.analysis.definitions.WSTwoAxisDefinition;
import com.easyinsight.core.DateValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        WSBubbleChartDefinition bubbleDefinition = (WSBubbleChartDefinition) report;

        List<JSONArray> arrays = new ArrayList<JSONArray>();

        for (IRow row : dataSet.getRows()) {
            JSONArray point = new JSONArray();
            point.put(row.getValue(bubbleDefinition.getXaxisMeasure()).toDouble());
            point.put(row.getValue(bubbleDefinition.getYaxisMeasure()).toDouble());
            point.put(row.getValue(bubbleDefinition.getZaxisMeasure()).toDouble());
            point.put(row.getValue(bubbleDefinition.getDimension()).toString());
            arrays.add(point);
        }


        // object.put("ticks", ticks);

        object.put("values", arrays);

        response.setContentType("application/json");
        String argh = object.toString();
        System.out.println(argh);
        response.getOutputStream().write(argh.getBytes());
        response.getOutputStream().flush();
    }
}
