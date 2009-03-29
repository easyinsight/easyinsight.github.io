package com.easyinsight.analysis;

import javax.persistence.*;

/**
 * User: James Boe
 * Date: Mar 29, 2009
 * Time: 9:25:28 AM
 */
@Entity
@PrimaryKeyJoinColumn(name="report_state_id")
@Table(name="crosstab_report")
public class CrosstabDefinitionState extends AnalysisDefinitionState {
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="crosstab_report_id")
    private long crosstabDefinitionID;

    public long getCrosstabDefinitionID() {
        return crosstabDefinitionID;
    }

    public void setCrosstabDefinitionID(long crosstabDefinitionID) {
        this.crosstabDefinitionID = crosstabDefinitionID;
    }

    public WSAnalysisDefinition createWSDefinition() {
        return new WSCrosstabDefinition();
    }

    public AnalysisDefinitionState clone() throws CloneNotSupportedException {
        CrosstabDefinitionState crosstabDefinitionState = (CrosstabDefinitionState) super.clone();
        crosstabDefinitionState.setCrosstabDefinitionID(0);
        return crosstabDefinitionState;
    }
}
