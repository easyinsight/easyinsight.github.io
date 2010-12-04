package com.easyinsight.analysis.form {
import com.easyinsight.analysis.AnalysisChangedEvent;
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemUpdateEvent;
import com.easyinsight.analysis.CustomChangeEvent;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.GenericDefinitionEditWindow;
import com.easyinsight.analysis.IReportControlBar;
import com.easyinsight.analysis.ListDropArea;
import com.easyinsight.analysis.ListDropAreaGrouping;
import com.easyinsight.analysis.ReportControlBar;
import com.easyinsight.analysis.ReportDataEvent;
import com.easyinsight.analysis.list.ColumnReorderEvent;

import com.easyinsight.util.PopUpUtil;

import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.controls.Button;
import mx.managers.PopUpManager;

public class FormControlBar extends ReportControlBar implements IReportControlBar {

    private var listViewGrouping:ListDropAreaGrouping;
    private var listDefinition:FormReport;

    [Embed(source="../../../../../assets/table_edit.png")]
    public var tableEditIcon:Class;

    [Embed(source="../../../../../assets/find.png")]
    public var findIcon:Class;

    public function FormControlBar() {
        super();
        listViewGrouping = new ListDropAreaGrouping();
        listViewGrouping.unlimited = true;
        listViewGrouping.dropAreaType = ListDropArea;
        listViewGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        setStyle("verticalAlign", "middle");
    }

    private function editList(event:MouseEvent):void {
        var listWindow:GenericDefinitionEditWindow = new GenericDefinitionEditWindow();
        listWindow.definition = listDefinition;
        listWindow.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        PopUpManager.addPopUp(listWindow, this, true);
        PopUpUtil.centerPopUp(listWindow);
    }

    override protected function createChildren():void {
        super.createChildren();
        var listEditButton:Button = new Button();
        listEditButton.setStyle("icon", tableEditIcon);
        listEditButton.toolTip = "Edit Form Properties...";
        listEditButton.addEventListener(MouseEvent.CLICK, editList);
        addChild(listEditButton);
        /*var findButton:Button = new Button();
        findButton.setStyle("icon", findIcon);
        findButton.toolTip = "Search Keyword...";
        findButton.addEventListener(MouseEvent.CLICK, startFind);
        addChild(findButton);*/
        addDropAreaGrouping(listViewGrouping);
        var columns:ArrayCollection = listDefinition.columns;
        if (columns != null) {
            for (var i:int = 0; i < columns.length; i++) {
                var column:AnalysisItem = columns.getItemAt(i) as AnalysisItem;
                listViewGrouping.addAnalysisItem(column);
            }
        }
        /*var limitLabel:LinkButton = new LinkButton();
        limitLabel.setStyle("textDecoration", "underline");
        limitLabel.addEventListener(MouseEvent.CLICK, editList);
        BindingUtils.bindProperty(limitLabel, "label", this, "limitText");
        addChild(limitLabel);*/
    }

    /*private function editList(event:MouseEvent):void {
        var listWindow:ListDefinitionEditWindow = ListDefinitionEditWindow(PopUpManager.createPopUp(this, ListDefinitionEditWindow, true));
        listWindow.fields = analysisItems;
        listWindow.listDefinition = listDefinition;
        listWindow.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        PopUpUtil.centerPopUp(listWindow);
    }*/

    public function onDataReceipt(event:DataServiceEvent):void {
    }

    private function requestListData(event:AnalysisItemUpdateEvent):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    public function set analysisDefinition(analysisDefinition:AnalysisDefinition):void {
        listDefinition = analysisDefinition as FormReport;
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