package com.easyinsight.analysis {
import com.easyinsight.filtering.FilterDefinition;

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
}
}