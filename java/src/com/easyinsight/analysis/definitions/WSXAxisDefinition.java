package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 4:49:42 PM
 */
public abstract class WSXAxisDefinition extends WSChartDefinition {

    private List<AnalysisItem> measures;
    private AnalysisItem xaxis;

    private double yAxisMin;
    private double yAxisMax;

    private String colorScheme;

    public String getColorScheme() {
        return colorScheme;
    }

    public void setColorScheme(String colorScheme) {
        this.colorScheme = colorScheme;
    }

    public double getYAxisMax() {
        return yAxisMax;
    }

    public void setYAxisMax(double yAxisMax) {
        this.yAxisMax = yAxisMax;
    }

    public double getYAxisMin() {
        return yAxisMin;
    }

    public void setYAxisMin(double yAxisMin) {
        this.yAxisMin = yAxisMin;
    }

    public AnalysisItem getXaxis() {
        return xaxis;
    }

    public void setXaxis(AnalysisItem xaxis) {
        this.xaxis = xaxis;
    }

    public List<AnalysisItem> getMeasures() {
        return measures;
    }

    public void setMeasures(List<AnalysisItem> measures) {
        this.measures = measures;
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("xAxis", Arrays.asList(xaxis), structure);
        addItems("measure", measures, structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        xaxis = firstItem("xAxis", structure);
        measures = items("measure", structure);
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        if (measures != null) {
            for (AnalysisItem analysisItem : measures) {
                columnList.add(analysisItem);
            }
        }
        columnList.add(xaxis);
        return columnList;
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        colorScheme = findStringProperty(properties, "colorScheme", "Bright Gradients");
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportStringProperty("colorScheme", colorScheme));
        return properties;
    }
}
