package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 6:45:48 PM
 */
public class WSPlotChartDefinition extends WSChartDefinition {
    private AnalysisItem dimension;
    private AnalysisItem iconGrouping;
    private AnalysisItem xaxisMeasure;
    private AnalysisItem yaxisMeasure;
    private boolean briefLabels;
    private boolean showLabels;

    public boolean isBriefLabels() {
        return briefLabels;
    }

    public void setBriefLabels(boolean briefLabels) {
        this.briefLabels = briefLabels;
    }

    public boolean isShowLabels() {
        return showLabels;
    }

    public void setShowLabels(boolean showLabels) {
        this.showLabels = showLabels;
    }

    private boolean calculateCorrelation;

    public boolean isCalculateCorrelation() {
        return calculateCorrelation;
    }

    public void setCalculateCorrelation(boolean calculateCorrelation) {
        this.calculateCorrelation = calculateCorrelation;
    }

    public AnalysisItem getIconGrouping() {
        return iconGrouping;
    }

    public void setIconGrouping(AnalysisItem iconGrouping) {
        this.iconGrouping = iconGrouping;
    }

    public AnalysisItem getXaxisMeasure() {
        return xaxisMeasure;
    }

    public void setXaxisMeasure(AnalysisItem xaxisMeasure) {
        this.xaxisMeasure = xaxisMeasure;
    }

    public AnalysisItem getYaxisMeasure() {
        return yaxisMeasure;
    }

    public void setYaxisMeasure(AnalysisItem yaxisMeasure) {
        this.yaxisMeasure = yaxisMeasure;
    }

    public AnalysisItem getDimension() {
        return dimension;
    }

    public void setDimension(AnalysisItem dimension) {
        this.dimension = dimension;
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        showLabels = findBooleanProperty(properties, "showLabels", true);
        briefLabels = findBooleanProperty(properties, "briefLabels", true);
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportBooleanProperty("showLabels", showLabels));
        properties.add(new ReportBooleanProperty("briefLabels", briefLabels));
        return properties;
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("xAxisMeasure", Arrays.asList(xaxisMeasure), structure);
        addItems("yAxisMeasure", Arrays.asList(yaxisMeasure), structure);
        addItems("dimension", Arrays.asList(dimension), structure);
        if (iconGrouping != null) {
            addItems("series", Arrays.asList(iconGrouping), structure);
        }
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        xaxisMeasure = firstItem("xAxisMeasure", structure);
        yaxisMeasure = firstItem("yAxisMeasure", structure);
        dimension = firstItem("dimension", structure);
        iconGrouping = firstItem("series", structure);
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        columnList.add(dimension);
        columnList.add(xaxisMeasure);
        columnList.add(yaxisMeasure);
        if (iconGrouping != null) {
            columnList.add(iconGrouping);
        }
        return columnList;
    }

    public int getChartType() {
        return ChartDefinitionState.PLOT_TYPE;
    }

    public int getChartFamily() {
        return ChartDefinitionState.PLOT_FAMILY;
    }

    @Override
    public List<String> javaScriptIncludes() {
        List<String> includes = super.javaScriptIncludes();
        includes.add("/js/plugins/jqplot.bubbleRenderer.min.js");
        includes.add("/js/plugins/jqplot.dateAxisRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasTextRenderer.min.js");
        includes.add("/js/plugins/jqplot.canvasAxisTickRenderer.min.js");
        includes.add("/js/visualizations/chart.js");
        includes.add("/js/visualizations/util.js");
        return includes;
    }

    @Override
    public String toHTML(String targetDiv, HTMLReportMetadata htmlReportMetadata) {

        JSONObject params;
        JSONObject object = new JSONObject();
        try {
            Map<String, Object> jsonParams = new LinkedHashMap<String, Object>();

            JSONObject seriesDefaults = new JSONObject();
            seriesDefaults.put("renderer", "$.jqplot.BubbleRenderer");
            JSONObject axes = new JSONObject();
            JSONObject xAxis = getMeasureAxis(xaxisMeasure);
            JSONObject yAxis = getMeasureAxis(yaxisMeasure);
            axes.put("xaxis", xAxis);
            axes.put("yaxis", yAxis);
            jsonParams.put("axes", axes);

            JSONObject rendererOptions = new JSONObject();
            //rendererOptions.put("fillToZero", "true");
            rendererOptions.put("bubbleGradients", "true");
            rendererOptions.put("autoscaleBubbles", "false");
            seriesDefaults.put("rendererOptions", rendererOptions);
            seriesDefaults.put("shadow", true);
            jsonParams.put("seriesDefaults", seriesDefaults);
            JSONObject grid = getGrid();
            jsonParams.put("grid", grid);
            params = new JSONObject(jsonParams);
            object.put("jqplotOptions", params);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String argh = object.toString();
        argh = argh.replaceAll("\"", "");
        String timezoneOffset = "&timezoneOffset='+new Date().getTimezoneOffset()+'";
        String customHeight = htmlReportMetadata.createStyleProperties();
        argh = "$.getJSON('/app/bubbleChart?reportID="+getUrlKey()+timezoneOffset+"&'+ strParams, Chart.getCallback('"+ targetDiv + "', " + argh + ",false,"+customHeight+"))";
        System.out.println(argh);
        return argh;
    }
}
