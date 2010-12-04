package com.easyinsight.analysis {
import flash.events.Event;

public class AnalysisItemChangeEvent extends Event {

    public static const ANALYSIS_ITEM_CHANGE:String = "analysisItemChange";

    public var item:AnalysisItem;

    public function AnalysisItemChangeEvent(item:AnalysisItem) {
        super(ANALYSIS_ITEM_CHANGE, true);
        this.item = item;
    }

    override public function clone():Event {
        return new AnalysisItemChangeEvent(item);
    }
}
}