package com.easyinsight.analysis.gantt {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemUpdateEvent;
import com.easyinsight.analysis.CustomChangeEvent;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.DimensionDropArea;
import com.easyinsight.analysis.IReportControlBar;
import com.easyinsight.analysis.ListDropAreaGrouping;
import com.easyinsight.analysis.ReportControlBar;
import com.easyinsight.analysis.ReportDataEvent;

import mx.controls.Label;

public class GanttChartControlBar extends ReportControlBar implements IReportControlBar {


    private var startGrouping:ListDropAreaGrouping;
    private var endGrouping:ListDropAreaGrouping;
    private var groupingGrouping:ListDropAreaGrouping;

    private var ganttReport:GanttReport;

    public function GanttChartControlBar() {
        super();
        groupingGrouping = new ListDropAreaGrouping();
        groupingGrouping.maxElements = 1;
        groupingGrouping.dropAreaType = DimensionDropArea;
        groupingGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        startGrouping = new ListDropAreaGrouping();
        startGrouping.maxElements = 1;
        startGrouping.dropAreaType = DimensionDropArea;
        startGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        endGrouping = new ListDropAreaGrouping();
        endGrouping.maxElements = 1;
        endGrouping.dropAreaType = DimensionDropArea;
        endGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
    }

    private function requestListData(event:AnalysisItemUpdateEvent):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    [Embed(source="../../../../../assets/table_edit.png")]
    public var tableEditIcon:Class;

    /*private function onUpdate(event:AnalysisItemUpdateEvent):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }*/

    override protected function createChildren():void {
        super.createChildren();

        var measureLabel:Label = new Label();
        measureLabel.text = "Grouping:";
        measureLabel.setStyle("fontSize", 14);
        addChild(measureLabel);
        addDropAreaGrouping(groupingGrouping);
        var groupingLabel:Label = new Label();
        groupingLabel.text = "Start Time:";
        groupingLabel.setStyle("fontSize", 14);
        addChild(groupingLabel);
        addDropAreaGrouping(startGrouping);
        var longGroupLabel:Label = new Label();
        longGroupLabel.text = "End Time:";
        longGroupLabel.setStyle("fontSize", 14);
        addChild(longGroupLabel);
        addDropAreaGrouping(endGrouping);

        if (ganttReport.startTime != null) {
            startGrouping.addAnalysisItem(ganttReport.startTime);
        }
        if (ganttReport.endTime != null) {
            endGrouping.addAnalysisItem(ganttReport.endTime);
        }
        if (ganttReport.grouping != null) {
            groupingGrouping.addAnalysisItem(ganttReport.grouping);
        }
    }

    public function set analysisDefinition(analysisDefinition:AnalysisDefinition):void {
        this.ganttReport = analysisDefinition as GanttReport;
    }

    public function createAnalysisDefinition():AnalysisDefinition {
        ganttReport.startTime = startGrouping.getListColumns()[0];
        ganttReport.endTime = endGrouping.getListColumns()[0];
        ganttReport.grouping = groupingGrouping.getListColumns()[0];
        return ganttReport;
    }

    public function isDataValid():Boolean {
        return startGrouping.getListColumns().length > 0 && endGrouping.getListColumns().length > 0 &&
                groupingGrouping.getListColumns().length > 0;
    }

    public function addItem(analysisItem:AnalysisItem):void {
    }

    public function onCustomChangeEvent(event:CustomChangeEvent):void {
    }

    public function onDataReceipt(event:DataServiceEvent):void {
    }
}
}