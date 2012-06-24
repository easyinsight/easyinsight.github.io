package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.ChartDefinitionState;
import com.easyinsight.analysis.ReportProperty;
import com.easyinsight.analysis.ReportStringProperty;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public String javaScriptIncludes() {
        return "<script type=\"text/javascript\" src=\"/js/jquery.jqplot.min.js\"></script>\n" +
                "<script class=\"include\" type=\"text/javascript\" src=\"/js/plugins/jqplot.pieRenderer.min.js\"></script>\n"+
                "    <script type=\"text/javascript\" src=\"/js/plugins/jqplot.pointLabels.min.js\"></script>\n" +
                "<script type=\"text/javascript\" src=\"/js/plugins/jqplot.canvasTextRenderer.min.js\"></script>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"/css/jquery.jqplot.min.css\" />";
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
    }
}
