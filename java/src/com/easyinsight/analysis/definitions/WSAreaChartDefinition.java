package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.ChartDefinitionState;
import com.easyinsight.analysis.ReportBooleanProperty;
import com.easyinsight.analysis.ReportProperty;
import com.easyinsight.analysis.ReportStringProperty;
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
        includes.add("/js/plugins/jqplot.barRenderer.min.js");
        includes.add("/js/plugins/jqplot.dateAxisRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasTextRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasAxisTickRenderer.min.js");
        return includes;
    }

    @Override
    public String toHTML(String targetDiv) {

        JSONObject params;
        try {
            Map<String, Object> jsonParams = new LinkedHashMap<String, Object>();

            jsonParams.put("stackSeries", "true");
            jsonParams.put("showMarker", "false");
            JSONObject seriesDefaults = new JSONObject();
            seriesDefaults.put("fill", "true");

            //seriesDefaults.put("renderer", "$.jqplot.BarRenderer");
            //JSONObject rendererOptions = new JSONObject();
            //rendererOptions.put("fillToZero", "true");
            //seriesDefaults.put("rendererOptions", rendererOptions);
            jsonParams.put("seriesDefaults", seriesDefaults);
            JSONObject grid = getGrid();
            jsonParams.put("grid", grid);
            JSONObject axes = new JSONObject();
            JSONObject xAxis = new JSONObject();
            xAxis.put("renderer", "$.jqplot.DateAxisRenderer");
            //JSONObject xAxisTicketOptions = new JSONObject();
            //xAxis.put("tickOptions", xAxisTicketOptions);
            axes.put("xaxis", xAxis);
            //JSONObject yAxis = new JSONObject();
            //JSONObject tickOptions = new JSONObject();
            //tickOptions.put("formatString", "'%d'");
            //yAxis.put("tickOptions", tickOptions);
            //axes.put("yaxis", yAxis);
            jsonParams.put("axes", axes);
            params = new JSONObject(jsonParams);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String argh = params.toString();
        argh = argh.replaceAll("\"", "");

        String timezoneOffset = "&timezoneOffset='+new Date().getTimezoneOffset()+'";
        argh = "$.getJSON('/app/twoAxisChart?reportID="+getUrlKey()+timezoneOffset+"&'+ strParams, function(data) {afterRefresh();\n" +
                "                var s1 = data[\"values\"];\n" +
                "                var plot1 = $.jqplot('"+targetDiv+"', s1, " + argh + ");\n})";
        System.out.println(argh);
        return argh;
    }
}
