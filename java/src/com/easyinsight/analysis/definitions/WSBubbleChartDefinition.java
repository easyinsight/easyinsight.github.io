package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 6:45:54 PM
 */
public class WSBubbleChartDefinition extends WSChartDefinition {
    private AnalysisItem dimension;
    private AnalysisItem xaxisMeasure;
    private AnalysisItem yaxisMeasure;
    private AnalysisItem zaxisMeasure;
    private boolean showLabels = true;
    private boolean briefLabels = true;

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

    public AnalysisItem getZaxisMeasure() {
        return zaxisMeasure;
    }

    public void setZaxisMeasure(AnalysisItem zaxisMeasure) {
        this.zaxisMeasure = zaxisMeasure;
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
        addItems("zAxisMeasure", Arrays.asList(zaxisMeasure), structure);
        addItems("dimension", Arrays.asList(dimension), structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        xaxisMeasure = firstItem("xAxisMeasure", structure);
        yaxisMeasure = firstItem("yAxisMeasure", structure);
        zaxisMeasure = firstItem("zAxisMeasure", structure);
        dimension = firstItem("dimension", structure);
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        columnList.add(dimension);
        columnList.add(xaxisMeasure);
        columnList.add(yaxisMeasure);
        columnList.add(zaxisMeasure);
        return columnList;
    }

    public int getChartType() {
        return ChartDefinitionState.BUBBLE_TYPE;
    }

    public int getChartFamily() {
        return ChartDefinitionState.BUBBLE_FAMILY;
    }

    @Override
    public JSONObject toJSON(HTMLReportMetadata htmlReportMetadata, List<FilterDefinition> parentDefinitions) throws JSONException {
        JSONObject areaChart = super.toJSON(htmlReportMetadata, parentDefinitions);
        areaChart.put("type", "bubble");
        areaChart.put("key", getUrlKey());
        areaChart.put("url", "/app/bubbleChart");
        areaChart.put("styles", htmlReportMetadata.createStyleProperties());
        return areaChart;
    }
}
