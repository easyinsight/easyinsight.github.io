package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 5/23/12
 * Time: 4:56 PM
 */
public class StackedColumnChartServlet extends HtmlServlet {
    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata,
                           EIConnection conn, WSAnalysisDefinition report) throws Exception {
        DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);

        JSONObject object = new JSONObject();
        // need series, need ticks
        AnalysisItem xAxisItem;
        AnalysisItem measureItem;
        AnalysisItem stackItem;
        if (report instanceof WSStackedColumnChartDefinition) {
            WSStackedColumnChartDefinition columnChartDefinition = (WSStackedColumnChartDefinition) report;
            xAxisItem = columnChartDefinition.getXaxis();
            stackItem = columnChartDefinition.getStackItem();
            measureItem = columnChartDefinition.getMeasures().get(0);
        } else if (report instanceof WSStackedBarChartDefinition) {
            WSStackedBarChartDefinition columnChartDefinition = (WSStackedBarChartDefinition) report;
            xAxisItem = columnChartDefinition.getYaxis();
            stackItem = columnChartDefinition.getStackItem();
            measureItem = columnChartDefinition.getMeasures().get(0);
        } else {
            throw new RuntimeException();
        }

        int i = 1;
        Map<String, JSONArray> seriesMap = new HashMap<String, JSONArray>();
        Map<String, Integer> indexMap = new HashMap<String, Integer>();

        JSONArray axisNames = new JSONArray();
        JSONArray series = new JSONArray();

        for (IRow row : dataSet.getRows()) {
            if (report instanceof WSStackedColumnChartDefinition) {
                String xAxisValue = row.getValue(xAxisItem).toString();
                String stackValue = row.getValue(stackItem).toString();
                Integer index = indexMap.get(xAxisValue);
                if (index == null) {
                    axisNames.put(xAxisValue);
                    index = i++;
                    indexMap.put(xAxisValue, index);
                }
                JSONArray array = seriesMap.get(stackValue);
                if (array == null) {
                    JSONObject seriesObj = new JSONObject();
                    seriesObj.put("label", stackValue);
                    series.put(seriesObj);
                    array = new JSONArray();
                    seriesMap.put(stackValue, array);
                }
                JSONArray point = new JSONArray();
                point.put(index);
                Double measure = row.getValue(measureItem).toDouble();
                point.put(measure);
                array.put(point);
            } else if (report instanceof WSStackedBarChartDefinition) {
                String xAxisValue = row.getValue(xAxisItem).toString();
                String stackValue = row.getValue(stackItem).toString();
                Integer index = indexMap.get(xAxisValue);
                if (index == null) {
                    axisNames.put(xAxisValue);
                    index = i++;
                    indexMap.put(xAxisValue, index);
                }
                JSONArray array = seriesMap.get(stackValue);
                if (array == null) {
                    JSONObject seriesObj = new JSONObject();
                    seriesObj.put("label", stackValue);
                    series.put(seriesObj);
                    array = new JSONArray();
                    seriesMap.put(stackValue, array);
                }
                JSONArray point = new JSONArray();
                Double measure = row.getValue(measureItem).toDouble();
                point.put(measure);
                point.put(index);
                array.put(point);
            }
        }

        JSONArray blahs = new JSONArray();
        for (Map.Entry<String, JSONArray> entry : seriesMap.entrySet()) {
            blahs.put(entry.getValue());
        }

        // object.put("ticks", ticks);
        //JSONArray blahArray = new JSONArray(blahs);
        object.put("values", blahs);
        object.put("ticks", axisNames);
        object.put("series", series);
        System.out.println(blahs);
        response.setContentType("application/json");
        response.getOutputStream().write(object.toString().getBytes());
        response.getOutputStream().flush();
    }
}
