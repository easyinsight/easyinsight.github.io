package com.easyinsight.analysis.timeline {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.CustomChangeEvent;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.IReportControlBar;
import com.easyinsight.analysis.ReportControlBar;
import com.easyinsight.analysis.ReportDataEvent;
import com.easyinsight.util.PopUpUtil;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.controls.Button;
import mx.controls.ComboBox;
import mx.controls.Label;
import mx.managers.PopUpManager;

public class TimelineControlBar extends ReportControlBar implements IReportControlBar {

    private var sequenceBox:SequenceBox;


    private var timeline:Timeline;

    private var _selectedReport:Object;

    private var _reportName:String;

    private var _sequence:Sequence;

    public function TimelineControlBar() {
        super();
    }


    [Bindable(event="sequenceChanged")]
    public function get sequence():Sequence {
        return _sequence;
    }

    public function set sequence(value:Sequence):void {
        if (_sequence == value) return;
        _sequence = value;
        //dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
        dispatchEvent(new Event("sequenceChanged"));
    }

    [Bindable(event="reportNameChanged")]
    public function get reportName():String {
        return _reportName;
    }

    public function set reportName(value:String):void {
        if (_reportName == value) return;
        _reportName = value;
        dispatchEvent(new Event("reportNameChanged"));
    }

    [Bindable]
    [Embed(source="../../../../../assets/pencil.png")]
    private var editIcon:Class;

    protected override function createChildren():void {
        super.createChildren();

        var reportLabel:Label = new Label();
        reportLabel.text = "Contained Report:";
        reportLabel.setStyle("fontSize", 14);
        addChild(reportLabel);
        var reportNameLabel:Label = new Label();
        BindingUtils.bindProperty(reportNameLabel, "text", this, "reportName");
        reportNameLabel.setStyle("fontSize", 14);
        addChild(reportNameLabel);
        var editButton:Button = new Button();
        editButton.toolTip = "Choose a Different Report...";
        editButton.setStyle("icon", editIcon);
        editButton.addEventListener(MouseEvent.CLICK, chooseReport);
        addChild(editButton);
        var sequenceLabel:Label = new Label();
        sequenceLabel.text = "Sequence Field:";
        sequenceLabel.setStyle("fontSize", 14);
        addChild(sequenceLabel);
        sequenceBox = new SequenceBox();
        sequenceBox.addEventListener(SequenceUpdateEvent.SEQUENCE_UPDATE, onSequenceUpdate);
        BindingUtils.bindProperty(sequenceBox, "sequence", this, "sequence");
        BindingUtils.bindProperty(this, "sequence", sequenceBox, "sequence");
        addChild(sequenceBox);
    }

    private function onSequenceUpdate(event:SequenceUpdateEvent):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    private function chooseReport(event:MouseEvent):void {
        var window:TimelineReportWindow = new TimelineReportWindow();
        window.dataSourceID = this.dataSourceID;
        window.addEventListener(TimelineReportEvent.TIMELINE_REPORT, choseReport);
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }

    private function choseReport(event:TimelineReportEvent):void {
        timeline.report = event.report;
        reportName = event.report.name;
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    public function set analysisDefinition(analysisDefinition:AnalysisDefinition):void {
        this.timeline = analysisDefinition as Timeline;
        if (timeline.report != null) {
            reportName = timeline.report.name;
        }
        this.sequence = timeline.sequence;
    }

    public function createAnalysisDefinition():AnalysisDefinition {
        this.timeline.sequence = sequenceBox.sequence;        
        return this.timeline;
    }

    public function isDataValid():Boolean {
        return timeline.sequence != null && timeline.report != null;
    }

    public function addItem(analysisItem:AnalysisItem):void {
    }

    public function onCustomChangeEvent(event:CustomChangeEvent):void {
    }

    public function onDataReceipt(event:DataServiceEvent):void {
    }
}
}