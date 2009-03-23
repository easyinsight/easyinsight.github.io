package com.easyinsight.analysis.charts.yaxisbased {
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
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(yAxisGrouping);
        addChild(measureGrouping);
         if (yAxisDefinition.yaxis != null) {
            yAxisGrouping.addAnalysisItem(yAxisDefinition.yaxis);
        }
        if (yAxisDefinition.measure != null) {
            measureGrouping.addAnalysisItem(yAxisDefinition.measure);
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

    public function addItem(analysisItem:com.easyinsight.analysis.AnalysisItem):void {
    }

    public function onCustomChangeEvent(event:CustomChangeEvent):void {
    }
}
}