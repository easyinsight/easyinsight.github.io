package com.easyinsight.analysis.list {

import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemUpdateEvent;
import com.easyinsight.analysis.IReportControlBar;
import com.easyinsight.analysis.ListDropArea;
import com.easyinsight.analysis.ListDropAreaGrouping;
import com.easyinsight.analysis.ReportDataEvent;
import mx.collections.ArrayCollection;
import mx.containers.HBox;

public class ListControlBar extends HBox implements IReportControlBar {

    private var listViewGrouping:ListDropAreaGrouping;
    private var listDefinition:ListDefinition;

    public function ListControlBar() {
        super();
        listViewGrouping = new ListDropAreaGrouping();
        listViewGrouping.unlimited = true;
        listViewGrouping.dropAreaType = ListDropArea;
        listViewGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(listViewGrouping);
        var columns:ArrayCollection = listDefinition.columns;
        if (columns != null) {
            for (var i:int = 0; i < columns.length; i++) {
                var column:AnalysisItem = columns.getItemAt(i) as AnalysisItem;
                listViewGrouping.addAnalysisItem(column);
            }
        }
    }

    private function requestListData(event:AnalysisItemUpdateEvent):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    public function set analysisDefinition(analysisDefinition:AnalysisDefinition):void {
        listDefinition = analysisDefinition as ListDefinition;
    }

    public function isDataValid():Boolean {
        return (listViewGrouping.getListColumns().length > 0);
    }

    public function createAnalysisDefinition():AnalysisDefinition {
        listDefinition.columns = new ArrayCollection(listViewGrouping.getListColumns());
        return listDefinition;
    }

    public function set analysisItems(analysisItems:ArrayCollection):void {
        listViewGrouping.analysisItems = analysisItems;
    }

    public function addItem(analysisItem:com.easyinsight.analysis.AnalysisItem):void {
        listViewGrouping.addAnalysisItem(analysisItem);
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }
}
}