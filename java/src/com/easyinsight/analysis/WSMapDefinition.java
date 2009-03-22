package com.easyinsight.analysis;

import java.util.Map;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

/**
 * User: James Boe
 * Date: Jul 17, 2008
 * Time: 7:45:24 PM
 */
public class WSMapDefinition extends WSGraphicDefinition {

    private long mapDefinitionID;
    private int mapType;

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

    protected void createReportStructure(Map<String, AnalysisItem> structure) {
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
}
