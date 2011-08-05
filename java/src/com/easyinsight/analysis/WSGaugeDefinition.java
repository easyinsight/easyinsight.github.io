package com.easyinsight.analysis;

import java.util.*;

/**
 * User: James Boe
 * Date: Mar 31, 2009
 * Time: 10:06:29 AM
 */
public class WSGaugeDefinition extends WSAnalysisDefinition {

    private AnalysisItem measure;
    private int gaugeType;
    private long gaugeDefinitionID;
    private double maxValue;
    private double alertPoint1;
    private double alertPoint2;
    private int color1;
    private int color2;
    private int color3;

    public double getAlertPoint1() {
        return alertPoint1;
    }

    public void setAlertPoint1(double alertPoint1) {
        this.alertPoint1 = alertPoint1;
    }

    public double getAlertPoint2() {
        return alertPoint2;
    }

    public void setAlertPoint2(double alertPoint2) {
        this.alertPoint2 = alertPoint2;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public long getGaugeDefinitionID() {
        return gaugeDefinitionID;
    }

    public void setGaugeDefinitionID(long gaugeDefinitionID) {
        this.gaugeDefinitionID = gaugeDefinitionID;
    }

    public int getGaugeType() {
        return gaugeType;
    }

    public void setGaugeType(int gaugeType) {
        this.gaugeType = gaugeType;
    }

    public AnalysisItem getMeasure() {
        return measure;
    }

    public void setMeasure(AnalysisItem measure) {
        this.measure = measure;
    }

    public String getDataFeedType() {
        return AnalysisTypes.GAUGE;
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        columnList.add(measure);
        return columnList;
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("measure", Arrays.asList(measure), structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        measure = firstItem("measure", structure);
    }

    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        alertPoint1 = findNumberProperty(properties, "alertPoint1", 0);
        alertPoint2 = findNumberProperty(properties, "alertPoint2", 0);
        color1 = (int) findNumberProperty(properties, "color1", 0);
        color2 = (int) findNumberProperty(properties, "color2", 0);
        color3 = (int) findNumberProperty(properties, "color3", 0);
    }

    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportNumericProperty("alertPoint1", alertPoint1));
        properties.add(new ReportNumericProperty("alertPoint2", alertPoint2));
        properties.add(new ReportNumericProperty("color1", color1));
        properties.add(new ReportNumericProperty("color2", color2));
        properties.add(new ReportNumericProperty("color3", color3));
        return properties;
    }
}
