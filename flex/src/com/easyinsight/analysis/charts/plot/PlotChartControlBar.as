package com.easyinsight.analysis.charts.plot {
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
import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.Label;
import mx.events.FlexEvent;
public class PlotChartControlBar extends HBox implements IReportControlBar  {

    private var dimensionGrouping:ListDropAreaGrouping;
    private var seriesGrouping:ListDropAreaGrouping;
    private var xmeasureGrouping:ListDropAreaGrouping;
    private var ymeasureGrouping:ListDropAreaGrouping;

    private var xAxisDefinition:PlotChartDefinition;

    public function PlotChartControlBar() {
        dimensionGrouping = new ListDropAreaGrouping();
        dimensionGrouping.maxElements = 1;
        dimensionGrouping.dropAreaType = DimensionDropArea;
        dimensionGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        seriesGrouping = new ListDropAreaGrouping();
        seriesGrouping.maxElements = 1;
        seriesGrouping.dropAreaType = DimensionDropArea;
        seriesGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        xmeasureGrouping = new ListDropAreaGrouping();
        xmeasureGrouping.maxElements = 1;
        xmeasureGrouping.dropAreaType = MeasureDropArea;
        xmeasureGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        ymeasureGrouping = new ListDropAreaGrouping();
        ymeasureGrouping.maxElements = 1;
        ymeasureGrouping.dropAreaType = MeasureDropArea;
        ymeasureGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        setStyle("verticalAlign", "middle");
    }

    override protected function createChildren():void {
        super.createChildren();
        var groupingLabel:Label = new Label();
        groupingLabel.text = "Grouping:";
        groupingLabel.setStyle("fontSize", 14);
        addChild(groupingLabel);
        addChild(dimensionGrouping);
        var pointTypeLabel:Label = new Label();
        pointTypeLabel.text = "Point Type:";
        pointTypeLabel.setStyle("fontSize", 14);
        addChild(pointTypeLabel);
        addChild(seriesGrouping);
        var yMeasureLabel:Label = new Label();
        yMeasureLabel.text = "Y Axis Measure:";
        yMeasureLabel.setStyle("fontSize", 14);
        addChild(yMeasureLabel);
        addChild(ymeasureGrouping);
        var xMeasureLabel:Label = new Label();
        xMeasureLabel.text = "X Axis Measure:";
        xMeasureLabel.setStyle("fontSize", 14);
        addChild(xMeasureLabel);
        addChild(xmeasureGrouping);
        if (xAxisDefinition.dimension != null) {
            dimensionGrouping.addAnalysisItem(xAxisDefinition.dimension);
        }
        if (xAxisDefinition.iconGrouping != null) {
            seriesGrouping.addAnalysisItem(xAxisDefinition.iconGrouping);
        }
        if (xAxisDefinition.xaxisMeasure != null) {
            xmeasureGrouping.addAnalysisItem(xAxisDefinition.xaxisMeasure);
        }
        if (xAxisDefinition.yaxisMeasure != null) {
            ymeasureGrouping.addAnalysisItem(xAxisDefinition.yaxisMeasure);
        }
        var limitLabel:Label = new Label();
        BindingUtils.bindProperty(limitLabel, "text", this, "limitText");
        addChild(limitLabel);
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
        xAxisDefinition = analysisDefinition as PlotChartDefinition;
    }

    public function isDataValid():Boolean {
        return (dimensionGrouping.getListColumns().length > 0 && xmeasureGrouping.getListColumns().length > 0
                && ymeasureGrouping.getListColumns().length > 0);
    }

    public function createAnalysisDefinition():AnalysisDefinition {
        xAxisDefinition.dimension = dimensionGrouping.getListColumns()[0];
        xAxisDefinition.xaxisMeasure = xmeasureGrouping.getListColumns()[0];
        xAxisDefinition.yaxisMeasure = ymeasureGrouping.getListColumns()[0];
        xAxisDefinition.iconGrouping = seriesGrouping.getListColumns()[0];
        return xAxisDefinition;
    }

    public function set analysisItems(analysisItems:ArrayCollection):void {
        xmeasureGrouping.analysisItems = analysisItems;
        dimensionGrouping.analysisItems = analysisItems;
        ymeasureGrouping.analysisItems = analysisItems;
        seriesGrouping.analysisItems = analysisItems;
    }

    public function addItem(analysisItem:com.easyinsight.analysis.AnalysisItem):void {
        if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
            if (xAxisDefinition.xaxisMeasure == null || xAxisDefinition.yaxisMeasure != null) {
                xmeasureGrouping.addAnalysisItem(analysisItem);
            } else if (xAxisDefinition.yaxisMeasure == null) {
                ymeasureGrouping.addAnalysisItem(analysisItem);
            }
        } else if (analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
            dimensionGrouping.addAnalysisItem(analysisItem);
        }
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    public function onCustomChangeEvent(event:CustomChangeEvent):void {
    }
}
}