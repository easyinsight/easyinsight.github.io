package com.easyinsight.analysis.definitions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.ReportNumericProperty;
import com.easyinsight.analysis.ReportProperty;
import com.easyinsight.analysis.WSAnalysisDefinition;

import java.util.*;

/**
 * User: jamesboe
 * Date: Dec 3, 2009
 * Time: 10:12:43 AM
 */
public class WSHeatMap extends WSAnalysisDefinition {

    private AnalysisItem latitudeItem;
    private AnalysisItem longitudeItem;
    private AnalysisItem zipCode;
    private AnalysisItem measure;

    private double latitude;
    private double longitude;
    private double minLong;
    private double maxLong;
    private double minLat;
    private double maxLat;
    private int zoomLevel;
    private int mapType;
    private int displayType;

    private int precision;

    private long pointReportID;

    private long heatMapID;

    public long getPointReportID() {
        return pointReportID;
    }

    public void setPointReportID(long pointReportID) {
        this.pointReportID = pointReportID;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    public AnalysisItem getZipCode() {
        return zipCode;
    }

    public void setZipCode(AnalysisItem zipCode) {
        this.zipCode = zipCode;
    }

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
        if (zipCode != null) {
            return new HashSet<AnalysisItem>(Arrays.asList(zipCode, measure));
        } else {
            return new HashSet<AnalysisItem>(Arrays.asList(latitudeItem, longitudeItem, measure));
        }
    }

    @Override
    public void createReportStructure(Map<String, AnalysisItem> structure) {
        addItems("latitudeItem", Arrays.asList(latitudeItem), structure);
        addItems("longitudeItem", Arrays.asList(longitudeItem), structure);
        addItems("measure", Arrays.asList(measure), structure);
        addItems("zipCode", Arrays.asList(zipCode), structure);
    }

    @Override
    public void populateFromReportStructure(Map<String, AnalysisItem> structure) {
        latitudeItem = firstItem("latitudeItem", structure);
        longitudeItem = firstItem("longitudeItem", structure);
        measure = firstItem("measure", structure);
        zipCode = firstItem("zipCode", structure);
    }

    public double getMaxLat() {
        return maxLat;
    }

    public void setMaxLat(double maxLat) {
        this.maxLat = maxLat;
    }

    public double getMinLat() {
        return minLat;
    }

    public void setMinLat(double minLat) {
        this.minLat = minLat;
    }

    public double getMinLong() {
        return minLong;
    }

    public void setMinLong(double minLong) {
        this.minLong = minLong;
    }

    public double getMaxLong() {
        return maxLong;
    }

    public void setMaxLong(double maxLong) {
        this.maxLong = maxLong;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    @Override
    public void populateProperties(List<ReportProperty> properties) {
        super.populateProperties(properties);
        precision = (int) findNumberProperty(properties, "precision", 2);
    }

    @Override
    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = super.createProperties();
        properties.add(new ReportNumericProperty("precision", precision));
        return properties;
    }
}
