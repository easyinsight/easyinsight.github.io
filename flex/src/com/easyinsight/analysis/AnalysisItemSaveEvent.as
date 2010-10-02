package com.easyinsight.analysis {
import flash.events.Event;

public class AnalysisItemSaveEvent extends Event{

    public static const ANALYSIS_ITEM_SAVE:String = "analysisItemSave";

    public var analysisItem:AnalysisItem;
    public var previousWrapper:AnalysisItemWrapper;

    public function AnalysisItemSaveEvent(item:AnalysisItem) {
        analysisItem = item;
        super(ANALYSIS_ITEM_SAVE, true);
    }
}
}