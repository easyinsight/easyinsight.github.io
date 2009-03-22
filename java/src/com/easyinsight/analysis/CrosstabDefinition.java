package com.easyinsight.analysis;

import java.util.ArrayList;

import javax.persistence.*;

/**
 * User: James Boe
 * Date: Jan 10, 2008
 * Time: 7:47:59 PM
 */
@Entity
@Table(name="crosstab_definition")
@PrimaryKeyJoinColumn(name="analysis_id")
public class CrosstabDefinition extends AnalysisDefinition {

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="crosstab_definition_id")
    private Long crosstabDefinitionID;

    public Long getCrosstabDefinitionID() {
        return crosstabDefinitionID;
    }

    public void setCrosstabDefinitionID(Long crosstabDefinitionID) {
        this.crosstabDefinitionID = crosstabDefinitionID;
    }

    public WSAnalysisDefinition createWSDefinition() {
        WSCrosstabDefinition wsCrosstabDefinition = new WSCrosstabDefinition();
        wsCrosstabDefinition.setCrosstabDefinitionID(crosstabDefinitionID);
        return wsCrosstabDefinition;
    }

    public AnalysisDefinition clone() throws CloneNotSupportedException {
        CrosstabDefinition crosstabDefinition = (CrosstabDefinition) super.clone();
        crosstabDefinition.setCrosstabDefinitionID(null);
        return crosstabDefinition;
    }
}
