package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
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
public class StackedColumnChartServlet extends HtmlServlet {

    private static interface Populator {
        JSONArray createArray(Double measure, Integer index);

        public Integer getIndex(JSONArray jsonArray) throws JSONException;
    }

    private static class ColumnPopulator implements Populator {

        public JSONArray createArray(Double measure, Integer index) {
            JSONArray point = new JSONArray();
            point.put(index);
            point.put(measure);
            return point;
        }

        public Integer getIndex(JSONArray jsonArray) throws JSONException {
            return jsonArray.getInt(0);
        }
    }

    private static class BarPopulator implements Populator {

        public JSONArray createArray(Double measure, Integer index) {
            JSONArray point = new JSONArray();
            point.put(measure);
            point.put(index);
            return point;
        }

        public Integer getIndex(JSONArray jsonArray) throws JSONException {
            return jsonArray.getInt(1);
        }
    }

    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata,
                           EIConnection conn, WSAnalysisDefinition report) throws Exception {
        DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);

        JSONObject object = new JSONObject();
        // need series, need ticks
        AnalysisItem xAxisItem;
        AnalysisItem measureItem;
        AnalysisItem stackItem;
        final Populator populator;
        if (report instanceof WSStackedColumnChartDefinition) {
            WSStackedColumnChartDefinition columnChartDefinition = (WSStackedColumnChartDefinition) report;
            xAxisItem = columnChartDefinition.getXaxis();
            stackItem = columnChartDefinition.getStackItem();
            measureItem = columnChartDefinition.getMeasures().get(0);
            populator = new ColumnPopulator();
        } else if (report instanceof WSStackedBarChartDefinition) {
            WSStackedBarChartDefinition columnChartDefinition = (WSStackedBarChartDefinition) report;
            xAxisItem = columnChartDefinition.getYaxis();
            stackItem = columnChartDefinition.getStackItem();
            measureItem = columnChartDefinition.getMeasures().get(0);
            populator = new BarPopulator();
        } else {
            throw new RuntimeException();
        }

        int i = 1;
        Map<String, List<JSONArray>> seriesMap = new HashMap<String, List<JSONArray>>();
        Map<String, Integer> indexMap = new HashMap<String, Integer>();

        JSONArray axisNames = new JSONArray();
        JSONArray series = new JSONArray();

        JSONObject params = new JSONObject();
        JSONObject seriesDefaults = new JSONObject();
        JSONObject rendererOptions = new JSONObject();


        seriesDefaults.put("rendererOptions", rendererOptions);

        params.put("seriesDefaults", seriesDefaults);
        object.put("params", params);

        Link l = stackItem.defaultLink();

        rendererOptions.put("highlightMouseOver", l != null);

        if (l != null && l instanceof DrillThrough) {
            JSONObject drillthrough = new JSONObject();
            drillthrough.put("reportID", report.getUrlKey());
            drillthrough.put("id", l.getLinkID());
            drillthrough.put("source", stackItem.getAnalysisItemID());
            drillthrough.put("xaxis", xAxisItem.getAnalysisItemID());
            drillthrough.put("stack", stackItem.getAnalysisItemID());
            object.put("drillthrough", drillthrough);
        }


        for (IRow row : dataSet.getRows()) {

            String xAxisValue = row.getValue(xAxisItem).toString();
            String stackValue = row.getValue(stackItem).toString();
            Integer index = indexMap.get(xAxisValue);
            if (index == null) {
                axisNames.put(xAxisValue);
                index = i++;
                indexMap.put(xAxisValue, index);
            }
            List<JSONArray> array = seriesMap.get(stackValue);
            if (array == null) {
                JSONObject seriesObj = new JSONObject();
                seriesObj.put("label", stackValue);
                series.put(seriesObj);
                array = new ArrayList<JSONArray>();
                seriesMap.put(stackValue, array);
            }
            Double measure = row.getValue(measureItem).toDouble();

            array.add(populator.createArray(measure, index));
        }

        for (List<JSONArray> array : seriesMap.values()) {
            Set<Integer> contained = new HashSet<Integer>();
            for (JSONArray anArray : array) {
                Integer point = populator.getIndex(anArray);
                contained.add(point);
            }
            for (int j = 1; j < i; j++) {
                if (!contained.contains(j)) {
                    array.add(populator.createArray(0., j));
                }
            }
            Collections.sort(array, new Comparator<JSONArray>() {

                public int compare(JSONArray jsonArray, JSONArray jsonArray1) {
                    try {
                        Integer i1 = populator.getIndex(jsonArray);
                        Integer i2 = populator.getIndex(jsonArray1);
                        return i1.compareTo(i2);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        JSONArray blahs = new JSONArray();
        for (Map.Entry<String, List<JSONArray>> entry : seriesMap.entrySet()) {
            blahs.put(entry.getValue());
        }

        object.put("values", blahs);
        object.put("ticks", axisNames);
        object.put("series", series);
        System.out.println(blahs);
        response.setContentType("application/json");
        response.getOutputStream().write(object.toString().getBytes());
        response.getOutputStream().flush();
    }
}
