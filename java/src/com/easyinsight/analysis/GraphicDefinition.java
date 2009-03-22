package com.easyinsight.analysis;

import javax.persistence.*;

/**
 * User: James Boe
 * Date: Jul 17, 2008
 * Time: 7:32:36 PM
 */
@Entity
@Table (name="graphic_definition")
@PrimaryKeyJoinColumn(name="analysis_id")
public abstract class GraphicDefinition extends AnalysisDefinition {

    /*@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="graphic_definition_id")
    private Long graphicDefinitionID;*/

    /*public Long getGraphicDefinitionID() {
        return graphicDefinitionID;
    }

    public void setGraphicDefinitionID(Long graphicDefinitionID) {
        this.graphicDefinitionID = graphicDefinitionID;
    }*/
}
