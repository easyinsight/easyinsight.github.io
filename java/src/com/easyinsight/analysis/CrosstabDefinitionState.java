package com.easyinsight.analysis;

import javax.persistence.*;
import java.util.List;

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
        WSCrosstabDefinition crosstab = new WSCrosstabDefinition();
        crosstab.setReportType(WSAnalysisDefinition.CROSSTAB);
        crosstab.setCrosstabDefinitionID(crosstabDefinitionID);
        return crosstab;
    }

    public AnalysisDefinitionState clone(List<AnalysisItem> allFields) throws CloneNotSupportedException {
        CrosstabDefinitionState crosstabDefinitionState = (CrosstabDefinitionState) super.clone(allFields);
        crosstabDefinitionState.setCrosstabDefinitionID(0);
        return crosstabDefinitionState;
    }
}
