package com.easyinsight.analysis;

import javax.persistence.*;
import java.util.List;

/**
 * User: James Boe
 * Date: Mar 31, 2009
 * Time: 10:06:02 AM
 */
@Entity
@Table(name="summary_report")
public class SummaryDefinitionState extends AnalysisDefinitionState {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="summary_report_id")
    private long summaryDefinitionID;

    public long getSummaryDefinitionID() {
        return summaryDefinitionID;
    }

    public void setSummaryDefinitionID(long summaryDefinitionID) {
        this.summaryDefinitionID = summaryDefinitionID;
    }

    public WSAnalysisDefinition createWSDefinition() {
        WSSummaryDefinition wsSummaryDefinition = new WSSummaryDefinition();
        wsSummaryDefinition.setSummaryDefinitionID(summaryDefinitionID);
        return wsSummaryDefinition;
    }

    @Override
    public AnalysisDefinitionState clone(List<AnalysisItem> allFields) throws CloneNotSupportedException {
        SummaryDefinitionState state = (SummaryDefinitionState) super.clone(allFields);
        state.setSummaryDefinitionID(0);
        return state;
    }
}