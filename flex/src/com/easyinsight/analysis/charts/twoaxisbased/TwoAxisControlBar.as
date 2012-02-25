package com.easyinsight.analysis.charts.twoaxisbased {
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
import com.easyinsight.analysis.ReportPropertiesEvent;
import com.easyinsight.analysis.charts.ChartRotationEvent;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.containers.VBox;
import mx.containers.ViewStack;
import mx.controls.Alert;
import mx.controls.Label;
import mx.controls.LinkButton;
import mx.controls.RadioButton;
import mx.controls.RadioButtonGroup;
import mx.events.FlexEvent;

public class TwoAxisControlBar extends ReportControlBar implements IReportControlBar {
    private var xAxisGrouping:ListDropAreaGrouping;
    private var yAxisGrouping:ListDropAreaGrouping;
    private var measureGrouping:ListDropAreaGrouping;
    private var measuresGrouping:ListDropAreaGrouping;

    private var xAxisDefinition:TwoAxisDefinition;

    public function TwoAxisControlBar() {
        xAxisGrouping = new ListDropAreaGrouping();
        xAxisGrouping.maxElements = 1;
        xAxisGrouping.dropAreaType = DimensionDropArea;
        xAxisGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        yAxisGrouping = new ListDropAreaGrouping();
        yAxisGrouping.maxElements = 1;
        yAxisGrouping.dropAreaType = DimensionDropArea;
        yAxisGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        measureGrouping = new ListDropAreaGrouping();
        measureGrouping.maxElements = 1;
        measureGrouping.dropAreaType = MeasureDropArea;
        measureGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        measuresGrouping = new ListDropAreaGrouping();
        measuresGrouping.unlimited = true;
        measuresGrouping.dropAreaType = MeasureDropArea;
        measuresGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        setStyle("verticalAlign", "middle");
    }
    private var stack:ViewStack;

    private function onChange(event:Event):void {
        if (event.currentTarget.selectedValue == "grouping") {
            stack.selectedIndex = 0;
            xAxisDefinition.multiMeasure = false;
        } else {
            stack.selectedIndex = 1;
            xAxisDefinition.multiMeasure = true;
        }
    }

    override public function highlight(analysisItem:AnalysisItem):Boolean {
        var valid:Boolean = false;
        if (stack.selectedIndex == 0) {
            if (analysisItem.hasType(AnalysisItemTypes.DATE)) {
                valid = valid || xAxisGrouping.highlight(analysisItem);
            } else if (analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
                valid = valid || yAxisGrouping.highlight(analysisItem);
            } else {
                valid = valid || measureGrouping.highlight(analysisItem);
            }
        } else {
            if (analysisItem.hasType(AnalysisItemTypes.DATE)) {
                valid = valid || xAxisGrouping.highlight(analysisItem);
            } else if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                valid = valid || measuresGrouping.highlight(analysisItem);
            }
        }
        return valid;
    }

    override public function normal():void {
        super.normal();
        if (stack.selectedIndex == 0) {
            yAxisGrouping.normal();
            measureGrouping.normal();
        } else {
            measuresGrouping.normal();
        }
    }

    override protected function createChildren():void {
        super.createChildren();

        var xAxisGroupingLabel:Label = new Label();
        xAxisGroupingLabel.text = "X Axis Grouping:";
        xAxisGroupingLabel.setStyle("fontSize", 14);
        addChild(xAxisGroupingLabel);
        addDropAreaGrouping(xAxisGrouping);

        var displayTypeVBox:VBox = new VBox();
        var displayTypeRadioGroup:RadioButtonGroup = new RadioButtonGroup();
        displayTypeRadioGroup.addEventListener(Event.CHANGE, onChange);
        var heatMapRadio:RadioButton = new RadioButton();
        heatMapRadio.label = "By Grouping:";
        heatMapRadio.value = "grouping";
        heatMapRadio.group = displayTypeRadioGroup;
        heatMapRadio.selected = !xAxisDefinition.multiMeasure;
        var pointMapRadio:RadioButton = new RadioButton();
        pointMapRadio.label = "By Multiple Measures:";
        pointMapRadio.value = "multimeasure";
        pointMapRadio.group = displayTypeRadioGroup;
        pointMapRadio.selected = xAxisDefinition.multiMeasure;
        displayTypeVBox.addChild(heatMapRadio);
        displayTypeVBox.addChild(pointMapRadio);
        addChild(displayTypeVBox);

        stack = new ViewStack();
        stack.percentWidth = 100;
        stack.resizeToContent = true;
        var box1:HBox = new HBox();
        box1.percentWidth = 100;
        var box2:HBox = new HBox();
        box2.percentWidth = 100;
        var groupingLabel:Label = new Label();
        groupingLabel.text = "Y Axis Grouping:";
        groupingLabel.setStyle("fontSize", 14);
        box1.addChild(groupingLabel);
        addDropAreaGrouping(yAxisGrouping, box1);
        var measureGroupingLabel:Label = new Label();
        measureGroupingLabel.text = "Measure:";
        measureGroupingLabel.setStyle("fontSize", 14);
        box1.addChild(measureGroupingLabel);
        addDropAreaGrouping(measureGrouping, box1);

        var measuresGroupingLabel:Label = new Label();
        measuresGroupingLabel.text = "Measures:";
        measuresGroupingLabel.setStyle("fontSize", 14);
        box2.addChild(measuresGroupingLabel);
        addDropAreaGrouping(measuresGrouping, box2);

        stack.addChild(box1);
        stack.addChild(box2);
        addChild(stack);

        stack.selectedIndex = xAxisDefinition.multiMeasure ? 1 : 0;

        if (xAxisDefinition.xaxis != null) {
            xAxisGrouping.addAnalysisItem(xAxisDefinition.xaxis);
        }
        if (xAxisDefinition.yaxis != null) {
            yAxisGrouping.addAnalysisItem(xAxisDefinition.yaxis);
        }
        if (xAxisDefinition.measure != null) {
            measureGrouping.addAnalysisItem(xAxisDefinition.measure);
        }
        if (xAxisDefinition.measures != null) {
            for each (var item:AnalysisItem in xAxisDefinition.measures) {
                measuresGrouping.addAnalysisItem(item);
            }
        }
        var limitLabel:LinkButton = new LinkButton();
        limitLabel.setStyle("textDecoration", "underline");
        limitLabel.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {
            dispatchEvent(new ReportPropertiesEvent(2));
        });
        BindingUtils.bindProperty(limitLabel, "label", this, "limitText");
        addChild(limitLabel);
    }

    private function onUpdate(event:AnalysisItemUpdateEvent):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    private var _limitText:String;

    [Bindable]
    public function get limitText():String {
        return _limitText;
    }

    public function set limitText(val:String):void {
        _limitText = val;
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    public function onDataReceipt(event:DataServiceEvent):void {
        if (event.limitedResults) {
            limitText = "Showing " + event.limitResults + " of " + event.maxResults + " results";
        } else {
            limitText = "";
        }
    }

    private function requestListData(event:AnalysisItemUpdateEvent):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    public function set analysisDefinition(analysisDefinition:AnalysisDefinition):void {
        xAxisDefinition = analysisDefinition as TwoAxisDefinition;
    }

    public function isDataValid():Boolean {
        if (xAxisDefinition.multiMeasure) {
            return xAxisGrouping.getListColumns().length > 0 && measuresGrouping.getListColumns().length > 0;
        } else {
            return (xAxisGrouping.getListColumns().length > 0 && measureGrouping.getListColumns().length > 0 &&
                yAxisGrouping.getListColumns().length > 0);
        }
    }

    public function createAnalysisDefinition():AnalysisDefinition {
        xAxisDefinition.xaxis = xAxisGrouping.getListColumns()[0];
        xAxisDefinition.yaxis = yAxisGrouping.getListColumns()[0];
        xAxisDefinition.measure = measureGrouping.getListColumns()[0];
        xAxisDefinition.measures = new ArrayCollection(measuresGrouping.getListColumns());
        return xAxisDefinition;
    }

    public function addItem(analysisItem:AnalysisItem):void {
        if (xAxisDefinition.multiMeasure) {
            if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                measuresGrouping.addAnalysisItem(analysisItem);
            } else {
                xAxisGrouping.addAnalysisItem(analysisItem);
            }
        } else {
            if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                measureGrouping.addAnalysisItem(analysisItem);
            } else if (analysisItem.hasType(AnalysisItemTypes.DATE)) {
                xAxisGrouping.addAnalysisItem(analysisItem);
            } else if (analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
                yAxisGrouping.addAnalysisItem(analysisItem);
            }
        }
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    public function onCustomChangeEvent(event:CustomChangeEvent):void {
        if (event is ChartRotationEvent) {
            var chartEvent:ChartRotationEvent = event as ChartRotationEvent;
            xAxisDefinition.elevationAngle = chartEvent.elevation;
            xAxisDefinition.rotationAngle = chartEvent.rotation;
        }
    }
}
}