package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSHeatMap;
import com.easyinsight.analysis.definitions.WSMap;

import javax.persistence.*;
import java.util.List;

/**
 * User: jamesboe
 * Date: Dec 3, 2009
 * Time: 10:45:43 AM
 */
@Entity
@Table(name="topo_map")
public class TopoMapDefinitionState extends AnalysisDefinitionState {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="topo_map_id")
    private long topoDefinitionID;

    public long getTopoDefinitionID() {
        return topoDefinitionID;
    }

    public void setTopoDefinitionID(long topoDefinitionID) {
        this.topoDefinitionID = topoDefinitionID;
    }

    @Override
    public AnalysisDefinitionState clone(List<AnalysisItem> allFields) throws CloneNotSupportedException {
        TopoMapDefinitionState heatMapDefinitionState = (TopoMapDefinitionState) super.clone(allFields);
        heatMapDefinitionState.setTopoDefinitionID(0);
        return heatMapDefinitionState;
    }

    @Override
    public WSAnalysisDefinition createWSDefinition() {
        WSMap heatMapDefinition = new WSMap();
        heatMapDefinition.setMapID(getTopoDefinitionID());
        return heatMapDefinition;
    }
}
