package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSVerticalListDefinition;
import com.easyinsight.analysis.definitions.WSYTDDefinition;

import javax.persistence.*;
import java.util.List;

/**
 * User: jamesboe
 * Date: Sep 27, 2010
 * Time: 4:58:50 PM
 */
@Entity
@Table(name="ytd_report")
public class YTDDefinitionState extends AnalysisDefinitionState {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="ytd_report_id")
    private long ytdID;

    @Override
    public WSAnalysisDefinition createWSDefinition() {
        WSYTDDefinition gantt = new WSYTDDefinition();
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
        YTDDefinitionState verticalListDefinitionState = (YTDDefinitionState) super.clone(allFields);
        verticalListDefinitionState.setYtdID(0);
        return verticalListDefinitionState;
    }
}
