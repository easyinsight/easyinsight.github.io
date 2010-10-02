package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSHeatMap;
import com.easyinsight.core.Key;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
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

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name="point_report_id")
    private AnalysisDefinition pointReport;

    public AnalysisDefinition getPointReport() {
        return pointReport;
    }

    public void setPointReport(AnalysisDefinition pointReport) {
        this.pointReport = pointReport;
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
        heatMapDefinition.setDisplayType(displayType);
        heatMapDefinition.setZoomLevel(zoomLevel);
        heatMapDefinition.setMinLat(minLat);
        heatMapDefinition.setMinLong(minLong);
        heatMapDefinition.setMaxLat(maxLat);
        heatMapDefinition.setMaxLong(maxLong);
        heatMapDefinition.setPointReportID(pointReport != null ? pointReport.getAnalysisID() : 0);
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

    @Override
    public Collection<? extends AnalysisDefinition> containedReports(Session session) {
        if (pointReport != null) {
            return Arrays.asList(pointReport);
        } else {
            return super.containedReports(session);
        }
    }

    public void updateReportIDs(Map<Long, AnalysisDefinition> reportReplacementMap) {
        super.updateReportIDs(reportReplacementMap);
        if (pointReport != null) {
            pointReport = reportReplacementMap.get(pointReport.getAnalysisID());
        }
    }
}
