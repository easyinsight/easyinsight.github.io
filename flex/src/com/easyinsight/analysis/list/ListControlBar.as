package com.easyinsight.analysis.list {

import com.easyinsight.analysis.AnalysisChangedEvent;
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemUpdateEvent;
import com.easyinsight.analysis.CustomChangeEvent;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.FeedMetadata;
import com.easyinsight.analysis.IReportControlBar;
import com.easyinsight.analysis.ListDropArea;
import com.easyinsight.analysis.ListDropAreaGrouping;
import com.easyinsight.analysis.ReportControlBar;
import com.easyinsight.analysis.ReportDataEvent;
import com.easyinsight.analysis.ReportPropertiesEvent;

import flash.events.MouseEvent;
import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.controls.Button;

import mx.controls.LinkButton;
import mx.events.FlexEvent;

public class ListControlBar extends ReportControlBar implements IReportControlBar {

    private var listViewGrouping:ListDropAreaGrouping;
    private var listDefinition:ListDefinition;

    [Embed(source="../../../../../assets/find.png")]
    public var findIcon:Class;

    [Embed(source="../../../../../assets/tables_x16.png")]
    public var lookupTableIcon:Class;

    public function ListControlBar() {
        super();
        listViewGrouping = new ListDropAreaGrouping();
        listViewGrouping.unlimited = true;
        listViewGrouping.dropAreaType = ListDropArea;
        listViewGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        setStyle("verticalAlign", "middle");
    }

    override protected function createChildren():void {
        super.createChildren();
        if (_feedMetadata.lookupTables.length > 0) {
            var lookupTableButton:Button = new Button();
            lookupTableButton.setStyle("icon", lookupTableIcon);
            lookupTableButton.toolTip = "Edit Lookup Tables";
            lookupTableButton.addEventListener(MouseEvent.CLICK, editLookupTables);
            addChild(lookupTableButton);
        }
        var findButton:Button = new Button();
        findButton.setStyle("icon", findIcon);
        findButton.toolTip = "Search Keyword...";
        findButton.addEventListener(MouseEvent.CLICK, startFind);
        addChild(findButton);
        listViewGrouping.report = listDefinition;
        addDropAreaGrouping(listViewGrouping);
        var columns:ArrayCollection = listDefinition.columns;
        if (columns != null) {
            for (var i:int = 0; i < columns.length; i++) {
                var column:AnalysisItem = columns.getItemAt(i) as AnalysisItem;
                listViewGrouping.addAnalysisItem(column);
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

    private function startFind(event:MouseEvent):void {
        dispatchEvent(new EnableKeywordEvent());
    }

    private var _feedMetadata:FeedMetadata;

    public function set feedMetadata(value:FeedMetadata):void {
        _feedMetadata = value;
    }

    private function editLookupTables(event:MouseEvent):void {
        dispatchEvent(new EnableLookupEditingEvent());
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA, false));
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
        listDefinition = analysisDefinition as ListDefinition;
    }

    public function isDataValid():Boolean {
        return (listViewGrouping.getListColumns().length > 0);
    }

    public function createAnalysisDefinition():AnalysisDefinition {
        listDefinition.columns = new ArrayCollection(listViewGrouping.getListColumns());
        return listDefinition;
    }

    public function addItem(analysisItem:AnalysisItem):void {
        listViewGrouping.addAnalysisItem(analysisItem);
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
        dispatchEvent(new AnalysisChangedEvent(false));
    }

    public function onCustomChangeEvent(event:CustomChangeEvent):void {
        if (event is ColumnReorderEvent) {
            var columnReorderEvent:ColumnReorderEvent = event as ColumnReorderEvent;
            listViewGrouping.reorder(columnReorderEvent.columns);
        }
    }
}
}