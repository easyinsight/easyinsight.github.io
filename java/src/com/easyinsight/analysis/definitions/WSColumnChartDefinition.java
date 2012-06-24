package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        String color;
        if (useChartColor) {
            color = String.format("#%06X", (0xFFFFFF & chartColor));
        } else {
            color = "#FF0000";
        }

        JSONObject params;
        try {
            Map<String, Object> jsonParams = new LinkedHashMap<String, Object>();
            jsonParams.put("seriesColors", new JSONArray(Arrays.asList("'" + color + "'")));
            JSONObject seriesDefaults = new JSONObject();
            seriesDefaults.put("renderer", "$.jqplot.BarRenderer");
            JSONObject rendererOptions = new JSONObject();
            rendererOptions.put("fillToZero", "true");
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
            axes.put("xaxis", xAxis);
            JSONObject yAxis = new JSONObject();
            yAxis.put("pad", 1.05);
            JSONObject tickOptions = new JSONObject();
            tickOptions.put("formatString", "'%d'");
            yAxis.put("tickOptions", tickOptions);
            axes.put("yaxis", yAxis);
            jsonParams.put("axes", axes);
            params = new JSONObject(jsonParams);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String argh = params.toString();
        argh = argh.replaceAll("\"", "");
        String timezoneOffset = "&timezoneOffset='+new Date().getTimezoneOffset()+'";
        return "$.getJSON('/app/columnChart?reportID="+getAnalysisID()+timezoneOffset+"&'+ strParams, function(data) {\n" +
                "                var s1 = data[\"values\"];\n" +
                "                var plot1 = $.jqplot('"+targetDiv+"', [s1], " + argh + ");afterRefresh();\n})";

        /*return "$.getJSON('../columnChart?reportID="+getAnalysisID()+"&'+ strParams, function(data) {\n" +
"                var s1 = data[\"values\"];\n" +
"                var plot1 = $.jqplot('"+targetDiv+"', [s1], {\n" +
"seriesColors:['"+color+"'],"+
"                    seriesDefaults:{\n" +
"                        renderer:$.jqplot.BarRenderer,\n" +
"                        rendererOptions: {fillToZero: true}\n" +
"                    },\n" +
"grid: { background: '#FFFFFF'},\n"+
"                    axes: {\n" +
"                        xaxis: {\n" +
"                            renderer: $.jqplot.CategoryAxisRenderer,\n" +
"                            ticks: ticks\n" +
"                        },\n" +
"                        yaxis: {\n" +
"                            pad: 1.05,\n" +
"                            tickOptions: {formatString: '%d'}\n" +
"                        }\n" +
"                    }\n" +
"                });\n" +
"            });";*/
    }
}
