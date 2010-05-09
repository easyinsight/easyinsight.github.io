package com.easyinsight.analysis {
import com.easyinsight.filtering.FilterDefinition;
import com.easyinsight.util.PopUpUtil;

import mx.collections.ArrayCollection;
import mx.core.UIComponent;
import mx.managers.PopUpManager;

public class AnalysisUtil {
    public function AnalysisUtil() {
    }

    public static function updateReport(analysisDefinition:AnalysisDefinition, savedDef:AnalysisDefinition):void {
        for each (var item:AnalysisItem in analysisDefinition.getFields()) {
            if (item.analysisItemID > 0) {
                continue;
            }
            for each (var savedItem:AnalysisItem in savedDef.getFields()) {
                if (savedItem.qualifiedName() == item.qualifiedName()) {
                    item.analysisItemID = savedItem.analysisItemID;
                    break;
                }
            }
        }
        if (analysisDefinition.filterDefinitions != null) {
            for each (var filter:FilterDefinition in analysisDefinition.filterDefinitions) {
                for each (var savedFilter:FilterDefinition in savedDef.filterDefinitions) {
                    if (savedFilter.field.qualifiedName() == filter.field.qualifiedName()) {
                        filter.filterID = savedFilter.filterID;
                    }
                }
            }
        }
        if (analysisDefinition.addedItems != null) {
            for each (var addedItem:AnalysisItem in analysisDefinition.addedItems){
                for each (var savedAddedItem:AnalysisItem in savedDef.addedItems) {
                    if (savedAddedItem.qualifiedName() == addedItem.qualifiedName()) {
                        addedItem.analysisItemID = savedAddedItem.analysisItemID;
                    }
                }
            }
        }
        analysisDefinition.analysisID = savedDef.analysisID;
    }

    /*public static function editAnalysisItem(parent:UIComponent, analysisItem:AnalysisItem, dataSourceID:int,
                                            analysisItems:ArrayCollection):void {
        var editor:Class;
        if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
            editor = HierarchyWindow;
        } else if (analysisItem.hasType(AnalysisItemTypes.CALCULATION)) {
            editor = CalculationWindow;
        } else {
            editor = AnalysisItemEditor;
        }
        var analysisItemEditor:AnalysisItemEditWindow = new AnalysisItemEditWindow();
        analysisItemEditor.editorClass = editor;
        analysisItemEditor.analysisItem = analysisItem;
        analysisItemEditor.dataSourceID = dataSourceID;
        analysisItemEditor.analysisItems = analysisItems;
        analysisItemEditor.addEventListener(AnalysisItemEditEvent.ANALYSIS_ITEM_EDIT, analysisItemEdited, false, 0, true);
        PopUpManager.addPopUp(analysisItemEditor, parent);
        PopUpUtil.centerPopUp(analysisItemEditor);
    }*/
}
}