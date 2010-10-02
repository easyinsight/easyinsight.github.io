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

import flash.events.Event;
import flash.events.MouseEvent;

import mx.containers.HBox;
import mx.containers.VBox;
import mx.containers.ViewStack;
import mx.controls.Button;
import mx.controls.Label;
import mx.controls.RadioButton;
import mx.controls.RadioButtonGroup;
import mx.managers.PopUpManager;

public class HeatMapControlBar extends ReportControlBar implements IReportControlBar {

    private var zipGrouping:ListDropAreaGrouping;
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
        zipGrouping = new ListDropAreaGrouping();
        zipGrouping.maxElements = 1;
        zipGrouping.dropAreaType = DimensionDropArea;
        zipGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
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

    private function onChange(event:Event):void {
        if (event.currentTarget.selectedValue == "zip") {
            stack.selectedIndex = 0;
        } else {
            stack.selectedIndex = 1;
        }
    }

    private function onTypeChange(event:Event):void {
        if (event.currentTarget.selectedValue == "heatmap") {
            heatMapDefinition.displayType = HeatMapDefinition.HEAT_MAP;
        } else {
            heatMapDefinition.displayType = HeatMapDefinition.POINT_MAP;
        }
        requestListData(null);
    }

    private var stack:ViewStack;

    override protected function createChildren():void {
        super.createChildren();

        var displayTypeVBox:VBox = new VBox();
        var displayTypeRadioGroup:RadioButtonGroup = new RadioButtonGroup();
        displayTypeRadioGroup.addEventListener(Event.CHANGE, onTypeChange);
        var heatMapRadio:RadioButton = new RadioButton();
        heatMapRadio.label = "Heat Map";
        heatMapRadio.value = "heatmap";
        heatMapRadio.group = displayTypeRadioGroup;
        heatMapRadio.selected = heatMapDefinition.displayType == HeatMapDefinition.HEAT_MAP;
        var pointMapRadio:RadioButton = new RadioButton();
        pointMapRadio.label = "Points";
        pointMapRadio.value = "points";
        pointMapRadio.group = displayTypeRadioGroup;
        pointMapRadio.selected = heatMapDefinition.displayType == HeatMapDefinition.POINT_MAP;
        displayTypeVBox.addChild(heatMapRadio);
        displayTypeVBox.addChild(pointMapRadio);
        addChild(displayTypeVBox);

        var vbox:VBox = new VBox();
        var radioGroup:RadioButtonGroup = new RadioButtonGroup();
        radioGroup.addEventListener(Event.CHANGE, onChange);
        var zipRadio:RadioButton = new RadioButton();
        zipRadio.label = "Use Zip Code";
        zipRadio.value = "zip";
        zipRadio.group = radioGroup;
        zipRadio.selected = heatMapDefinition.latitudeItem == null && heatMapDefinition.longitudeItem == null;
        var latLongRadio:RadioButton = new RadioButton();
        latLongRadio.label = "Use Longitude/Latitude";
        latLongRadio.value = "latlong";
        latLongRadio.group = radioGroup;
        latLongRadio.selected = heatMapDefinition.latitudeItem != null && heatMapDefinition.longitudeItem != null;
        vbox.addChild(zipRadio);
        vbox.addChild(latLongRadio);
        addChild(vbox);

        stack = new ViewStack();
        stack.resizeToContent = true;
        var box1:HBox = new HBox();
        var box2:HBox = new HBox();
        var zipLabel:Label = new Label();
        zipLabel.text = "Zip Code:";
        zipLabel.setStyle("fontSize", 14);
        box1.addChild(zipLabel);
        addDropAreaGrouping(zipGrouping, box1);
        var groupingLabel:Label = new Label();
        groupingLabel.text = "Latitude:";
        groupingLabel.setStyle("fontSize", 14);
        box2.addChild(groupingLabel);
        addDropAreaGrouping(latGrouping, box2);
        var longGroupLabel:Label = new Label();
        longGroupLabel.text = "Longitude:";
        longGroupLabel.setStyle("fontSize", 14);
        box2.addChild(longGroupLabel);
        addDropAreaGrouping(longGrouping, box2);
        stack.addChild(box1);
        stack.addChild(box2);
        addChild(stack);

        var measureLabel:Label = new Label();
        measureLabel.text = "Measure:";
        measureLabel.setStyle("fontSize", 14);
        addChild(measureLabel);
        addDropAreaGrouping(measureGrouping);
        if (heatMapDefinition.zipCode != null) {
            zipGrouping.addAnalysisItem(heatMapDefinition.zipCode);
        }
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
        heatMapDefinition.zipCode = zipGrouping.getListColumns()[0];
        heatMapDefinition.latitudeItem = latGrouping.getListColumns()[0];
        heatMapDefinition.longitudeItem = longGrouping.getListColumns()[0];
        heatMapDefinition.measure = measureGrouping.getListColumns()[0];
        return heatMapDefinition;
    }

    public function isDataValid():Boolean {
        return ((zipGrouping.getListColumns().length > 0 || (latGrouping.getListColumns().length > 0 && longGrouping.getListColumns().length > 0 )) &&
                measureGrouping.getListColumns().length > 0);
    }

    public function addItem(analysisItem:AnalysisItem):void {
    }

    public function onCustomChangeEvent(event:CustomChangeEvent):void {
    }

    public function onDataReceipt(event:DataServiceEvent):void {
    }
}
}