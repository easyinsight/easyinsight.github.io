package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.*;
import com.easyinsight.core.Value;
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
public class StackedColumnChartServlet extends HtmlServlet {

    private static interface Populator {
        JSONObject createArray(Double measure, String string, int index) throws JSONException;

        public Integer getIndex(JSONObject jsonObject) throws JSONException;

        public Double getMeasure(JSONObject jsonObject) throws JSONException;
    }

    private static class ColumnPopulator implements Populator {

        public JSONObject createArray(Double measure, String string, int index) throws JSONException {
            JSONObject point = new JSONObject();
            point.put("x", string);
            point.put("y", measure);
            point.put("index", index);
            return point;
        }

        public Integer getIndex(JSONObject jsonObject) throws JSONException {
            return jsonObject.getInt("index");
        }

        public Double getMeasure(JSONObject jsonObject) throws JSONException {
            return jsonObject.getDouble("y");
        }
    }

    private static class BarPopulator implements Populator {

        public JSONObject createArray(Double measure, String string, int index) throws JSONException {
            JSONObject point = new JSONObject();
            point.put("x", string);
            point.put("y", measure);
            point.put("index", index);
            return point;
        }

        public Integer getIndex(JSONObject jsonObject) throws JSONException {
            return jsonObject.getInt("index");
        }

        public Double getMeasure(JSONObject jsonObject) throws JSONException {
            return jsonObject.getDouble("y");
        }
    }

    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata,
                           EIConnection conn, WSAnalysisDefinition report, ExportMetadata md) throws Exception {
        DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);

        JSONObject pointLabels = new JSONObject();




        JSONObject seriesDefaults = new JSONObject();
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
            if ("inside".equals(columnChartDefinition.getLabelPosition())) {
                seriesDefaults.put("pointLabels", pointLabels);
                pointLabels.put("labels", new JSONArray());
            }
        } else {
            throw new RuntimeException();
        }

        int i = 1;
        Map<String, List<JSONObject>> seriesMap = new LinkedHashMap<String, List<JSONObject>>();
        Map<String, Integer> indexMap = new HashMap<String, Integer>();
        Map<Integer, String> reverseIndexMap = new HashMap<Integer, String>();

        JSONArray axisNames = new JSONArray();
        JSONArray series = new JSONArray();

        JSONObject rendererOptions = new JSONObject();






        Link l = stackItem.defaultLink();

        rendererOptions.put("highlightMouseOver", l != null);

        if (l != null && l instanceof DrillThrough) {
            JSONObject drillthrough = new JSONObject();
            drillthrough.put("reportID", report.getUrlKey());
            drillthrough.put("id", l.createID());
            drillthrough.put("source", stackItem.getAnalysisItemID());
            drillthrough.put("xaxis", xAxisItem.getAnalysisItemID());
            drillthrough.put("stack", stackItem.getAnalysisItemID());
            object.put("drillthrough", drillthrough);
        }

        List<String> colors = ((WSChartDefinition) report).createMultiColors();


        for (IRow row : dataSet.getRows()) {

            String xAxisValue = row.getValue(xAxisItem).toString();
            String stackValue = row.getValue(stackItem).toString();
            Integer index = indexMap.get(xAxisValue);
            if (index == null) {
                axisNames.put(xAxisValue);
                index = i++;
                indexMap.put(xAxisValue, index);
                reverseIndexMap.put(index, xAxisValue);
            }


            List<JSONObject> array = seriesMap.get(stackValue);
            if (array == null) {
                JSONObject seriesObj = new JSONObject();
                seriesObj.put("label", stackValue);
                series.put(seriesObj);
                array = new ArrayList<JSONObject>();
                seriesMap.put(stackValue, array);
            }

            Value curValue = row.getValue(measureItem);
            Double measure = curValue.toDouble();
            // need to end up with...
            array.add(populator.createArray(measure, xAxisValue, index));
        }

        for (List<JSONObject> array : seriesMap.values()) {
            Set<Integer> contained = new HashSet<Integer>();
            for (JSONObject anArray : array) {
                Integer point = populator.getIndex(anArray);
                contained.add(point);
            }
            for (int j = 1; j < i; j++) {
                if (!contained.contains(j)) {
                    array.add(populator.createArray(0., reverseIndexMap.get(j), j));
                }
            }
            Collections.sort(array, new Comparator<JSONObject>() {

                public int compare(JSONObject jsonArray, JSONObject jsonArray1) {
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

        List<Integer> zeroIndicies = new ArrayList<Integer>();
        if (seriesMap.entrySet().size() > 0) {
            Map.Entry<String, List<JSONObject>> first = null;
            for (Map.Entry<String, List<JSONObject>> entry : seriesMap.entrySet()) {
                first = entry;
                break;
            }
            for (int k = 0; k < first.getValue().size(); k++) {
                JSONObject jsonArray = first.getValue().get(k);
                Integer curIndex = populator.getIndex(jsonArray);
                Double total = 0.0;
                for (Map.Entry<String, List<JSONObject>> entry : seriesMap.entrySet()) {
                    Double curVal = 0.0;
                    for (JSONObject arr : entry.getValue()) {
                        if (populator.getIndex(arr).equals(curIndex)) {
                            curVal = populator.getMeasure(arr);
                            break;
                        }
                    }
                    total = total + curVal;
                }
                if (total == 0.0) {
                    zeroIndicies.add(k);
                }
            }
        }
        // start from top removing them in reverse order should make it easier
//
        for (int k = zeroIndicies.size() - 1; k >= 0; k--) {
            for (Map.Entry<String, List<JSONObject>> entry : seriesMap.entrySet()) {
                entry.getValue().remove(entry.getValue().get(zeroIndicies.get(k)));
            }
            axisNames.remove(k);
        }


        // series map is keyed on stack value
        // needs to contain x axis value + y value

        JSONArray blahs = new JSONArray();
        int k = 0;
        //zeroIndicies = new ArrayList<Integer>();
        for (Map.Entry<String, List<JSONObject>> entry : seriesMap.entrySet()) {
            double total = 0;
            for (JSONObject arr : entry.getValue()) {
                total = total + populator.getMeasure(arr);
            }
            if (total != 0) {
                JSONObject axisObject = new JSONObject();
                axisObject.put("key", entry.getKey());

                String color = colors.get(k % colors.size());
                axisObject.put("color", color);
                axisObject.put("values", entry.getValue());
                blahs.put(axisObject);
            } else {
                zeroIndicies.add(k);
            }
            k++;
        }

        /*for (int j = zeroIndicies.size() - 1; j >= 0; j--) {
            series.remove(zeroIndicies.get(j));
        }*/

        object.put("values", blahs);
        configureAxes(object, (WSChartDefinition) report, xAxisItem, measureItem);

        response.setContentType("application/json");
        System.out.println(object.toString());
        response.getOutputStream().write(object.toString().getBytes());
        response.getOutputStream().flush();
    }
}
