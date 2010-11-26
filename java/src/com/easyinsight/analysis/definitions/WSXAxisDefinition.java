package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.ReportProperty;
import com.easyinsight.analysis.ReportStringProperty;
import com.easyinsight.analysis.WSChartDefinition;
import com.easyinsight.analysis.AnalysisItem;

import java.util.*;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 4:49:42 PM
 */
public abstract class WSXAxisDefinition extends WSChartDefinition {

    private AnalysisItem measure;
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

    public AnalysisItem getMeasure() {
        return measure;
    }

    public void setMeasure(AnalysisItem measure) {
        this.measure = measure;
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("xAxis", Arrays.asList(xaxis), structure);
        addItems("measure", Arrays.asList(measure), structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        xaxis = firstItem("xAxis", structure);
        measure = firstItem("measure", structure);
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        columnList.add(measure);
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
