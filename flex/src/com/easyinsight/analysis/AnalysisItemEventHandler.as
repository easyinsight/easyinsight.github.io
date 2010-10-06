package com.easyinsight.analysis {
import com.easyinsight.AnalysisItemDeleteEvent;
import com.easyinsight.util.PopUpUtil;
import com.easyinsight.util.UserAudit;

import mx.collections.ArrayCollection;
import mx.core.UIComponent;
import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

[Event(name="addedItemUpdate", type="com.easyinsight.analysis.AddedItemUpdateEvent")]
[Event(name="itemCopy", type="com.easyinsight.analysis.AnalysisItemCopyEvent")]
[Event(name="analysisItemDelete", type="com.easyinsight.AnalysisItemDeleteEvent")]
public class AnalysisItemEventHandler extends UIComponent {

    private var analysisService:RemoteObject;

    private var _analysisItems:ArrayCollection;
    private var _dataSourceID:int;


    public function AnalysisItemEventHandler() {
        super();
        analysisService = new RemoteObject();
        analysisService.destination = "analysisDefinition";
        analysisService.cloneItem.addEventListener(ResultEvent.RESULT, onCopy)
    }

    public function set analysisItems(value:ArrayCollection):void {
        _analysisItems = value;
    }

    public function set dataSourceID(value:int):void {
        _dataSourceID = value;
    }

    public function copyField(event:ReportEditorFieldEvent):void {
        UserAudit.instance().audit(UserAudit.COPIED_FIELD);
        analysisService.cloneItem.send(event.item.analysisItem);
    }

    private function onCopy(event:ResultEvent):void {
        var copyItem:AnalysisItem = analysisService.cloneItem.lastResult as AnalysisItem;
        var node:AnalysisItemNode = new AnalysisItemNode();
        node.analysisItem = copyItem;
        var wrapper:AnalysisItemWrapper = new AnalysisItemWrapper(node);
        dispatchEvent(new AnalysisItemCopyEvent(AnalysisItemCopyEvent.ITEM_COPY, copyItem, wrapper));
        edit(wrapper);
    }

    public function deleteField(event:ReportEditorFieldEvent):void {
        dispatchEvent(new AnalysisItemDeleteEvent(event.item));
    }

    private function edit(analysisItemWrapper:AnalysisItemWrapper):void {
        var editor:Class;
        var analysisItem:AnalysisItem = analysisItemWrapper.analysisItem;
        if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
            editor = HierarchyWindow;
        } else if (analysisItem.hasType(AnalysisItemTypes.CALCULATION)) {
            editor = CalculationMeasureWindow;
        } else if (analysisItem.hasType(AnalysisItemTypes.DERIVED_GROUPING)) {
            editor = DerivedGroupingWindow;
        }
        var analysisItemEditor:AnalysisItemEditWindow = new AnalysisItemEditWindow();
        analysisItemEditor.editorClass = editor;
        analysisItemEditor.originalWrapper = analysisItemWrapper;
        analysisItemEditor.analysisItem = analysisItemWrapper.analysisItem;
        analysisItemEditor.dataSourceID = _dataSourceID;
        analysisItemEditor.analysisItems = this._analysisItems;
        analysisItemEditor.addEventListener(AnalysisItemEditEvent.ANALYSIS_ITEM_EDIT, analysisItemEdited, false, 0, true);
        PopUpManager.addPopUp(analysisItemEditor, this.parent);
        PopUpUtil.centerPopUp(analysisItemEditor);
    }

    public function editField(event:ReportEditorFieldEvent):void {
        var analysisItemWrapper:AnalysisItemWrapper = event.item;
        edit(analysisItemWrapper);
    }

    private function analysisItemEdited(event:AnalysisItemEditEvent):void {
        var analysisItemWrapper:AnalysisItemWrapper = event.previousItemWrapper;
        var existingItem:AnalysisItem = analysisItemWrapper.analysisItem;
        analysisItemWrapper.analysisItem = event.analysisItem;
        analysisItemWrapper.displayName = event.analysisItem.display;
        dispatchEvent(new AddedItemUpdateEvent(AddedItemUpdateEvent.UPDATE, existingItem, analysisItemWrapper, event.analysisItem));
    }
}
}