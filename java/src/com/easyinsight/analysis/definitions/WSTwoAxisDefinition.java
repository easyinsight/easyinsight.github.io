package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.ReportProperty;
import com.easyinsight.analysis.ReportStringProperty;
import com.easyinsight.analysis.WSChartDefinition;
import com.easyinsight.analysis.AnalysisItem;

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
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        xaxis = firstItem("xAxis", structure);
        yaxis = firstItem("yAxis", structure);
        measure = firstItem("measure", structure);
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        columnList.add(measure);
        columnList.add(xaxis);
        columnList.add(yaxis);
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
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportStringProperty("form", form));
        properties.add(new ReportStringProperty("baseAtZero", baseAtZero));
        properties.add(new ReportStringProperty("interpolateValues", interpolateValues));
        return properties;
    }
}
