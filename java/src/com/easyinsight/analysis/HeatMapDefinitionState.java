package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSHeatMap;
import com.easyinsight.core.Key;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

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

    private int mapType;

    private double longitude;

    private double latitude;

    private int zoomLevel;

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
    public AnalysisDefinitionState clone(Map<Key, Key> keyMap, List<AnalysisItem> allFields) throws CloneNotSupportedException {
        HeatMapDefinitionState heatMapDefinitionState = (HeatMapDefinitionState) super.clone(keyMap, allFields);
        heatMapDefinitionState.setHeatMapDefinitionID(0);
        return heatMapDefinitionState;
    }

    @Override
    public WSAnalysisDefinition createWSDefinition() {
        WSHeatMap heatMapDefinition = new WSHeatMap();
        heatMapDefinition.setLatitude(latitude);
        heatMapDefinition.setLongitude(longitude);
        heatMapDefinition.setMapType(mapType);
        heatMapDefinition.setZoomLevel(zoomLevel);
        heatMapDefinition.setHeatMapID(getHeatMapDefinitionID());
        return heatMapDefinition;
    }
}
