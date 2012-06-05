package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSHeatMap;
import com.easyinsight.core.XMLMetadata;
import nu.xom.Attribute;
import nu.xom.Element;

import javax.persistence.*;
import java.util.List;

/**
 * User: jamesboe
 * Date: Dec 3, 2009
 * Time: 10:45:43 AM
 */
@Entity
@Table(name="heat_map")
public class HeatMapDefinitionState extends AnalysisDefinitionState {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="heat_map_id")
    private long heatMapDefinitionID;

    @Column(name="map_type")
    private int mapType;

    @Column(name="longitude")
    private double longitude;

    @Column(name="latitude")
    private double latitude;

    @Column(name="zoom_level")
    private int zoomLevel;

    @Column(name="min_long")
    private double minLong;

    @Column(name="max_long")
    private double maxLong;

    @Column(name="min_lat")
    private double minLat;

    @Column(name="max_lat")
    private double maxLat;

    @Column(name="display_type")
    private int displayType;

    @Override
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = new Element("heatMapState");
        element.addAttribute(new Attribute("mapType", String.valueOf(mapType)));
        element.addAttribute(new Attribute("longitude", String.valueOf(longitude)));
        element.addAttribute(new Attribute("latitude", String.valueOf(latitude)));
        element.addAttribute(new Attribute("zoomLevel", String.valueOf(zoomLevel)));
        element.addAttribute(new Attribute("minLong", String.valueOf(minLong)));
        element.addAttribute(new Attribute("maxLong", String.valueOf(maxLong)));
        element.addAttribute(new Attribute("minLat", String.valueOf(minLat)));
        element.addAttribute(new Attribute("maxLat", String.valueOf(maxLat)));
        element.addAttribute(new Attribute("displayType", String.valueOf(displayType)));
        return element;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(int zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public long getHeatMapDefinitionID() {
        return heatMapDefinitionID;
    }

    public void setHeatMapDefinitionID(long heatMapDefinitionID) {
        this.heatMapDefinitionID = heatMapDefinitionID;
    }

    @Override
    public AnalysisDefinitionState clone(List<AnalysisItem> allFields) throws CloneNotSupportedException {
        HeatMapDefinitionState heatMapDefinitionState = (HeatMapDefinitionState) super.clone(allFields);
        heatMapDefinitionState.setHeatMapDefinitionID(0);
        return heatMapDefinitionState;
    }

    @Override
    public WSAnalysisDefinition createWSDefinition() {
        WSHeatMap heatMapDefinition = new WSHeatMap();
        heatMapDefinition.setLatitude(latitude);
        heatMapDefinition.setLongitude(longitude);
        heatMapDefinition.setMapType(mapType);
        heatMapDefinition.setDisplayType(displayType);
        heatMapDefinition.setZoomLevel(zoomLevel);
        heatMapDefinition.setMinLat(minLat);
        heatMapDefinition.setMinLong(minLong);
        heatMapDefinition.setMaxLat(maxLat);
        heatMapDefinition.setMaxLong(maxLong);
        heatMapDefinition.setHeatMapID(getHeatMapDefinitionID());
        return heatMapDefinition;
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

    public double getMinLat() {
        return minLat;
    }

    public void setMinLat(double minLat) {
        this.minLat = minLat;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public void setMaxLat(double maxLat) {
        this.maxLat = maxLat;
    }
}
