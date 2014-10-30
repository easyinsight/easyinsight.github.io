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
    private AnalysisItem zaxisMeasure;
    private boolean briefLabels;
    private boolean showLabels;
    private List<MultiColor> multiColors;

    public AnalysisItem getZaxisMeasure() {
        return zaxisMeasure;
    }

    public void setZaxisMeasure(AnalysisItem zaxisMeasure) {
        this.zaxisMeasure = zaxisMeasure;
    }

    public List<MultiColor> getMultiColors() {
        return multiColors;
    }

    public void setMultiColors(List<MultiColor> multiColors) {
        this.multiColors = multiColors;
    }

    @Override
    protected List<MultiColor> configuredMultiColors() {
        return multiColors;
    }

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
        multiColors = multiColorProperty(properties, "multiColors");
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportBooleanProperty("showLabels", showLabels));
        properties.add(new ReportBooleanProperty("briefLabels", briefLabels));
        properties.add(ReportMultiColorProperty.fromColors(multiColors, "multiColors"));
        return properties;
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("xAxisMeasure", Arrays.asList(xaxisMeasure), structure);
        addItems("yAxisMeasure", Arrays.asList(yaxisMeasure), structure);
        addItems("dimension", Arrays.asList(dimension), structure);
        if (iconGrouping != null) {
            addItems("series", Arrays.asList(iconGrouping), structure);
        }
        if (zaxisMeasure != null) {
            addItems("zaxisMeasure", Arrays.asList(zaxisMeasure), structure);
        }
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        xaxisMeasure = firstItem("xAxisMeasure", structure);
        yaxisMeasure = firstItem("yAxisMeasure", structure);
        zaxisMeasure = firstItem("zaxisMeasure", structure);
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
        if (zaxisMeasure != null) {
            columnList.add(zaxisMeasure);
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
    public JSONObject toJSON(HTMLReportMetadata htmlReportMetadata, List<FilterDefinition> parentDefinitions) throws JSONException {
        JSONObject pie = super.toJSON(htmlReportMetadata, parentDefinitions);
        pie.put("key", getUrlKey());
        pie.put("type", "plot");
        pie.put("styles", htmlReportMetadata.createStyleProperties());
        pie.put("url", "/app/plotChart");
        return pie;
    }
}
