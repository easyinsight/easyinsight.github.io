package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
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
    private List<MultiColor> multiColors = new ArrayList<MultiColor>();
    private int legendMaxWidth;

    public int getLegendMaxWidth() {
        return legendMaxWidth;
    }

    public void setLegendMaxWidth(int legendMaxWidth) {
        this.legendMaxWidth = legendMaxWidth;
    }

    public List<MultiColor> getMultiColors() {
        return multiColors;
    }

    public void setMultiColors(List<MultiColor> multiColors) {
        this.multiColors = multiColors;
    }

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
        legendMaxWidth = (int) findNumberProperty(properties, "legendMaxWidth", 200);
        multiColors = multiColorProperty(properties, "multiColors");
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportStringProperty("labelPosition", labelPosition));
        properties.add(new ReportNumericProperty("legendMaxWidth", legendMaxWidth));
        properties.add(ReportMultiColorProperty.fromColors(multiColors, "multiColors"));
        return properties;
    }

    @Override
    public List<String> javaScriptIncludes() {
        List<String> includes = super.javaScriptIncludes();
        includes.add("/js/plugins/jqplot.gradientPieRenderer.js");
        includes.add("/js/plugins/jqplot.highlighter.min.js");
        includes.add("/js/plugins/jqplot.cursor.min.js");
        includes.add("/js/plugins/jqplot.pointLabels.min.js");
        includes.add("/js/plugins/jqplot.canvasTextRenderer.min.js");
        includes.add("/js/visualizations/chart.js");
        includes.add("/js/visualizations/util.js");
        return includes;
    }

    @Override
    public String toHTML(String targetDiv, HTMLReportMetadata htmlReportMetadata) {

        JSONObject params;
        JSONObject data = new JSONObject();
        try {
            Map<String, Object> jsonParams = new LinkedHashMap<String, Object>();
            JSONObject seriesDefaults = new JSONObject();
            seriesDefaults.put("renderer", "$.jqplot.PieRenderer");
            JSONObject rendererOptions = new JSONObject();
            rendererOptions.put("showDataLabels", "true");
            seriesDefaults.put("rendererOptions", rendererOptions);
            jsonParams.put("seriesDefaults", seriesDefaults);
            jsonParams.put("legend", getLegend());
            JSONObject grid = getGrid();
            jsonParams.put("grid", grid);
            JSONArray seriesColors = getSeriesColors();
            jsonParams.put("seriesColors", seriesColors);
            params = new JSONObject(jsonParams);
            data.put("jqplotOptions", params);
            JSONObject drillthrough = new JSONObject();
            drillthrough.put("embedded", htmlReportMetadata.isEmbedded());
            data.put("drillthrough", drillthrough);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String argh = data.toString();
        argh = argh.replaceAll("\"", "");
        String timezoneOffset = "&timezoneOffset='+new Date().getTimezoneOffset()+'";
        String customHeight = htmlReportMetadata.createStyleProperties();
        String xyz = "$.getJSON('/app/columnChart?reportID="+getUrlKey()+timezoneOffset+"&'+ strParams, Chart.getPieChartCallback('" + targetDiv + "', " + argh + ","+customHeight+"))";
        return xyz;
    }
}
