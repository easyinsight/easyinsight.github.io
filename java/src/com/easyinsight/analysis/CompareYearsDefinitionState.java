package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSCompareYearsDefinition;

import javax.persistence.*;
import java.util.List;

/**
 * User: jamesboe
 * Date: Sep 27, 2010
 * Time: 4:58:50 PM
 */
@Entity
@Table(name="compare_time_report")
public class CompareYearsDefinitionState extends AnalysisDefinitionState {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="compare_time_report_id")
    private long ytdID;

    @Override
    public WSAnalysisDefinition createWSDefinition() {
        WSCompareYearsDefinition gantt = new WSCompareYearsDefinition();
        gantt.setYtdID(ytdID);
        return gantt;
    }

    public long getYtdID() {
        return ytdID;
    }

    public void setYtdID(long ytdID) {
        this.ytdID = ytdID;
    }

    @Override
    public AnalysisDefinitionState clone(List<AnalysisItem> allFields) throws CloneNotSupportedException {
        CompareYearsDefinitionState verticalListDefinitionState = (CompareYearsDefinitionState) super.clone(allFields);
        verticalListDefinitionState.setYtdID(0);
        return verticalListDefinitionState;
    }
}
