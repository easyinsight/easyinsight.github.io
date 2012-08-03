package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.*;

/**
 * User: James Boe
 * Date: Mar 20, 2009
 * Time: 7:23:14 PM
 */
public class WSColumnChartDefinition extends WSXAxisDefinition {

    private int chartColor;
    private int gradientColor;
    private boolean useChartColor;
    private String columnSort;
    private String axisType = "Linear";

    public String getAxisType() {
        return axisType;
    }

    public void setAxisType(String axisType) {
        this.axisType = axisType;
    }

    public int getGradientColor() {
        return gradientColor;
    }

    public void setGradientColor(int gradientColor) {
        this.gradientColor = gradientColor;
    }

    public int getChartType() {
        return ChartDefinitionState.COLUMN_2D;
    }

    public int getChartFamily() {
        return ChartDefinitionState.COLUMN_FAMILY;
    }

    public String getColumnSort() {
        return columnSort;
    }

    public void setColumnSort(String columnSort) {
        this.columnSort = columnSort;
    }

    public int getChartColor() {
        return chartColor;
    }

    public void setChartColor(int chartColor) {
        this.chartColor = chartColor;
    }

    public boolean isUseChartColor() {
        return useChartColor;
    }

    public void setUseChartColor(boolean useChartColor) {
        this.useChartColor = useChartColor;
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        chartColor = (int) findNumberProperty(properties, "chartColor", 0);
        gradientColor = (int) findNumberProperty(properties, "gradientColor", 0);
        useChartColor = findBooleanProperty(properties, "useChartColor", false);
        columnSort = findStringProperty(properties, "columnSort", "Unsorted");
        axisType = findStringProperty(properties, "axisType", "Linear");
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportNumericProperty("chartColor", chartColor));
        properties.add(new ReportNumericProperty("gradientColor", gradientColor));
        properties.add(new ReportBooleanProperty("useChartColor", useChartColor));
        properties.add(new ReportStringProperty("columnSort", columnSort));
        properties.add(new ReportStringProperty("axisType", axisType));
        return properties;
    }

    @Override
    public List<String> javaScriptIncludes() {
        List<String> includes = super.javaScriptIncludes();
        includes.add("/js/plugins/jqplot.barRenderer.min.js");
        includes.add("/js/plugins/jqplot.categoryAxisRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasTextRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasAxisLabelRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasAxisTickRenderer.min.js");
        includes.add("/js/visualizations/chart.js");
        includes.add("/js/visualizations/util.js");
        return includes;
    }

    @Override
    public String rootHTML() {
        return "<div id=\"chartpseudotooltip\"></div>";
    }

    @Override
    public String toHTML(String targetDiv) {
        String color;
        if (useChartColor) {
            color = String.format("#%06X", (0xFFFFFF & chartColor));
        } else {
            color = "#FF0000";
        }

        JSONObject params;
        try {
            Map<String, Object> jsonParams = new LinkedHashMap<String, Object>();
            if (getMeasures().size() == 1) {
                jsonParams.put("seriesColors", new JSONArray(Arrays.asList("'" + color + "'")));
            } else {
                JSONArray seriesColors = getSeriesColors();
                jsonParams.put("seriesColors", seriesColors);
                jsonParams.put("legend", getLegend());
            }
            JSONObject seriesDefaults = new JSONObject();
            seriesDefaults.put("renderer", "$.jqplot.BarRenderer");
            JSONObject rendererOptions = new JSONObject();
            rendererOptions.put("fillToZero", "true");
            seriesDefaults.put("rendererOptions", rendererOptions);
            jsonParams.put("seriesDefaults", seriesDefaults);
            JSONObject grid = getGrid();
            jsonParams.put("grid", grid);
            JSONObject axes = new JSONObject();
            JSONObject xAxis = getGroupingAxis(getXaxis());
            axes.put("xaxis", xAxis);
            axes.put("yaxis", getMeasureAxis(getMeasures().get(0)));
            jsonParams.put("axes", axes);
            params = new JSONObject(jsonParams);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String argh = params.toString();
        argh = argh.replaceAll("\"", "");
        String timezoneOffset = "&timezoneOffset='+new Date().getTimezoneOffset()+'";
        String xyz = "$.getJSON('/app/columnChart?reportID="+getUrlKey()+timezoneOffset+"&'+ strParams, Chart.getColumnChartCallback('"+ targetDiv + "', " + argh + "))";

        return xyz;
    }
}
