package com.easyinsight.analysis.charts.bubble {
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

import com.easyinsight.analysis.charts.ChartDefinitionEditWindow;

import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.Label;
import mx.controls.LinkButton;
import mx.events.FlexEvent;
import mx.managers.PopUpManager;

public class BubbleChartControlBar extends ReportControlBar implements IReportControlBar  {

    private var dimensionGrouping:ListDropAreaGrouping;
    private var xmeasureGrouping:ListDropAreaGrouping;
    private var ymeasureGrouping:ListDropAreaGrouping;
    private var zmeasureGrouping:ListDropAreaGrouping;

    private var xAxisDefinition:BubbleChartDefinition;

    public function BubbleChartControlBar() {
        dimensionGrouping = new ListDropAreaGrouping();
        dimensionGrouping.maxElements = 1;
        dimensionGrouping.dropAreaType = DimensionDropArea;
        dimensionGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        xmeasureGrouping = new ListDropAreaGrouping();
        xmeasureGrouping.maxElements = 1;
        xmeasureGrouping.dropAreaType = MeasureDropArea;
        xmeasureGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        ymeasureGrouping = new ListDropAreaGrouping();
        ymeasureGrouping.maxElements = 1;
        ymeasureGrouping.dropAreaType = MeasureDropArea;
        ymeasureGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        zmeasureGrouping = new ListDropAreaGrouping();
        zmeasureGrouping.maxElements = 1;
        zmeasureGrouping.dropAreaType = MeasureDropArea;
        zmeasureGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        setStyle("verticalAlign", "middle");
    }

    override protected function createChildren():void {
        super.createChildren();
        var groupingLabel:Label = new Label();
        groupingLabel.text = "Grouping:";
        groupingLabel.setStyle("fontSize", 14);
        addChild(groupingLabel);
        addDropAreaGrouping(dimensionGrouping);

        var yMeasureLabel:Label = new Label();
        yMeasureLabel.text = "Y Axis Measure:";
        yMeasureLabel.setStyle("fontSize", 14);
        addChild(yMeasureLabel);
        addDropAreaGrouping(ymeasureGrouping);
        
        var xMeasureLabel:Label = new Label();
        xMeasureLabel.text = "X Axis Measure:";
        xMeasureLabel.setStyle("fontSize", 14);
        addChild(xMeasureLabel);
        addDropAreaGrouping(xmeasureGrouping);

        var zMeasureLabel:Label = new Label();
        zMeasureLabel.text = "Radius Measure:";
        zMeasureLabel.setStyle("fontSize", 14);
        addChild(zMeasureLabel);
        addDropAreaGrouping(zmeasureGrouping);
         if (xAxisDefinition.dimension != null) {
            dimensionGrouping.addAnalysisItem(xAxisDefinition.dimension);
        }
        if (xAxisDefinition.xaxisMeasure != null) {
            xmeasureGrouping.addAnalysisItem(xAxisDefinition.xaxisMeasure);
        }
        if (xAxisDefinition.yaxisMeasure != null) {
            ymeasureGrouping.addAnalysisItem(xAxisDefinition.yaxisMeasure);
        }
        if (xAxisDefinition.zaxisMeasure != null) {
            zmeasureGrouping.addAnalysisItem(xAxisDefinition.zaxisMeasure);
        }
        var limitLabel:LinkButton = new LinkButton();
        limitLabel.setStyle("textDecoration", "underline");
        limitLabel.addEventListener(MouseEvent.CLICK, editLimits);
        BindingUtils.bindProperty(limitLabel, "label", this, "limitText");
        addChild(limitLabel);
    }

    private function editLimits(event:MouseEvent):void {
        var window:ChartDefinitionEditWindow = new ChartDefinitionEditWindow();
        window.chartDefinition = xAxisDefinition;
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
        xAxisDefinition = analysisDefinition as BubbleChartDefinition;
    }

    public function isDataValid():Boolean {
        return (dimensionGrouping.getListColumns().length > 0 && xmeasureGrouping.getListColumns().length > 0
                && ymeasureGrouping.getListColumns().length > 0 && zmeasureGrouping.getListColumns().length > 0);
    }

    public function createAnalysisDefinition():AnalysisDefinition {
        xAxisDefinition.dimension = dimensionGrouping.getListColumns()[0];
        xAxisDefinition.xaxisMeasure = xmeasureGrouping.getListColumns()[0];
        xAxisDefinition.yaxisMeasure = ymeasureGrouping.getListColumns()[0];
        xAxisDefinition.zaxisMeasure = zmeasureGrouping.getListColumns()[0];
        return xAxisDefinition;
    }

    public function addItem(analysisItem:com.easyinsight.analysis.AnalysisItem):void {
        if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
            if (xAxisDefinition.xaxisMeasure == null || (xAxisDefinition.yaxisMeasure != null &&
                    xAxisDefinition.zaxisMeasure != null)) {
                xmeasureGrouping.addAnalysisItem(analysisItem);
            } else if (xAxisDefinition.yaxisMeasure == null) {
                ymeasureGrouping.addAnalysisItem(analysisItem);
            } else if (xAxisDefinition.zaxisMeasure == null) {
                zmeasureGrouping.addAnalysisItem(analysisItem);
            }
        } else if (analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
            dimensionGrouping.addAnalysisItem(analysisItem);
        }
    }

    public function onCustomChangeEvent(event:CustomChangeEvent):void {
    }
}
}