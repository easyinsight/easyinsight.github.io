package com.easyinsight.analysis;

import javax.persistence.*;
import java.util.List;

/**
 * User: James Boe
 * Date: Mar 29, 2009
 * Time: 9:25:20 AM
 */
@Entity
@PrimaryKeyJoinColumn(name="report_state_id")
@Table(name="map_report")
public class MapDefinitionState extends AnalysisDefinitionState {

    public static final int USA = 1;
    public static final int WORLD = 2;
    public static final int EUROPE = 3;
    public static final int ASIA = 4;
    public static final int AMERICAS = 5;
    public static final int MIDDLE_EAST = 6;
    public static final int AFRICA = 7;

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
        wsMapDefinition.setMapDefinitionID(mapDefinitionID);
        return wsMapDefinition;
    }

    public int translateType() {
        if (mapType == WORLD) {
            return WSAnalysisDefinition.MAP_WORLD;
        } else if (mapType == USA) {
            return WSAnalysisDefinition.MAP_USA;
        } else if (mapType == EUROPE) {
            return WSAnalysisDefinition.MAP_EUROPE;
        } else if (mapType == ASIA) {
            return WSAnalysisDefinition.MAP_ASIA;
        } else if (mapType == AMERICAS) {
            return WSAnalysisDefinition.MAP_AMERICAS;
        } else if (mapType == MIDDLE_EAST) {
            return WSAnalysisDefinition.MAP_MIDDLE_EAST;
        } else if (mapType == AFRICA) {
            return WSAnalysisDefinition.MAP_AFRICA;
        } else {
            throw new RuntimeException();
        }
    }

    public AnalysisDefinitionState clone(List<AnalysisItem> allFields) throws CloneNotSupportedException {
        MapDefinitionState mapDefinition = (MapDefinitionState) super.clone(allFields);
        mapDefinition.setMapDefinitionID(0);
        return mapDefinition;
    }
}
