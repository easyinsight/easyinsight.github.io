package com.easyinsight.analysis {
import com.easyinsight.filtering.FilterDefinition;

public class AnalysisUtil {
    public function AnalysisUtil() {
    }

    public static function updateReport(analysisDefinition:AnalysisDefinition, savedDef:AnalysisDefinition):void {
        for each (var item:AnalysisItem in analysisDefinition.getFields()) {
            if (item != null && item.analysisItemID > 0) {
                continue;
            }
            for each (var savedItem:AnalysisItem in savedDef.getFields()) {
                if (savedItem != null && item != null) {
                    if (item.analysisItemID == 0) {
                        if (savedItem.qualifiedName() == item.qualifiedName() &&
                                savedItem.getType() == item.getType()) {
                            item.analysisItemID = savedItem.analysisItemID;
                            if (item.filters != null) {
                                for each (var itemFilter:FilterDefinition in item.filters) {
                                    if (itemFilter.filterID == 0) {
                                        for each (var savedItemFilter:FilterDefinition in savedItem.filters) {
                                            if (savedItemFilter.field.qualifiedName() == itemFilter.field.qualifiedName() &&
                                                    savedItemFilter.getType() == itemFilter.getType()) {
                                                itemFilter.filterID = savedItemFilter.filterID;
                                                itemFilter.field.analysisItemID = savedItemFilter.field.analysisItemID;
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        if (analysisDefinition.filterDefinitions != null) {
            for each (var filter:FilterDefinition in analysisDefinition.filterDefinitions) {
                if (filter.filterID == 0) {
                    for each (var savedFilter:FilterDefinition in savedDef.filterDefinitions) {
                        if (savedFilter.field != null) {
                            if (savedFilter.field.qualifiedName() == filter.field.qualifiedName() &&
                                    savedFilter.getType() == filter.getType()) {
                                filter.filterID = savedFilter.filterID;
                                filter.field.analysisItemID = savedFilter.field.analysisItemID;
                            }
                        }
                    }
                }
            }
        }
        if (analysisDefinition.addedItems != null) {
            for each (var addedItem:AnalysisItem in analysisDefinition.addedItems){
                for each (var savedAddedItem:AnalysisItem in savedDef.addedItems) {
                    if (addedItem.analysisItemID == 0) {
                        if (savedAddedItem.qualifiedName() == addedItem.qualifiedName() &&
                                savedAddedItem.getType() == addedItem.getType()) {
                            addedItem.analysisItemID = savedAddedItem.analysisItemID;
                        }
                    }
                }
            }
        }
        analysisDefinition.analysisID = savedDef.analysisID;
    }
}
}