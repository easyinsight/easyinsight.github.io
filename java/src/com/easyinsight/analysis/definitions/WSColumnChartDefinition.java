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
        includes.add("/js/plugins/jqplot.canvasAxisTickRenderer.min.js");
        includes.add("/js/visualizations/chart.js");
        return includes;
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
//        String xyz = "$.getJSON('/app/columnChart?reportID="+getAnalysisID()+timezoneOffset+"&'+ strParams, function(data) {\n" +
//                "                var s1 = data[\"values\"];\n" +
//                "                var ticks = data[\"ticks\"];\n" +
//                "                var plot1 = $.jqplot('"+targetDiv+"', s1, " + argh + ");";
//        xyz += " $('#"+targetDiv+"').bind('jqplotDataHighlight', \n" +
//                "        function (ev, seriesIndex, pointIndex, data ) {\n" +
//                "            var mouseX = ev.pageX; //these are going to be how jquery knows where to put the div that will be our tooltip\n" +
//                "            var mouseY = ev.pageY;\n" +
//                "            $('#chartpseudotooltip').html(ticks[pointIndex] + ', ' + data[1]);\n" +
//                "            var cssObj = {\n" +
//                "                  'position' : 'absolute',\n" +
//                "                  'font-weight' : 'bold',\n" +
//                "                  'left' : mouseX + 'px', //usually needs more offset here\n" +
//                "                  'top' : mouseY + 'px'\n" +
//                "                };\n" +
//                "            $('#chartpseudotooltip').css(cssObj);\n" +
//                "            $('#chartpseudotooltip').show();\n" +
//                "            }\n" +
//                "    );    \n" +
//                "\n" +
//                "    $('#"+targetDiv+"').bind('jqplotDataUnhighlight', \n" +
//                "        function (ev) {\n" +
//                "            $('#chartpseudotooltip').html('');\n" +
//                "            $('#chartpseudotooltip').hide();\n" +
//                "        }\n" +
//                "    );";
//        xyz += "afterRefresh();\n" +
//                "})";
        String xyz = "$.getJSON('/app/columnChart?reportID="+getAnalysisID()+timezoneOffset+"&'+ strParams, Chart.getColumnChartCallback('"+ targetDiv + "', " + argh + "))";
        // katherine on gantt charts
        // justin sherman, nrc, 402-475-2525

        return xyz;
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
