package com.easyinsight.analysis {
import flash.events.Event;

public class AnalysisItemCopyEvent extends Event {

    public static const ITEM_COPY:String = "itemCopy";

    public var analysisItem:AnalysisItem;

    public function AnalysisItemCopyEvent(analysisItem:AnalysisItem) {
        super(ITEM_COPY, true);
        this.analysisItem = analysisItem;
    }

    override public function clone():Event {
        return new AnalysisItemCopyEvent(analysisItem);
    }
}
}