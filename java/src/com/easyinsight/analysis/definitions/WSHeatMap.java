package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.WSAnalysisDefinition;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: jamesboe
 * Date: Dec 3, 2009
 * Time: 10:12:43 AM
 */
public class WSHeatMap extends WSAnalysisDefinition {

    private AnalysisItem latitudeItem;
    private AnalysisItem longitudeItem;
    private AnalysisItem measure;

    private double latitude;
    private double longitude;
    private int zoomLevel;
    private int mapType;

    private long heatMapID;

    public long getHeatMapID() {
        return heatMapID;
    }

    public void setHeatMapID(long heatMapID) {
        this.heatMapID = heatMapID;
    }

    public AnalysisItem getLatitudeItem() {
        return latitudeItem;
    }

    public void setLatitudeItem(AnalysisItem latitudeItem) {
        this.latitudeItem = latitudeItem;
    }

    public AnalysisItem getLongitudeItem() {
        return longitudeItem;
    }

    public void setLongitudeItem(AnalysisItem longitudeItem) {
        this.longitudeItem = longitudeItem;
    }

    public AnalysisItem getMeasure() {
        return measure;
    }

    public void setMeasure(AnalysisItem measure) {
        this.measure = measure;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(int zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

    @Override
    public String getDataFeedType() {
        return "Heatmap";
    }

    @Override
    public Set<AnalysisItem> getAllAnalysisItems() {
        return new HashSet<AnalysisItem>(Arrays.asList(latitudeItem, longitudeItem, measure));
    }

    @Override
    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("latitudeItem", Arrays.asList(latitudeItem), structure);
        addItems("longitudeItem", Arrays.asList(longitudeItem), structure);
        addItems("measure", Arrays.asList(measure), structure);
    }

    @Override
    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        latitudeItem = firstItem("latitudeItem", structure);
        longitudeItem = firstItem("longitudeItem", structure);
        measure = firstItem("measure", structure);
    }
}
