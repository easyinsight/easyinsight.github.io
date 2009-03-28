package com.easyinsight.analysis.list {

import com.easyinsight.analysis.AnalysisChangedEvent;
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemUpdateEvent;
import com.easyinsight.analysis.CustomChangeEvent;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.IReportControlBar;
import com.easyinsight.analysis.ListDropArea;
import com.easyinsight.analysis.ListDropAreaGrouping;
import com.easyinsight.analysis.ReportDataEvent;
import flash.events.MouseEvent;
import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.Button;
import mx.controls.Label;
import mx.events.FlexEvent;
import mx.managers.PopUpManager;

public class ListControlBar extends HBox implements IReportControlBar {

    private var listViewGrouping:ListDropAreaGrouping;
    private var listDefinition:ListDefinition;
    private var availableFields:ArrayCollection;

    [Embed(source="../../../../../assets/table_edit.png")]
    public var tableEditIcon:Class;

    [Embed(source="../../../../../assets/find.png")]
    public var findIcon:Class;

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
        var listEditButton:Button = new Button();
        listEditButton.setStyle("icon", tableEditIcon);
        listEditButton.toolTip = "Edit List Properties...";
        listEditButton.addEventListener(MouseEvent.CLICK, editList);
        addChild(listEditButton);
        var findButton:Button = new Button();
        findButton.setStyle("icon", findIcon);
        findButton.toolTip = "Search Keyword...";
        findButton.addEventListener(MouseEvent.CLICK, startFind);
        addChild(findButton);
        addChild(listViewGrouping);
        var columns:ArrayCollection = listDefinition.columns;
        if (columns != null) {
            for (var i:int = 0; i < columns.length; i++) {
                var column:AnalysisItem = columns.getItemAt(i) as AnalysisItem;
                listViewGrouping.addAnalysisItem(column);
            }
        }
        var limitLabel:Label = new Label();
        BindingUtils.bindProperty(limitLabel, "text", this, "limitText");
        addChild(limitLabel);
    }

    private function startFind(event:MouseEvent):void {
        dispatchEvent(new EnableKeywordEvent());
    }

    private function editList(event:MouseEvent):void {
        var listWindow:ListDefinitionEditWindow = ListDefinitionEditWindow(PopUpManager.createPopUp(this, ListDefinitionEditWindow, true));
        listWindow.fields = availableFields;
        listWindow.listDefinition = listDefinition;
        listWindow.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        PopUpManager.centerPopUp(listWindow);
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

    public function set analysisItems(analysisItems:ArrayCollection):void {
        availableFields = analysisItems;
        listViewGrouping.analysisItems = analysisItems;
    }

    public function addItem(analysisItem:com.easyinsight.analysis.AnalysisItem):void {
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