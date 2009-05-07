package com.easyinsight.analysis.crosstab {
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
import mx.containers.Grid;
import mx.containers.GridItem;
import mx.containers.GridRow;
import mx.containers.HBox;
import mx.controls.Label;
import mx.events.FlexEvent;
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
        var columnGroupingLabel:Label = new Label();
        columnGroupingLabel.text = "Column Grouping:";
        columnGroupingLabel.setStyle("fontSize", 14);
        columnGroupingItem.addChild(columnGroupingLabel);
        columnGroupingItem.addChild(columnGrouping);
        topRow.addChild(emptyGridItem);
        topRow.addChild(columnGroupingItem);
        var bottomRow:GridRow = new GridRow();
        var rowGroupingItem:GridItem = new GridItem();
        var rowGroupingLabel:Label = new Label();
        rowGroupingLabel.text = "Row Grouping:";
        rowGroupingLabel.setStyle("fontSize", 14);
        rowGroupingItem.addChild(rowGroupingLabel);
        rowGroupingItem.addChild(rowGrouping);
        var measureGroupingItem:GridItem = new GridItem();
        var measureLabel:Label = new Label();
        measureLabel.text = "Measure:";
        measureLabel.setStyle("fontSize", 14);
        measureGroupingItem.addChild(measureLabel);
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
        xAxisDefinition = analysisDefinition as CrosstabDefinition;
    }

    public function isDataValid():Boolean {
        return (columnGrouping.getListColumns().length > 0 && measureGrouping.getListColumns().length > 0 &&
                rowGrouping.getListColumns().length > 0);
    }

    public function createAnalysisDefinition():AnalysisDefinition {
        xAxisDefinition.columns = new ArrayCollection(columnGrouping.getListColumns());
        xAxisDefinition.measures = new ArrayCollection(measureGrouping.getListColumns());
        xAxisDefinition.rows = new ArrayCollection(rowGrouping.getListColumns());
        return xAxisDefinition;
    }

    public function set analysisItems(analysisItems:ArrayCollection):void {
        columnGrouping.analysisItems = analysisItems;
        rowGrouping.analysisItems = analysisItems;
        measureGrouping.analysisItems = analysisItems;
    }

    public function addItem(analysisItem:com.easyinsight.analysis.AnalysisItem):void {
        if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
            xAxisDefinition.measures = new ArrayCollection([analysisItem]);
        } else if (analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
            if (xAxisDefinition.columns.length == 0) {
                xAxisDefinition.columns.addItem(analysisItem);
            } else if (xAxisDefinition.rows.length == 0) {
                xAxisDefinition.rows.addItem(analysisItem);
            } else {
                xAxisDefinition.columns = new ArrayCollection([analysisItem]); 
            }
        }
    }

    public function onCustomChangeEvent(event:CustomChangeEvent):void {
    }
}
}