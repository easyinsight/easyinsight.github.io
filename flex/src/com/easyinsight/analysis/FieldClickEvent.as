package com.easyinsight.analysis {
import flash.events.Event;

public class FieldClickEvent extends Event {

    public static const FIELD_CLICK:String = "fieldClick";

    public var analysisItem:AnalysisItemWrapper;

    public function FieldClickEvent(analysisItem:AnalysisItemWrapper) {
        super(FIELD_CLICK, true);
        this.analysisItem = analysisItem;
    }

    override public function clone():Event {
        return new FieldDoubleClickEvent(analysisItem);
    }
}
}