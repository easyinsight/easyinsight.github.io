package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * User: James Boe
 * Date: Mar 20, 2009
 * Time: 7:23:14 PM
 */
public class WSStackedBarChartDefinition extends WSYAxisDefinition {

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
        return ChartDefinitionState.BAR_2D_STACKED;
    }

    public int getChartFamily() {
        return ChartDefinitionState.BAR_FAMILY;
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
    public String javaScriptIncludes() {
        return "<script type=\"text/javascript\" src=\"/js/jquery.jqplot.min.js\"></script>\n" +
                "    <script type=\"text/javascript\" src=\"/js/plugins/jqplot.barRenderer.min.js\"></script>\n" +
                "    <script type=\"text/javascript\" src=\"/js/plugins/jqplot.categoryAxisRenderer.min.js\"></script>\n" +
                "    <script type=\"text/javascript\" src=\"/js//plugins/jqplot.pointLabels.min.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"/js/plugins/jqplot.canvasTextRenderer.min.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"/js/plugins/jqplot.canvasAxisTickRenderer.min.js\"></script>\n"+
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"/css/jquery.jqplot.min.css\" />";
    }

    @Override
    public String toHTML(String targetDiv) {

        JSONObject params;
        try {
            Map<String, Object> jsonParams = new LinkedHashMap<String, Object>();
            JSONObject legendObj = new JSONObject();
            legendObj.put("show", "true");
            legendObj.put("placement", "'outside'");
            legendObj.put("location", "'e'");
            jsonParams.put("legend", legendObj);
            jsonParams.put("series", "data['series']");
            jsonParams.put("stackSeries", "true");
            //jsonParams.put("seriesColors", new JSONArray(Arrays.asList("'" + color + "'")));
            JSONObject seriesDefaults = new JSONObject();
            seriesDefaults.put("renderer", "$.jqplot.BarRenderer");
            JSONObject rendererOptions = new JSONObject();
            rendererOptions.put("barDirection", "'horizontal'");
            //rendererOptions.put("fillToZero", "true");
            seriesDefaults.put("rendererOptions", rendererOptions);
            jsonParams.put("seriesDefaults", seriesDefaults);
            JSONObject grid = new JSONObject();
            grid.put("background", "'#FFFFFF'");
            jsonParams.put("grid", grid);
            JSONObject axes = new JSONObject();
            JSONObject xAxis = new JSONObject();
            xAxis.put("renderer", "$.jqplot.CategoryAxisRenderer");
            xAxis.put("tickRenderer", "$.jqplot.CanvasAxisTickRenderer");
            JSONObject xAxisTicketOptions = new JSONObject();
            xAxisTicketOptions.put("angle", -15);
            xAxis.put("tickOptions", xAxisTicketOptions);
            xAxis.put("ticks", "data['ticks']");
            axes.put("yaxis", xAxis);
            JSONObject yAxis = new JSONObject();
            yAxis.put("pad", 1.05);
            JSONObject tickOptions = new JSONObject();
            tickOptions.put("formatString", "'%d'");
            yAxis.put("tickOptions", tickOptions);
            axes.put("xaxis", yAxis);
            jsonParams.put("axes", axes);
            params = new JSONObject(jsonParams);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String argh = params.toString();
        argh = argh.replaceAll("\"", "");
        String timezoneOffset = "&timezoneOffset='+new Date().getTimezoneOffset()+'";
        return "$.getJSON('/app/stackedChart?reportID="+getAnalysisID()+timezoneOffset+"&'+ strParams, function(data) {\n" +
                "                var s1 = data[\"values\"];\n" +
                "                var plot1 = $.jqplot('"+targetDiv+"', s1, " + argh + ");afterRefresh();\n})";
    }
}
