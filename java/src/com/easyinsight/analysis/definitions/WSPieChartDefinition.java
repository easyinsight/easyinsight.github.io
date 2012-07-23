package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.ChartDefinitionState;
import com.easyinsight.analysis.ReportProperty;
import com.easyinsight.analysis.ReportStringProperty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 6:44:12 PM
 */
public class WSPieChartDefinition extends WSXAxisDefinition {

    private String labelPosition;

    public int getChartType() {
        return ChartDefinitionState.PIE_2D;
    }

    public int getChartFamily() {
        return ChartDefinitionState.PIE_FAMILY;
    }

    public String getLabelPosition() {
        return labelPosition;
    }

    public void setLabelPosition(String labelPosition) {
        this.labelPosition = labelPosition;
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        labelPosition = findStringProperty(properties, "labelPosition", "outside");
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportStringProperty("labelPosition", labelPosition));
        return properties;
    }

    @Override
    public List<String> javaScriptIncludes() {
        List<String> includes = super.javaScriptIncludes();
        includes.add("/js/plugins/jqplot.pieRenderer.min.js");
        includes.add("/js/plugins/jqplot.highlighter.min.js");
        includes.add("/js/plugins/jqplot.cursor.min.js");
        includes.add("/js/plugins/jqplot.pointLabels.min.js");
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
            JSONObject seriesDefaults = new JSONObject();
            seriesDefaults.put("renderer", "$.jqplot.PieRenderer");
            JSONObject rendererOptions = new JSONObject();
            rendererOptions.put("showDataLabels", "true");
            seriesDefaults.put("rendererOptions", rendererOptions);
            jsonParams.put("seriesDefaults", seriesDefaults);
            JSONObject legend = new JSONObject();
            legend.put("show", "true");
            jsonParams.put("legend", legend);
            JSONObject grid = new JSONObject();
            grid.put("background", "'#FFFFFF'");
            jsonParams.put("grid", grid);
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

        String xyz = "$.getJSON('/app/columnChart?reportID="+getAnalysisID()+timezoneOffset+"&'+ strParams, Chart.getPieChartCallback('" + targetDiv + "', " + argh + "))";
        return xyz;
    }
}
