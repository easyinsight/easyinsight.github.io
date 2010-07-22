package com.easyinsight.analysis.heatmap {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemUpdateEvent;
import com.easyinsight.analysis.CustomChangeEvent;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.DimensionDropArea;
import com.easyinsight.analysis.IReportControlBar;
import com.easyinsight.analysis.ListDropAreaGrouping;
import com.easyinsight.analysis.MeasureDropArea;
import com.easyinsight.analysis.ReportControlBar;
import com.easyinsight.analysis.ReportDataEvent;
import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;

import mx.controls.Button;
import mx.controls.Label;
import mx.managers.PopUpManager;

public class HeatMapControlBar extends ReportControlBar implements IReportControlBar {

    private var latGrouping:ListDropAreaGrouping;
    private var longGrouping:ListDropAreaGrouping;
    private var measureGrouping:ListDropAreaGrouping;

    private var heatMapDefinition:HeatMapDefinition;

    public function HeatMapControlBar() {
        super();
        var pieEditButton:Button = new Button();
        pieEditButton.setStyle("icon", tableEditIcon);
        pieEditButton.toolTip = "Edit Heat Map Properties...";
        pieEditButton.addEventListener(MouseEvent.CLICK, editLimits);
        addChild(pieEditButton);
        latGrouping = new ListDropAreaGrouping();
        latGrouping.maxElements = 1;
        latGrouping.dropAreaType = DimensionDropArea;
        latGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        longGrouping = new ListDropAreaGrouping();
        longGrouping.maxElements = 1;
        longGrouping.dropAreaType = DimensionDropArea;
        longGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        measureGrouping = new ListDropAreaGrouping();
        measureGrouping.maxElements = 1;
        measureGrouping.dropAreaType = MeasureDropArea;
        measureGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
    }

    private function requestListData(event:AnalysisItemUpdateEvent):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    [Embed(source="../../../../../assets/table_edit.png")]
    public var tableEditIcon:Class;

    private function editLimits(event:MouseEvent):void {
        var window:HeatMapDefinitionEditWindow = new HeatMapDefinitionEditWindow();
        window.heatMapDefinition = heatMapDefinition;
        window.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, onUpdate);
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }

    private function onUpdate(event:AnalysisItemUpdateEvent):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    override protected function createChildren():void {
        super.createChildren();
        var groupingLabel:Label = new Label();
        groupingLabel.text = "Latitude Grouping:";
        groupingLabel.setStyle("fontSize", 14);
        addChild(groupingLabel);
        addDropAreaGrouping(latGrouping);
        var longGroupLabel:Label = new Label();
        longGroupLabel.text = "Longitude Grouping:";
        longGroupLabel.setStyle("fontSize", 14);
        addChild(longGroupLabel);
        addDropAreaGrouping(longGrouping);
        var measureLabel:Label = new Label();
        measureLabel.text = "Measure:";
        measureLabel.setStyle("fontSize", 14);
        addChild(measureLabel);
        addDropAreaGrouping(measureGrouping);

        if (heatMapDefinition.latitudeItem != null) {
            latGrouping.addAnalysisItem(heatMapDefinition.latitudeItem);
        }
        if (heatMapDefinition.longitudeItem != null) {
            longGrouping.addAnalysisItem(heatMapDefinition.longitudeItem);
        }
        if (heatMapDefinition.measure != null) {
            measureGrouping.addAnalysisItem(heatMapDefinition.measure);
        }
        /*var limitLabel:LinkButton = new LinkButton();
        limitLabel.setStyle("textDecoration", "underline");
        limitLabel.addEventListener(MouseEvent.CLICK, editLimits);
        BindingUtils.bindProperty(limitLabel, "label", this, "limitText");
        addChild(limitLabel);*/
    }

    public function set analysisDefinition(analysisDefinition:AnalysisDefinition):void {
        this.heatMapDefinition = analysisDefinition as HeatMapDefinition;
    }

    public function createAnalysisDefinition():AnalysisDefinition {
        heatMapDefinition.latitudeItem = latGrouping.getListColumns()[0];
        heatMapDefinition.longitudeItem = longGrouping.getListColumns()[0];
        heatMapDefinition.measure = measureGrouping.getListColumns()[0];
        return heatMapDefinition;
    }

    public function isDataValid():Boolean {
        return (latGrouping.getListColumns().length > 0 && measureGrouping.getListColumns().length > 0 &&
                longGrouping.getListColumns().length > 0);
    }

    public function addItem(analysisItem:AnalysisItem):void {
    }

    public function onCustomChangeEvent(event:CustomChangeEvent):void {
    }

    public function onDataReceipt(event:DataServiceEvent):void {
    }
}
}