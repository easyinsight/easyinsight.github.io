package com.easyinsight.analysis.crosstab {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemUpdateEvent;
import com.easyinsight.analysis.CustomChangeEvent;
import com.easyinsight.analysis.DimensionDropArea;
import com.easyinsight.analysis.IReportControlBar;
import com.easyinsight.analysis.ListDropAreaGrouping;
import com.easyinsight.analysis.MeasureDropArea;
import com.easyinsight.analysis.ReportDataEvent;
import mx.collections.ArrayCollection;
import mx.containers.Grid;
import mx.containers.GridItem;
import mx.containers.GridRow;
import mx.containers.HBox;
public class CrosstabControlBar extends HBox implements IReportControlBar {
    private var rowGrouping:ListDropAreaGrouping;
    private var columnGrouping:ListDropAreaGrouping;
    private var measureGrouping:ListDropAreaGrouping;

    private var xAxisDefinition:CrosstabDefinition;

    public function CrosstabControlBar() {
        rowGrouping = new ListDropAreaGrouping();
        rowGrouping.maxElements = 1;
        rowGrouping.dropAreaType = DimensionDropArea;
        rowGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        columnGrouping = new ListDropAreaGrouping();
        columnGrouping.maxElements = 1;
        columnGrouping.dropAreaType = DimensionDropArea;
        columnGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        measureGrouping = new ListDropAreaGrouping();
        measureGrouping.maxElements = 1;
        measureGrouping.dropAreaType = MeasureDropArea;
        measureGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
    }

    override protected function createChildren():void {
        super.createChildren();
        var grid:Grid = new Grid();
        var topRow:GridRow = new GridRow();
        var emptyGridItem:GridItem = new GridItem();
        var columnGroupingItem:GridItem = new GridItem();
        columnGroupingItem.addChild(columnGrouping);
        topRow.addChild(emptyGridItem);
        topRow.addChild(columnGroupingItem);
        var bottomRow:GridRow = new GridRow();
        var rowGroupingItem:GridItem = new GridItem();
        rowGroupingItem.addChild(rowGrouping);
        var measureGroupingItem:GridItem = new GridItem();
        measureGroupingItem.addChild(measureGrouping);
        bottomRow.addChild(rowGroupingItem);
        bottomRow.addChild(measureGroupingItem);
        grid.addChild(topRow);
        grid.addChild(bottomRow);
        addChild(grid);
        if (xAxisDefinition.columns != null) {
            for each (var column:AnalysisItem in xAxisDefinition.columns) {
                columnGrouping.addAnalysisItem(column);
            }
        }
        if (xAxisDefinition.rows != null) {
            for each (var row:AnalysisItem in xAxisDefinition.rows) {
                rowGrouping.addAnalysisItem(row);
            }
        }
        if (xAxisDefinition.measures != null) {
            for each (var measure:AnalysisItem in xAxisDefinition.measures) {
                measureGrouping.addAnalysisItem(measure);
            }
        }
    }

    private function requestListData(event:AnalysisItemUpdateEvent):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    public function set analysisDefinition(analysisDefinition:AnalysisDefinition):void {
        xAxisDefinition = analysisDefinition as CrosstabDefinition;
    }

    public function isDataValid():Boolean {
        return (columnGrouping.getListColumns().length > 0 && measureGrouping.getListColumns().length > 0 &&
                rowGrouping.getListColumns().length > 0);
    }

    public function createAnalysisDefinition():AnalysisDefinition {
        xAxisDefinition.columns = columnGrouping.getListColumns();
        xAxisDefinition.measures = measureGrouping.getListColumns();
        xAxisDefinition.rows = rowGrouping.getListColumns();
        return xAxisDefinition;
    }

    public function set analysisItems(analysisItems:ArrayCollection):void {
        columnGrouping.analysisItems = analysisItems;
        rowGrouping.analysisItems = analysisItems;
        measureGrouping.analysisItems = analysisItems;
    }

    public function addItem(analysisItem:com.easyinsight.analysis.AnalysisItem):void {
    }

    public function onCustomChangeEvent(event:CustomChangeEvent):void {
    }
}
}