package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.*;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
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
        Point createArray(Value measure, Value string, int index) throws JSONException;

        public Integer getIndex(JSONObject jsonObject) throws JSONException;

        public Double getMeasure(JSONObject jsonObject) throws JSONException;
    }

    private static class Point {
        private Value x;
        private Value y;
        private int index;

        private Point(Value x, Value y, int index) {
            this.x = x;
            this.y = y;
            this.index = index;
        }

        public Value getX() {
            return x;
        }

        public Value getY() {
            return y;
        }

        public int getIndex() {
            return index;
        }
    }

    private static class ColumnPopulator implements Populator {

        public Point createArray(Value measure, Value string, int index) throws JSONException {
            return new Point(string, measure, index);
        }

        public Integer getIndex(JSONObject jsonObject) throws JSONException {
            return jsonObject.getInt("index");
        }

        public Double getMeasure(JSONObject jsonObject) throws JSONException {
            return jsonObject.getDouble("y");
        }
    }

    private static class BarPopulator implements Populator {

        public Point createArray(Value measure, Value string, int index) throws JSONException {
            return new Point(string, measure, index);
            /*JSONObject point = new JSONObject();
            point.put("x", string);
            point.put("y", measure);
            point.put("index", index);
            return point;*/
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


        boolean dateAxis = false;
        boolean sortStackAscending;
        boolean sortStackDescending;
        boolean sortX = false;
        boolean sortY = false;
        boolean ascending = false;

        JSONObject seriesDefaults = new JSONObject();
        JSONObject object = new JSONObject();
        // need series, need ticks
        AnalysisItem xAxisItem;
        AnalysisItem measureItem;
        AnalysisItem stackItem;
        AnalysisItem minItem = null;
        final Populator populator;
        if (report instanceof WSStackedColumnChartDefinition) {
            WSStackedColumnChartDefinition columnChartDefinition = (WSStackedColumnChartDefinition) report;
            xAxisItem = columnChartDefinition.getXaxis();
            stackItem = columnChartDefinition.getStackItem();
            measureItem = columnChartDefinition.getMeasures().get(0);
            populator = new ColumnPopulator();
            sortStackAscending = "Stack Ascending".equals(columnChartDefinition.getStackSort());
            sortStackDescending = "Stack Descending".equals(columnChartDefinition.getStackSort());
            if ("X-Axis Descending".equals(columnChartDefinition.getColumnSort())) {
                sortX = true;
                ascending = false;
            } else if ("X-Axis Ascending".equals(columnChartDefinition.getColumnSort())) {
                sortX = true;
                ascending = true;
            } else if ("Y-Axis Descending".equals(columnChartDefinition.getColumnSort())) {
                sortY = true;
                ascending = false;
            } else if ("Y-Axis Ascending".equals(columnChartDefinition.getColumnSort())) {
                sortY = true;
                ascending = true;
            }
            if ("inside".equals(columnChartDefinition.getLabelPosition())) {
                seriesDefaults.put("pointLabels", pointLabels);
                pointLabels.put("labels", new JSONArray());
                object.put("valueLabel", true);
            }
        } else if (report instanceof WSStackedBarChartDefinition) {
            WSStackedBarChartDefinition columnChartDefinition = (WSStackedBarChartDefinition) report;
            xAxisItem = columnChartDefinition.getYaxis();
            stackItem = columnChartDefinition.getStackItem();
            measureItem = columnChartDefinition.getMeasures().get(0);
            minItem = columnChartDefinition.getMinimumXAxis();
            dateAxis = columnChartDefinition.isDateAxis();
            populator = new BarPopulator();
            if ("inside".equals(columnChartDefinition.getLabelPosition())) {
                seriesDefaults.put("pointLabels", pointLabels);
                pointLabels.put("labels", new JSONArray());
                object.put("valueLabel", true);
            }
            if ("Y-Axis Descending".equals(columnChartDefinition.getColumnSort())) {
                sortX = true;
                ascending = false;
            } else if ("Y-Axis Ascending".equals(columnChartDefinition.getColumnSort())) {
                sortX = true;
                ascending = true;
            } else if ("X-Axis Descending".equals(columnChartDefinition.getColumnSort())) {
                sortY = true;
                ascending = false;
            } else if ("X-Axis Ascending".equals(columnChartDefinition.getColumnSort())) {
                sortY = true;
                ascending = true;
            }
            sortStackAscending = "Stack Ascending".equals(columnChartDefinition.getStackSort());
            sortStackDescending = "Stack Descending".equals(columnChartDefinition.getStackSort());
        } else {
            throw new RuntimeException();
        }

        int i = 1;
        Map<Value, List<Point>> seriesMap = new LinkedHashMap<>();
        Map<Value, Integer> indexMap = new HashMap<>();
        Map<Integer, Value> reverseIndexMap = new HashMap<>();

        List<Value> axisNames = new ArrayList<>();


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

        Map<Value, Double> indexToMin = new HashMap<>();

        List<Map<String, Value>> seriesList = new ArrayList<>();

        for (IRow row : dataSet.getRows()) {

            Value xAxisValue = row.getValue(xAxisItem);
            Value sortValue = row.getValue(xAxisItem);
            //String xAxisValue = row.getValue(xAxisItem).toString();
            Value sValue = row.getValue(stackItem);
            //Value stackValue = sValue;
            Integer index = indexMap.get(xAxisValue);
            if (index == null) {
                axisNames.add(sortValue);
                index = i++;
                indexMap.put(xAxisValue, index);
                reverseIndexMap.put(index, xAxisValue);

                if (minItem != null) {
                    double minValue = row.getValue(minItem).toDouble();
                    indexToMin.put(xAxisValue, minValue);
                }
            }


            List<Point> array = seriesMap.get(sValue);
            if (array == null) {
                Map<String, Value> map = new HashMap<>();
                map.put("label", sValue);
                map.put("sort", sValue);
                seriesList.add(map);
                array = new ArrayList<>();
                seriesMap.put(sValue, array);
            }

            Value curValue = row.getValue(measureItem);

            // need to end up with...
            array.add(populator.createArray(curValue, xAxisValue, index));


        }

        if (sortStackAscending) {
            seriesList.sort((o1, o2) -> o1.get("sort").compareTo(o2.get("sort")));
        } else if (sortStackDescending) {
            seriesList.sort((o1, o2) -> o2.get("sort").compareTo(o1.get("sort")));
        }

        JSONArray series = new JSONArray();
        for (Map<String, Value> map : seriesList) {
            JSONObject seriesObject = new JSONObject();
            seriesObject.put("label", map.get("label"));
            series.put(seriesObject);
        }

        Map<Value, List<Point>> sortedSeriesMap = new LinkedHashMap<>();
        for (Map<String, Value> map : seriesList) {
            Value label = map.get("label");
            sortedSeriesMap.put(label, seriesMap.get(label));
        }
        seriesMap = sortedSeriesMap;


        for (List<Point> array : seriesMap.values()) {
            Set<Integer> contained = new HashSet<Integer>();
            for (Point anArray : array) {
                Integer point = anArray.getIndex();
                contained.add(point);
            }
            for (int j = 1; j < i; j++) {
                if (!contained.contains(j)) {
                    array.add(populator.createArray(new NumericValue(0.), reverseIndexMap.get(j), j));
                }
            }
            Collections.sort(array, (jsonArray, jsonArray1) -> {

                Integer i1 = jsonArray.getIndex();
                Integer i2 = jsonArray1.getIndex();
                return i1.compareTo(i2);

            });
        }

        /*for (List<Point> array : seriesMap.values()) {
            Iterator<Point> iter = array.iterator();
            while (iter.hasNext()) {
                Point pointer = iter.next();
                if (pointer.getY().toDouble() == 0) {
                    iter.remove();
                }
            }
        }*/

        /*Map<Value, List<Integer>> zeroIndicies = new HashMap<>();
        if (seriesMap.entrySet().size() > 0) {
            Map.Entry<Value, List<Point>> first = null;
            for (Map.Entry<Value, List<Point>> entry : seriesMap.entrySet()) {
                first = entry;
                break;
            }
            for (int k = 0; k < first.getValue().size(); k++) {
                Point jsonArray = first.getValue().get(k);
                Integer curIndex = jsonArray.getIndex();
                Double total = 0.0;
                for (Map.Entry<Value, List<Point>> entry : seriesMap.entrySet()) {
                    Double curVal = 0.0;
                    for (Point arr : entry.getValue()) {
                        if (arr.getIndex() == curIndex) {
                            curVal = arr.getY().toDouble();
                            break;
                        }
                    }
                    total = total + curVal;
                }
                if (total == 0.0) {
                    zeroIndicies.add(k);
                }
            }
        }*/
        // start from top removing them in reverse order should make it easier
//
        /*for (int k = zeroIndicies.size() - 1; k >= 0; k--) {
            for (Map.Entry<Value, List<Point>> entry : seriesMap.entrySet()) {
                entry.getValue().remove(entry.getValue().get(zeroIndicies.get(k)));
            }
            axisNames.remove(k);
        }*/


        // series map is keyed on stack value
        // needs to contain x axis value + y value

        JSONArray blahs = new JSONArray();
        int k = 0;
        double maxY = Double.MIN_VALUE;

        if (sortX) {
            Collections.sort(axisNames);
            if (!ascending) {
                Collections.reverse(axisNames);
            }
            for (List<Point> list : seriesMap.values()) {
                Collections.sort(list, (o1, o2) -> {

                    Value val1 = o1.getX();
                    Value val2 = o2.getX();
                    Integer i1 = axisNames.indexOf(val1);
                    Integer i2 = axisNames.indexOf(val2);
                    return (i1.compareTo(i2));

                });
            }
        } else if (sortY) {

            Map<Value, Double> map = new HashMap<>();
            for (List<Point> list : seriesMap.values()) {

                for (Point jo : list) {
                    Value x = jo.getX();
                    Double d = map.get(x);
                    if (d == null) {
                        map.put(x, jo.getY().toDouble());
                    } else {
                        map.put(x, jo.getY().toDouble() + d);
                    }

                }
            }
            Collections.sort(axisNames, (o1, o2) -> {
                Double d1 = map.get(o1);
                Double d2 = map.get(o2);
                if (d1 == null) {
                    d1 = 0.;
                }
                if (d2 == null) {
                    d2 = 0.;
                }
                return d1.compareTo(d2);
            });
            if (!ascending) {
                Collections.reverse(axisNames);
            }
            for (List<Point> list : seriesMap.values()) {
                Collections.sort(list, new Comparator<Point>() {

                    @Override
                    public int compare(Point o1, Point o2) {
                        Value val1 = o1.getX();
                        Value val2 = o2.getY();
                        return ((Integer) axisNames.indexOf(val1)).compareTo(axisNames.indexOf(val2));

                    }
                });
            }
        }
        Map<Value, Double> stackMap = new HashMap<>();

        for (Map.Entry<Value, List<Point>> entry : seriesMap.entrySet()) {

            double total = 0;
            for (Point arr : entry.getValue()) {
                total = total + arr.getY().toDouble();
            }
            if (total != 0) {
                JSONObject axisObject = new JSONObject();
                String keyString = ExportService.createValue(md, xAxisItem, entry.getKey(), true);
                axisObject.put("key", keyString);

                String color = colors.get(k % colors.size());
                axisObject.put("color", color);
                List<Point> list = entry.getValue();
                List<JSONObject> joList = new ArrayList<>(list.size());

                if (dateAxis) {
                    for (Point point : list) {
                        Value key = point.getX();
                        Double min = stackMap.get(key);
                        if (min == null) {
                            min = indexToMin.get(key);
                            stackMap.put(key, min);
                        }
                        Double y = point.getY().toDouble();
                        Double delta = y - min;
                        maxY = Math.max(maxY, y);
                        JSONObject jo = new JSONObject();
                        jo.put("y", delta);
                        jo.put("xMin", min);
                        String x = ExportService.createValue(md, xAxisItem, point.getX(), true);
                        jo.put("x", x);
                        joList.add(jo);
                        stackMap.put(key, (min + delta));
                    }
                } else {
                    for (Point point : list) {
                        JSONObject jo = new JSONObject();
                        String x = ExportService.createValue(md, xAxisItem, point.getX(), true);
                        jo.put("x", x);
                        jo.put("y", point.getY().toDouble());
                        joList.add(jo);
                    }
                }

                axisObject.put("values", joList);
                blahs.put(axisObject);
            }
            k++;
        }

        object.put("maxY", maxY);
        object.put("values", blahs);
        object.put("floatingY", minItem != null);
        configureAxes(object, (WSChartDefinition) report, xAxisItem, measureItem, md);

        if (dateAxis) {
            object.put("dateAxis", dateAxis);
        }

        response.setContentType("application/json");
        System.out.println(object.toString());
        response.getOutputStream().write(object.toString().getBytes());
        response.getOutputStream().flush();
    }
}
