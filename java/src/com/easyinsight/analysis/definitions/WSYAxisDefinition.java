package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.ReportProperty;
import com.easyinsight.analysis.ReportStringProperty;
import com.easyinsight.analysis.WSChartDefinition;

import java.util.*;

/**
 * User: James Boe
 * Date: Mar 21, 2009
 * Time: 6:43:40 PM
 */
public abstract class WSYAxisDefinition extends WSChartDefinition {
    private AnalysisItem measure;
    private AnalysisItem yaxis;

    private String colorScheme;

    public String getColorScheme() {
        return colorScheme;
    }

    public void setColorScheme(String colorScheme) {
        this.colorScheme = colorScheme;
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
        addItems("yAxis", Arrays.asList(yaxis), structure);
        addItems("measure", Arrays.asList(measure), structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        yaxis = firstItem("yAxis", structure);
        measure = firstItem("measure", structure);
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        columnList.add(measure);
        columnList.add(yaxis);
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
