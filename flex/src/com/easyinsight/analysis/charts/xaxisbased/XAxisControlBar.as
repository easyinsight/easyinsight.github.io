package com.easyinsight.analysis.charts.xaxisbased {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItemUpdateEvent;
import com.easyinsight.analysis.CustomChangeEvent;
import com.easyinsight.analysis.DimensionDropArea;
import com.easyinsight.analysis.IReportControlBar;
import com.easyinsight.analysis.ListDropAreaGrouping;
import com.easyinsight.analysis.MeasureDropArea;
import com.easyinsight.analysis.ReportDataEvent;
import mx.collections.ArrayCollection;
import mx.containers.HBox;
public class XAxisControlBar extends HBox implements IReportControlBar  {

    private var xAxisGrouping:ListDropAreaGrouping;
    private var measureGrouping:ListDropAreaGrouping;

    private var xAxisDefinition:XAxisDefinition;

    public function XAxisControlBar() {
        xAxisGrouping = new ListDropAreaGrouping();
        xAxisGrouping.maxElements = 1;
        xAxisGrouping.dropAreaType = DimensionDropArea;
        xAxisGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        measureGrouping = new ListDropAreaGrouping();
        measureGrouping.maxElements = 1;
        measureGrouping.dropAreaType = MeasureDropArea;
        measureGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(xAxisGrouping);
        addChild(measureGrouping);
         if (xAxisDefinition.xaxis != null) {
            xAxisGrouping.addAnalysisItem(xAxisDefinition.xaxis);
        }
        if (xAxisDefinition.measure != null) {
            measureGrouping.addAnalysisItem(xAxisDefinition.measure);
        }
    }

    private function requestListData(event:AnalysisItemUpdateEvent):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    public function set analysisDefinition(analysisDefinition:AnalysisDefinition):void {
        xAxisDefinition = analysisDefinition as XAxisDefinition;
    }

    public function isDataValid():Boolean {
        return (xAxisGrouping.getListColumns().length > 0 && measureGrouping.getListColumns().length > 0);
    }

    public function createAnalysisDefinition():AnalysisDefinition {
        xAxisDefinition.xaxis = xAxisGrouping.getListColumns()[0];
        xAxisDefinition.measure = measureGrouping.getListColumns()[0];
        return xAxisDefinition;
    }

    public function set analysisItems(analysisItems:ArrayCollection):void {
        xAxisGrouping.analysisItems = analysisItems;
        measureGrouping.analysisItems = analysisItems;
    }

    public function addItem(analysisItem:com.easyinsight.analysis.AnalysisItem):void {
    }

    public function onCustomChangeEvent(event:CustomChangeEvent):void {
    }
}
}