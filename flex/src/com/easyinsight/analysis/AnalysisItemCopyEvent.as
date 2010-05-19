package com.easyinsight.analysis {
import flash.events.Event;

public class AnalysisItemCopyEvent extends Event {

    public static const ITEM_COPY:String = "itemCopy";

    public var analysisItem:AnalysisItem;
    public var wrapper:AnalysisItemWrapper;

    public function AnalysisItemCopyEvent(analysisItem:AnalysisItem, wrapper:AnalysisItemWrapper = null) {
        super(ITEM_COPY, true);
        this.analysisItem = analysisItem;
        this.wrapper = wrapper;
    }

    override public function clone():Event {
        return new AnalysisItemCopyEvent(analysisItem);
    }
}
}