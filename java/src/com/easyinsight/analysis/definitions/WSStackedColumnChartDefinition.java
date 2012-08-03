package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * User: James Boe
 * Date: Mar 20, 2009
 * Time: 7:23:14 PM
 */
public class WSStackedColumnChartDefinition extends WSXAxisDefinition {

    private int chartColor;
    private boolean useChartColor;
    private String columnSort;
    private AnalysisItem stackItem;

    public AnalysisItem getStackItem() {
        return stackItem;
    }

    public void setStackItem(AnalysisItem stackItem) {
        this.stackItem = stackItem;
    }

    public int getChartType() {
        return ChartDefinitionState.COLUMN_2D_STACKED;
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
        useChartColor = findBooleanProperty(properties, "useChartColor", false);
        columnSort = findStringProperty(properties, "columnSort", "Unsorted");
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportNumericProperty("chartColor", chartColor));
        properties.add(new ReportBooleanProperty("useChartColor", useChartColor));
        properties.add(new ReportStringProperty("columnSort", columnSort));
        return properties;
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        super.createReportStructure(structure);
        addItems("stackItem", Arrays.asList(stackItem), structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        super.populateFromReportStructure(structure);
        stackItem = firstItem("stackItem", structure);
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = super.getAllAnalysisItems();
        columnList.add(stackItem);
        return columnList;
    }

    @Override
    public List<String> javaScriptIncludes() {
        List<String> includes = super.javaScriptIncludes();
        includes.add("/js/plugins/jqplot.barRenderer.min.js");
        includes.add("/js/plugins/jqplot.categoryAxisRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasAxisTickRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasAxisLabelRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasTextRenderer.min.js");
        includes.add("/js/visualizations/chart.js");
        includes.add("/js/visualizations/util.js");
        return includes;
    }

    @Override
    public String toHTML(String targetDiv) {

        JSONObject params;
        try {
            Map<String, Object> jsonParams = new LinkedHashMap<String, Object>();
            JSONObject legendObj = new JSONObject();
            legendObj.put("show", "true");
            legendObj.put("placement", "'outsideGrid'");
            legendObj.put("location", "'e'");
            jsonParams.put("legend", legendObj);
            jsonParams.put("stackSeries", "true");
            JSONObject seriesDefaults = new JSONObject();
            seriesDefaults.put("renderer", "$.jqplot.BarRenderer");
            JSONObject rendererOptions = new JSONObject();
            rendererOptions.put("barDirection", "'vertical'");
            seriesDefaults.put("rendererOptions", rendererOptions);
            jsonParams.put("seriesDefaults", seriesDefaults);
            JSONObject grid = new JSONObject();
            grid.put("background", "'#FFFFFF'");
            jsonParams.put("grid", grid);
            JSONObject axes = new JSONObject();
            JSONObject xAxis = new JSONObject();
            xAxis.put("renderer", "$.jqplot.CategoryAxisRenderer");
            xAxis.put("tickRenderer", "$.jqplot.CanvasAxisTickRenderer");
            xAxis.put("label", "'"+getXaxis().toDisplay()+"'");
            JSONObject xAxisTicketOptions = new JSONObject();
            xAxisTicketOptions.put("angle", -15);
            xAxis.put("tickOptions", xAxisTicketOptions);
            axes.put("xaxis", xAxis);
            JSONObject yAxis = new JSONObject();
            yAxis.put("pad", 1.05);
            yAxis.put("label", "'"+getMeasures().get(0).toDisplay() +"'");
            yAxis.put("labelRenderer", "$.jqplot.CanvasAxisLabelRenderer");
            JSONObject tickOptions = new JSONObject();
            tickOptions.put("formatter", "$.jqplot.currencyTickNumberFormatter");
            yAxis.put("tickOptions", tickOptions);
            axes.put("yaxis", yAxis);
            jsonParams.put("axes", axes);
            JSONArray seriesColors = new JSONArray(Arrays.asList("'#a6bc59'", "'#597197'", "'#d6ab2a'", "'#d86068'", "'#5d9942'",
                    "'#7a4c6c'", "'#F0B400'", "'#1E6C0B'", "'#00488C'", "'#332600'", "'#D84000'"));
            jsonParams.put("seriesColors", seriesColors);
            params = new JSONObject(jsonParams);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String argh = params.toString();
        argh = argh.replaceAll("\"", "");
        String timezoneOffset = "&timezoneOffset='+new Date().getTimezoneOffset()+'";
        String xyz = "$.getJSON('/app/stackedChart?reportID="+getUrlKey()+timezoneOffset+"&'+ strParams, Chart.getStackedColumnChart('"+ targetDiv + "', " + argh + "))";
        /*return "$.getJSON('/app/stackedChart?reportID="+getUrlKey()+timezoneOffset+"&'+ strParams, function(data) {\n" +
                "                var s1 = data[\"values\"];\n" +
                "                var plot1 = $.jqplot('"+targetDiv+"', s1, " + argh + ");afterRefresh();\n})";*/
        return xyz;
    }
}
