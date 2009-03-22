package com.easyinsight.analysis;

import javax.persistence.*;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Jul 17, 2008
 * Time: 7:44:33 PM
 */
@Entity
@Table(name="map_definition")
@PrimaryKeyJoinColumn(name="analysis_id")
public class MapDefinition extends GraphicDefinition {

    @Column(name="map_type")
    private int mapType;

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="map_definition_id")
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

    protected WSAnalysisDefinition createWSDefinition() {
        WSMapDefinition wsMapDefinition = new WSMapDefinition();
        wsMapDefinition.setMapType(mapType);
        wsMapDefinition.setMapDefinitionID(mapDefinitionID);
        return wsMapDefinition;
    }

    public AnalysisDefinition clone() throws CloneNotSupportedException {
        MapDefinition mapDefinition = (MapDefinition) super.clone();
        mapDefinition.setMapDefinitionID(0);
        return mapDefinition;
    }
}
