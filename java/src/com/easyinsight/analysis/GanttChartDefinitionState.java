package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSGanttChartDefinition;

import javax.persistence.*;
import java.util.List;

/**
 * User: jamesboe
 * Date: Sep 27, 2010
 * Time: 4:58:50 PM
 */
@Entity
@Table(name="gantt_chart")
public class GanttChartDefinitionState extends AnalysisDefinitionState {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="gantt_chart_id")
    private long ganttDefinitionID;

    @Override
    public WSAnalysisDefinition createWSDefinition() {
        WSGanttChartDefinition gantt = new WSGanttChartDefinition();
        gantt.setGanttDefinitionID(ganttDefinitionID);
        return gantt;
    }

    public long getGanttDefinitionID() {
        return ganttDefinitionID;
    }

    public void setGanttDefinitionID(long ganttDefinitionID) {
        this.ganttDefinitionID = ganttDefinitionID;
    }

    @Override
    public AnalysisDefinitionState clone(List<AnalysisItem> allFields) throws CloneNotSupportedException {
        GanttChartDefinitionState ganttChartDefinitionState = (GanttChartDefinitionState) super.clone(allFields);
        ganttChartDefinitionState.setGanttDefinitionID(0);
        return ganttChartDefinitionState;
    }
}
