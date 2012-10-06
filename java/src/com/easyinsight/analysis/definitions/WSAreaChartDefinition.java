package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 6:45:30 PM
 */
public class WSAreaChartDefinition extends WSTwoAxisDefinition {

    private String stackingType = "stacked";

    public String getStackingType() {
        return stackingType;
    }

    public void setStackingType(String stackingType) {
        this.stackingType = stackingType;
    }

    public int getChartType() {
        return ChartDefinitionState.AREA_2D;
    }

    public int getChartFamily() {
        return ChartDefinitionState.AREA_FAMILY;
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        stackingType = findStringProperty(properties, "stackingType", "stacked");
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportStringProperty("stackingType", stackingType));
        return properties;
    }

    @Override
    public List<String> javaScriptIncludes() {
        List<String> includes = super.javaScriptIncludes();
        includes.add("/js/plugins/jqplot.dateAxisRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasTextRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasAxisTickRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasAxisLabelRenderer.min.js");
        includes.add("/js/plugins/jqplot.enhancedLegendRenderer.min.js");
        includes.add("/js/visualizations/chart.js");
        includes.add("/js/visualizations/util.js");
        return includes;
    }

    @Override
    public String toHTML(String targetDiv, HTMLReportMetadata htmlReportMetadata) {

        JSONObject params;
        try {
            Map<String, Object> jsonParams = new LinkedHashMap<String, Object>();
            JSONObject legend = getLegend();
            jsonParams.put("legend", legend);
            jsonParams.put("stackSeries", "true");
            jsonParams.put("showMarker", "false");
            JSONObject seriesDefaults = new JSONObject();
            seriesDefaults.put("fill", "true");

            jsonParams.put("seriesDefaults", seriesDefaults);
            JSONObject grid = getGrid();
            jsonParams.put("grid", grid);
            JSONObject axes = new JSONObject();
            JSONObject xAxis = getGroupingAxis(getXaxis());
            xAxis.put("renderer", "$.jqplot.DateAxisRenderer");

            JSONObject xAxisTickOptions = xAxis.getJSONObject("tickOptions");
            AnalysisDateDimension date = (AnalysisDateDimension) this.getXaxis();
            if (date.getDateLevel() == AnalysisDateDimension.DAY_LEVEL) {
                xAxisTickOptions.put("formatString", "'%b %#d'");
            } else if (date.getDateLevel() == AnalysisDateDimension.MONTH_LEVEL) {
                xAxisTickOptions.put("formatString", "'%b'");
            } else if (date.getDateLevel() == AnalysisDateDimension.YEAR_LEVEL) {
                xAxisTickOptions.put("formatString", "'%b'");
            } else {
                xAxisTickOptions.put("formatString", "'%b %#d'");
            }

            xAxis.put("tickOptions", xAxisTickOptions);
            axes.put("xaxis", xAxis);
            if (getMeasure() != null) {
                axes.put("yaxis", getMeasureAxis(getMeasure()));
            }
            jsonParams.put("axes", axes);
            JSONObject highlighter = new JSONObject();
            highlighter.put("show", true);
            highlighter.put("sizeAdjust", 7.5);
            highlighter.put("useAxesFormatters", "true");
            jsonParams.put("highlighter", highlighter);
            JSONObject cursor = new JSONObject();
            cursor.put("show", false);
            jsonParams.put("cursor", cursor);
            JSONArray seriesColors = getSeriesColors();
            jsonParams.put("seriesColors", seriesColors);
            params = new JSONObject(jsonParams);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String argh = params.toString();
        argh = argh.replaceAll("\"", "");

        String timezoneOffset = "&timezoneOffset='+new Date().getTimezoneOffset()+'";
        /*argh = "$.getJSON('/app/twoAxisChart?reportID="+getUrlKey()+timezoneOffset+"&'+ strParams, function(data) {afterRefresh();\n" +
                "                var s1 = data[\"values\"];\n" +
                "                var plot1 = $.jqplot('"+targetDiv+"', s1, " + argh + ");\n})";*/
        String customHeight = htmlReportMetadata.createStyleProperties();
        argh = "$.getJSON('/app/twoAxisChart?reportID="+getUrlKey()+timezoneOffset+"&'+ strParams, Chart.getCallback('" + targetDiv + "', " + argh + ", true, " + customHeight + "))";
        System.out.println(argh);
        return argh;
    }
}
