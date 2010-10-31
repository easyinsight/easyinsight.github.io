package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.*;

import java.util.*;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 6:45:09 PM
 */
public abstract class WSTwoAxisDefinition extends WSChartDefinition {
    private AnalysisItem measure;
    private AnalysisItem xaxis;
    private AnalysisItem yaxis;
    private List<AnalysisItem> measures;
    private boolean multiMeasure;

    public List<AnalysisItem> getMeasures() {
        return measures;
    }

    public void setMeasures(List<AnalysisItem> measures) {
        this.measures = measures;
    }

    public boolean isMultiMeasure() {
        return multiMeasure;
    }

    public void setMultiMeasure(boolean multiMeasure) {
        this.multiMeasure = multiMeasure;
    }

    public AnalysisItem getXaxis() {
        return xaxis;
    }

    public void setXaxis(AnalysisItem xaxis) {
        this.xaxis = xaxis;
    }

    public AnalysisItem getYaxis() {
        return yaxis;
    }

    public void setYaxis(AnalysisItem yaxis) {
        this.yaxis = yaxis;
    }

    public AnalysisItem getMeasure() {
        return measure;
    }

    public void setMeasure(AnalysisItem measure) {
        this.measure = measure;
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("xAxis", Arrays.asList(xaxis), structure);
        addItems("yAxis", Arrays.asList(yaxis), structure);
        addItems("measure", Arrays.asList(measure), structure);
        addItems("measures", measures, structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        xaxis = firstItem("xAxis", structure);
        yaxis = firstItem("yAxis", structure);
        measure = firstItem("measure", structure);
        measures = items("measures", structure);
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        columnList.add(xaxis);
        if (multiMeasure) {
            columnList.addAll(measures);
        } else {
            columnList.add(measure);   
            columnList.add(yaxis);
        }

        return columnList;
    }

    private String form;
    private String baseAtZero;
    private String interpolateValues;

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getBaseAtZero() {
        return baseAtZero;
    }

    public void setBaseAtZero(String baseAtZero) {
        this.baseAtZero = baseAtZero;
    }

    public String getInterpolateValues() {
        return interpolateValues;
    }

    public void setInterpolateValues(String interpolateValues) {
        this.interpolateValues = interpolateValues;
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        form = findStringProperty(properties, "form", "segment");
        baseAtZero = findStringProperty(properties, "baseAtZero", "true");
        interpolateValues = findStringProperty(properties, "interpolateValues", "false");
        multiMeasure = findBooleanProperty(properties, "multiMeasure", false);
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportStringProperty("form", form));
        properties.add(new ReportStringProperty("baseAtZero", baseAtZero));
        properties.add(new ReportStringProperty("interpolateValues", interpolateValues));
        properties.add(new ReportBooleanProperty("multiMeasure", multiMeasure));
        return properties;
    }
}
