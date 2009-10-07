package com.easyinsight.analysis.charts.yaxisbased {
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
import com.easyinsight.analysis.ReportDataEvent;
import com.easyinsight.analysis.charts.ChartDefinitionEditWindow;
import com.easyinsight.analysis.charts.ChartRotationEvent;

import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.Label;
import mx.controls.LinkButton;
import mx.events.FlexEvent;
import mx.managers.PopUpManager;

public class YAxisControlBar extends HBox implements IReportControlBar  {

    private var yAxisGrouping:ListDropAreaGrouping;
    private var measureGrouping:ListDropAreaGrouping;

    private var yAxisDefinition:YAxisDefinition;

    public function YAxisControlBar() {
        yAxisGrouping = new ListDropAreaGrouping();
        yAxisGrouping.maxElements = 1;
        yAxisGrouping.dropAreaType = DimensionDropArea;
        yAxisGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        measureGrouping = new ListDropAreaGrouping();
        measureGrouping.maxElements = 1;
        measureGrouping.dropAreaType = MeasureDropArea;
        measureGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        setStyle("verticalAlign", "middle");
    }

    override protected function createChildren():void {
        super.createChildren();
        var groupingLabel:Label = new Label();
        groupingLabel.text = "Y Axis Grouping:";
        groupingLabel.setStyle("fontSize", 14);
        addChild(groupingLabel);
        addChild(yAxisGrouping);
        var measureLabel:Label = new Label();
        measureLabel.text = "X Axis Measure:";
        measureLabel.setStyle("fontSize", 14);
        addChild(measureLabel);
        addChild(measureGrouping);
         if (yAxisDefinition.yaxis != null) {
            yAxisGrouping.addAnalysisItem(yAxisDefinition.yaxis);
        }
        if (yAxisDefinition.measure != null) {
            measureGrouping.addAnalysisItem(yAxisDefinition.measure);
        }
        var limitLabel:LinkButton = new LinkButton();
        limitLabel.setStyle("textDecoration", "underline");
        limitLabel.addEventListener(MouseEvent.CLICK, editLimits);
        BindingUtils.bindProperty(limitLabel, "label", this, "limitText");
        addChild(limitLabel);
    }

    private function editLimits(event:MouseEvent):void {
        var window:ChartDefinitionEditWindow = new ChartDefinitionEditWindow();
        window.chartDefinition = yAxisDefinition;
        window.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, onUpdate);
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
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
        yAxisDefinition = analysisDefinition as YAxisDefinition;
    }

    public function isDataValid():Boolean {
        return (yAxisGrouping.getListColumns().length > 0 && measureGrouping.getListColumns().length > 0);
    }

    public function createAnalysisDefinition():AnalysisDefinition {
        yAxisDefinition.yaxis = yAxisGrouping.getListColumns()[0];
        yAxisDefinition.measure = measureGrouping.getListColumns()[0];
        return yAxisDefinition;
    }

    public function set analysisItems(analysisItems:ArrayCollection):void {
        yAxisGrouping.analysisItems = analysisItems;
        measureGrouping.analysisItems = analysisItems;
    }

    public function addItem(analysisItem:AnalysisItem):void {
        if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
            measureGrouping.addAnalysisItem(analysisItem);
        } else if (analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
            yAxisGrouping.addAnalysisItem(analysisItem);
        }
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    public function onCustomChangeEvent(event:CustomChangeEvent):void {
        if (event is ChartRotationEvent) {
            var chartEvent:ChartRotationEvent = event as ChartRotationEvent;
            yAxisDefinition.elevationAngle = chartEvent.elevation;
            yAxisDefinition.rotationAngle = chartEvent.rotation;
        }
    }
}
}