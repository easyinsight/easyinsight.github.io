package com.easyinsight.analysis.gauge {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.AnalysisItemUpdateEvent;
import com.easyinsight.analysis.CustomChangeEvent;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.IReportControlBar;
import com.easyinsight.analysis.ListDropAreaGrouping;
import com.easyinsight.analysis.MeasureDropArea;
import com.easyinsight.analysis.ReportControlBar;
import com.easyinsight.analysis.ReportDataEvent;
import flash.events.Event;

import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.Button;
import mx.controls.ComboBox;
import mx.controls.Label;
import mx.controls.TextInput;
public class GaugeControlBar extends ReportControlBar implements IReportControlBar {

    private var measureGrouping:ListDropAreaGrouping;
    private var benchmarkGrouping:ListDropAreaGrouping;
    private var maxValueInput:TextInput;
    private var gaugeTypeBox:ComboBox;
    private var gaugeDefinition:GaugeDefinition;

    public function GaugeControlBar() {
        measureGrouping = new ListDropAreaGrouping();
        measureGrouping.maxElements = 1;
        measureGrouping.dropAreaType = MeasureDropArea;
        measureGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        benchmarkGrouping = new ListDropAreaGrouping();
        benchmarkGrouping.maxElements = 1;
        benchmarkGrouping.dropAreaType = MeasureDropArea;
        benchmarkGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        maxValueInput = new TextInput();
        gaugeTypeBox = new ComboBox();
        gaugeTypeBox.dataProvider = new ArrayCollection( [{label: "Circular Gauge", type: GaugeDefinition.CIRCULAR_GAUGE},
            {label: "Horizontal Gauge", type: GaugeDefinition.HORIZONTAL_GAUGE}]);
        gaugeTypeBox.addEventListener(Event.CHANGE, onChange);
        setStyle("verticalAlign", "middle");
    }

    private function onChange(event:Event):void {
        var type:int = int(gaugeTypeBox.selectedItem.type);
        dispatchEvent(new GaugeTypeEvent(type));
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA, false));
    }


    override protected function createChildren():void {
        super.createChildren();
        /*var gaugeLabel:Label = new Label();
        gaugeLabel.text = "Gauge Type: ";
        addChild(gaugeLabel);
        addChild(gaugeTypeBox);*/
        var measureLabel:Label = new Label();
        measureLabel.text = "Measure: ";
        addChild(measureLabel);
        addDropAreaGrouping(measureGrouping);

        var benchmarkLabel:Label = new Label();
        benchmarkLabel.text = "Benchmark: ";
        addChild(benchmarkLabel);
        addDropAreaGrouping(benchmarkGrouping);

        var maxValueLabel:Label = new Label();
        maxValueLabel.text = "Max Value: ";
        addChild(maxValueLabel);
        addChild(maxValueInput);
        var applyButton:Button = new Button();
        applyButton.label = "Apply Changes";
        applyButton.addEventListener(MouseEvent.CLICK, onApplyClick);
        addChild(applyButton);
        //maxValueInput.addEventListener(Event.CHANGE, onMaxValueChange);
        if (gaugeDefinition.measure != null) {
            measureGrouping.addAnalysisItem(gaugeDefinition.measure);
        }
        if (gaugeDefinition.benchmarkMeasure != null) {
            benchmarkGrouping.addAnalysisItem(gaugeDefinition.benchmarkMeasure);
        }
        maxValueInput.text = String(gaugeDefinition.maxValue);
    }

    private function onApplyClick(event:MouseEvent):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA, false));
    }

    private function onMaxValueChange(event:Event):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA, false));
    }

    private function requestListData(event:AnalysisItemUpdateEvent):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    public function set analysisDefinition(analysisDefinition:AnalysisDefinition):void {
        gaugeDefinition = analysisDefinition as GaugeDefinition;
    }

    public function createAnalysisDefinition():AnalysisDefinition {
        gaugeDefinition.measure = measureGrouping.getListColumns()[0];
        if (benchmarkGrouping.getListColumns().length > 0) {
            gaugeDefinition.benchmarkMeasure = benchmarkGrouping.getListColumns()[0];
        } else {
            gaugeDefinition.benchmarkMeasure = null;
        }
        gaugeDefinition.gaugeType = GaugeDefinition.CIRCULAR_GAUGE;
        gaugeDefinition.maxValue = int(maxValueInput.text);
        return gaugeDefinition;
    }

    public function isDataValid():Boolean {
        var value:int = int(maxValueInput.text);
        return (measureGrouping.getListColumns().length > 0 && value > 0);
    }

    public function addItem(analysisItem:AnalysisItem):void {
        if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
            measureGrouping.addAnalysisItem(analysisItem);
        }
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    public function onCustomChangeEvent(event:CustomChangeEvent):void {
    }

    public function onDataReceipt(event:DataServiceEvent):void {
    }
}
}