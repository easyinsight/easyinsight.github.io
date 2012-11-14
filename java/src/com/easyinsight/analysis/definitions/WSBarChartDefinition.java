package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: James Boe
 * Date: Mar 20, 2009
 * Time: 7:23:14 PM
 */
public class WSBarChartDefinition extends WSYAxisDefinition {

    private int chartColor;
    private int gradientColor;
    private boolean useChartColor;
    private String columnSort;
    private String axisType;

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

    public int getChartType() {
        return ChartDefinitionState.BAR_2D;
    }

    public int getChartFamily() {
        return ChartDefinitionState.BAR_FAMILY;
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
        properties.add(new ReportStringProperty("axisType", columnSort));
        return properties;
    }

    @Override
    public List<String> javaScriptIncludes() {
        List<String> includes = super.javaScriptIncludes();
        includes.add("/js/plugins/jqplot.gradientBarRenderer.js");
        includes.add("/js/plugins/jqplot.categoryAxisRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasTextRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasAxisLabelRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasAxisTickRenderer.min.js");
        includes.add("/js/visualizations/chart.js");
        includes.add("/js/visualizations/util.js");
        return includes;
    }

    @Override
    public String toHTML(String targetDiv, HTMLReportMetadata htmlReportMetadata) {
        String color;
        String color2;
        JSONArray colorObj = new JSONArray();
        try {
            if (useChartColor) {
                color = String.format("'#%06X'", (0xFFFFFF & chartColor));
                color2 = String.format("'#%06X'", (0xFFFFFF & gradientColor));
                JSONObject colorStop = new JSONObject();
                colorStop.put("point", 0);
                colorStop.put("color", color);
                colorObj.put(colorStop);

                colorStop = new JSONObject();
                colorStop.put("point", 1);
                colorStop.put("color", color2);
                colorObj.put(colorStop);
            } else {

                color = "'#FF0000'";
                color2 = "'#990000'";


                JSONObject colorStop = new JSONObject();
                colorStop.put("point", 0);
                colorStop.put("color", color);
                colorObj.put(colorStop);

                colorStop = new JSONObject();
                colorStop.put("point", .15);
                colorStop.put("color", color2);
                colorObj.put(colorStop);

                colorStop = new JSONObject();
                colorStop.put("point", .5);
                colorStop.put("color", color);
                colorObj.put(colorStop);

                colorStop = new JSONObject();
                colorStop.put("point", .9);
                colorStop.put("color", color);
                colorObj.put(colorStop);

                colorStop = new JSONObject();
                colorStop.put("point", 1);
                colorStop.put("color", color2);
                colorObj.put(colorStop);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JSONObject params;
        try {
            Map<String, Object> jsonParams = new LinkedHashMap<String, Object>();
            if (getMeasures().size() == 1) {


                jsonParams.put("seriesColors", new JSONArray(Arrays.asList(colorObj)));

            } else {
                JSONArray seriesColors = getSeriesColors();
                jsonParams.put("seriesColors", seriesColors);
                jsonParams.put("legend", getLegend());
            }
            JSONObject seriesDefaults = new JSONObject();
            seriesDefaults.put("renderer", "$.jqplot.GradientBarRenderer");
            JSONObject rendererOptions = new JSONObject();
            rendererOptions.put("fillToZero", "true");
            rendererOptions.put("barDirection", "'horizontal'");
            rendererOptions.put("varyBarColor", "true");
            rendererOptions.put("barMargin", (45 / 2));
            seriesDefaults.put("rendererOptions", rendererOptions);
            jsonParams.put("seriesDefaults", seriesDefaults);
            JSONObject grid = getGrid();
            jsonParams.put("grid", grid);
            JSONObject axes = new JSONObject();
            JSONObject xAxis = getMeasureAxis(getMeasures().get(0));
            axes.put("xaxis", xAxis);
            JSONObject yAxis = getGroupingAxis(getYaxis());
            axes.put("yaxis", yAxis);
            jsonParams.put("axes", axes);
            params = new JSONObject(jsonParams);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String argh = params.toString();
        argh = argh.replaceAll("\"", "");

        String timezoneOffset = "&timezoneOffset='+new Date().getTimezoneOffset()+'";
        String customHeight = htmlReportMetadata.createStyleProperties();
        return "$.getJSON('/app/columnChart?reportID=" + getUrlKey() + timezoneOffset + "&'+ strParams, Chart.getCallback('" + targetDiv + "', " + argh + ",false," + customHeight + "))";
    }
}