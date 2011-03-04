package com.easyinsight.analysis {
import com.easyinsight.filtering.FilterDefinition;

public class AnalysisUtil {
    public function AnalysisUtil() {
    }

    public static function updateReport(analysisDefinition:AnalysisDefinition, savedDef:AnalysisDefinition):void {
        for each (var item:AnalysisItem in analysisDefinition.getFields()) {
            for each (var savedItem:AnalysisItem in savedDef.getFields()) {
                if (savedItem != null && item != null) {
                    if (item.matches(savedItem)) {
                        item.updateFromSaved(savedItem);
                        break;
                    }
                }
            }
        }
        if (analysisDefinition.filterDefinitions != null) {
            for each (var filter:FilterDefinition in analysisDefinition.filterDefinitions) {
                for each (var savedFilter:FilterDefinition in savedDef.filterDefinitions) {
                    if (filter.matches(savedFilter)) {
                        filter.updateFromSaved(savedFilter);
                    }

                }
            }
        }
        if (analysisDefinition.addedItems != null) {
            for each (var addedItem:AnalysisItem in analysisDefinition.addedItems){
                for each (var savedAddedItem:AnalysisItem in savedDef.addedItems) {
                    if (addedItem.matches(savedAddedItem)) {
                        addedItem.updateFromSaved(savedAddedItem);
                    }
                }
            }
        }
        if (analysisDefinition.joinOverrides != null) {
            for each (var join:JoinOverride in analysisDefinition.joinOverrides) {
                for each (var savedJoin:JoinOverride in savedDef.joinOverrides) {
                    if (join.matches(savedJoin)) {
                        join.updateFromSaved(savedJoin);
                    }
                }
            }
        }
        analysisDefinition.fromSave(savedDef);

    }
}
}