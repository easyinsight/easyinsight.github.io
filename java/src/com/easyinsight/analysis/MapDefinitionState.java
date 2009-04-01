package com.easyinsight.analysis;

import javax.persistence.*;

/**
 * User: James Boe
 * Date: Mar 29, 2009
 * Time: 9:25:20 AM
 */
@Entity
@PrimaryKeyJoinColumn(name="report_state_id")
@Table(name="map_report")
public class MapDefinitionState extends AnalysisDefinitionState {
    @Column(name="map_type")
    private int mapType;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="map_report_id")
    private long mapDefinitionID;

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

    public WSAnalysisDefinition createWSDefinition() {
        WSMapDefinition wsMapDefinition = new WSMapDefinition();
        wsMapDefinition.setMapType(mapType);
        wsMapDefinition.setReportType(WSAnalysisDefinition.MAP);
        wsMapDefinition.setMapDefinitionID(mapDefinitionID);
        return wsMapDefinition;
    }

    public AnalysisDefinitionState clone() throws CloneNotSupportedException {
        MapDefinitionState mapDefinition = (MapDefinitionState) super.clone();
        mapDefinition.setMapDefinitionID(0);
        return mapDefinition;
    }
}
