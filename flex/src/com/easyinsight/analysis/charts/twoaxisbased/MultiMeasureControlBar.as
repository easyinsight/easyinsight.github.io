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
import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.Label;
import mx.events.FlexEvent;
public class MultiMeasureControlBar extends ReportControlBar implements IReportControlBar {
    private var xAxisGrouping:ListDropAreaGrouping;
    private var measureGrouping:ListDropAreaGrouping;

    private var xAxisDefinition:MultiMeasureDefinition;

    public function MultiMeasureControlBar() {
        xAxisGrouping = new ListDropAreaGrouping();
        xAxisGrouping.maxElements = 1;
        xAxisGrouping.dropAreaType = DimensionDropArea;
        xAxisGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        measureGrouping = new ListDropAreaGrouping();
        measureGrouping.unlimited = true;
        measureGrouping.dropAreaType = MeasureDropArea;
        measureGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        setStyle("verticalAlign", "middle");
    }

    override protected function createChildren():void {
        super.createChildren();
        var xAxisGroupingLabel:Label = new Label();
        xAxisGroupingLabel.text = "X Axis Grouping:";
        xAxisGroupingLabel.setStyle("fontSize", 14);
        addChild(xAxisGroupingLabel);
        addDropAreaGrouping(xAxisGrouping);
        var measureGroupingLabel:Label = new Label();
        measureGroupingLabel.text = "Measures:";
        measureGroupingLabel.setStyle("fontSize", 14);
        addChild(measureGroupingLabel);
        addDropAreaGrouping(measureGrouping);
         if (xAxisDefinition.xaxis != null) {
            xAxisGrouping.addAnalysisItem(xAxisDefinition.xaxis);
        }
        if (xAxisDefinition.measures != null) {
            for each (var analysisItem:AnalysisItem in xAxisDefinition.measures) {
                measureGrouping.addAnalysisItem(analysisItem);
            }
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
        xAxisDefinition = analysisDefinition as MultiMeasureDefinition;
    }

    public function isDataValid():Boolean {
        return (xAxisGrouping.getListColumns().length > 0 && measureGrouping.getListColumns().length > 0);
    }

    public function createAnalysisDefinition():AnalysisDefinition {
        xAxisDefinition.xaxis = xAxisGrouping.getListColumns()[0];
        xAxisDefinition.measures = new ArrayCollection(measureGrouping.getListColumns());
        return xAxisDefinition;
    }

    public function addItem(analysisItem:com.easyinsight.analysis.AnalysisItem):void {
        if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
            if (!xAxisDefinition.measures.contains(analysisItem)) {
                measureGrouping.addAnalysisItem(analysisItem);
            }
        } else if (analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
            xAxisGrouping.addAnalysisItem(analysisItem);
        }
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    public function onCustomChangeEvent(event:CustomChangeEvent):void {
    }
}
}