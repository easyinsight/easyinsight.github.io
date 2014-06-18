package com.easyinsight.analysis.heatmap {
import com.easyinsight.analysis.AnalysisChangedEvent;
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.AnalysisItemUpdateEvent;
import com.easyinsight.analysis.CustomChangeEvent;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.DimensionDropArea;
import com.easyinsight.analysis.IReportControlBar;
import com.easyinsight.analysis.ListDropAreaGrouping;
import com.easyinsight.analysis.MeasureDropArea;
import com.easyinsight.analysis.ReportControlBar;
import com.easyinsight.analysis.ReportDataEvent;

import flash.events.Event;

import mx.containers.HBox;
import mx.containers.VBox;
import mx.containers.ViewStack;
import mx.controls.Label;
import mx.controls.RadioButton;
import mx.controls.RadioButtonGroup;

public class TopoMapControlBar extends ReportControlBar implements IReportControlBar {

    private var regionGrouping:ListDropAreaGrouping;

    private var measureGrouping:ListDropAreaGrouping;

    private var topoMapDefinition:TopoMapDefinition;

    public function TopoMapControlBar() {
        super();
        regionGrouping = new ListDropAreaGrouping();
        regionGrouping.maxElements = 1;
        regionGrouping.dropAreaType = DimensionDropArea;
        regionGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        measureGrouping = new ListDropAreaGrouping();
        measureGrouping.maxElements = 1;
        measureGrouping.dropAreaType = MeasureDropArea;
        measureGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
    }

    private function requestListData(event:AnalysisItemUpdateEvent):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    private function onUpdate(event:AnalysisItemUpdateEvent):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }


    override protected function createChildren():void {
        super.createChildren();

        var zipLabel:Label = new Label();
        zipLabel.text = "Region:";
        zipLabel.setStyle("fontSize", 14);
        addChild(zipLabel);
        addDropAreaGrouping(regionGrouping);

        var measureLabel:Label = new Label();
        measureLabel.text = "Measure:";
        measureLabel.setStyle("fontSize", 14);
        addChild(measureLabel);
        addDropAreaGrouping(measureGrouping);

        if (topoMapDefinition.region != null) {
            regionGrouping.addAnalysisItem(topoMapDefinition.region);
        }
        if (topoMapDefinition.measure != null) {
            measureGrouping.addAnalysisItem(topoMapDefinition.measure);
        }
    }

    public function set analysisDefinition(analysisDefinition:AnalysisDefinition):void {
        this.topoMapDefinition = analysisDefinition as TopoMapDefinition;
    }

    public function createAnalysisDefinition():AnalysisDefinition {
        topoMapDefinition.region = regionGrouping.getListColumns()[0];
        topoMapDefinition.measure = measureGrouping.getListColumns()[0];
        return topoMapDefinition;
    }

    public function isDataValid():Boolean {
        return (regionGrouping.getListColumns().length > 0 && measureGrouping.getListColumns().length > 0);
    }

    public function addItem(analysisItem:AnalysisItem):void {
        if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
            measureGrouping.addAnalysisItem(analysisItem);
        }
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
        dispatchEvent(new AnalysisChangedEvent(false));
    }

    public function onCustomChangeEvent(event:CustomChangeEvent):void {
    }

    public function onDataReceipt(event:DataServiceEvent):void {
    }
}
}