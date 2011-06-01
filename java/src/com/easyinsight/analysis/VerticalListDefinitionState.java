package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSVerticalListDefinition;

import javax.persistence.*;
import java.util.List;

/**
 * User: jamesboe
 * Date: Sep 27, 2010
 * Time: 4:58:50 PM
 */
@Entity
@Table(name="vertical_list")
public class VerticalListDefinitionState extends AnalysisDefinitionState {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="vertical_list_id")
    private long verticalListID;

    @Override
    public WSAnalysisDefinition createWSDefinition() {
        WSVerticalListDefinition gantt = new WSVerticalListDefinition();
        gantt.setVerticalListID(verticalListID);
        return gantt;
    }

    public long getVerticalListID() {
        return verticalListID;
    }

    public void setVerticalListID(long verticalListID) {
        this.verticalListID = verticalListID;
    }

    @Override
    public AnalysisDefinitionState clone(List<AnalysisItem> allFields) throws CloneNotSupportedException {
        VerticalListDefinitionState verticalListDefinitionState = (VerticalListDefinitionState) super.clone(allFields);
        verticalListDefinitionState.setVerticalListID(0);
        return verticalListDefinitionState;
    }
}
