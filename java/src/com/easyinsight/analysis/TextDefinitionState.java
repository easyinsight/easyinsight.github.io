package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSTextDefinition;

import javax.persistence.*;
import java.util.List;

/**
 * User: jamesboe
 * Date: Sep 27, 2010
 * Time: 4:58:50 PM
 */
@Entity
@Table(name="text_report")
public class TextDefinitionState extends AnalysisDefinitionState {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="text_report_id")
    private long textReportID;

    @Column(name="report_text")
    private String reportText;

    public String getReportText() {
        return reportText;
    }

    public void setReportText(String reportText) {
        this.reportText = reportText;
    }

    @Override
    public WSAnalysisDefinition createWSDefinition() {
        WSTextDefinition gantt = new WSTextDefinition();
        gantt.setTextReportID(textReportID);
        gantt.setText(reportText);
        return gantt;
    }

    public long getTextReportID() {
        return textReportID;
    }

    public void setTextReportID(long textReportID) {
        this.textReportID = textReportID;
    }

    @Override
    public AnalysisDefinitionState clone(List<AnalysisItem> allFields) throws CloneNotSupportedException {
        TextDefinitionState ganttChartDefinitionState = (TextDefinitionState) super.clone(allFields);
        ganttChartDefinitionState.setTextReportID(0);
        return ganttChartDefinitionState;
    }
}
