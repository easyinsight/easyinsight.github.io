package com.easyinsight.analysis;

import java.util.*;

/**
 * User: James Boe
 * Date: Jul 17, 2008
 * Time: 7:45:24 PM
 */
public class WSMapDefinition extends WSAnalysisDefinition {

    private long mapDefinitionID;
    private int mapType;
    private int highColor;
    private int lowColor;
    private String colorStrategy;

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

    public long getMapDefinitionID() {
        return mapDefinitionID;
    }

    public void setMapDefinitionID(long mapDefinitionID) {
        this.mapDefinitionID = mapDefinitionID;
    }

    public int getHighColor() {
        return highColor;
    }

    public void setHighColor(int highColor) {
        this.highColor = highColor;
    }

    public int getLowColor() {
        return lowColor;
    }

    public void setLowColor(int lowColor) {
        this.lowColor = lowColor;
    }

    public String getColorStrategy() {
        return colorStrategy;
    }

    public void setColorStrategy(String colorStrategy) {
        this.colorStrategy = colorStrategy;
    }

    public String getDataFeedType() {
        return AnalysisTypes.MAP;
    }

    private AnalysisItem measure;
    private AnalysisItem geography;

    public AnalysisItem getMeasure() {
        return measure;
    }

    public void setMeasure(AnalysisItem measure) {
        this.measure = measure;
    }

    public AnalysisItem getGeography() {
        return geography;
    }

    public void setGeography(AnalysisItem geography) {
        this.geography = geography;
    }

    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("geography", Arrays.asList(geography), structure);
        addItems("measure", Arrays.asList(measure), structure);
    }

    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        geography = firstItem("geography", structure);
        measure = firstItem("measure", structure);
    }

    public Set<AnalysisItem> getAllAnalysisItems() {
        Set<AnalysisItem> columnList = new HashSet<AnalysisItem>();
        columnList.add(measure);
        columnList.add(geography);
        return columnList;
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        highColor = (int) findNumberProperty(properties, "highColor", 2);
        lowColor = (int) findNumberProperty(properties, "lowColor", 2);
        colorStrategy = findStringProperty(properties, "colorStrategy", "Linear");
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportNumericProperty("highColor", highColor));
        properties.add(new ReportNumericProperty("lowColor", lowColor));
        properties.add(new ReportStringProperty("colorStrategy", colorStrategy));
        return properties;
    }
}
