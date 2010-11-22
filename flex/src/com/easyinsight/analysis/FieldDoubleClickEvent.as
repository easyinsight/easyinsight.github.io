package com.easyinsight.analysis {
import flash.events.Event;

public class FieldDoubleClickEvent extends Event {

    public static const FIELD_DOUBLE_CLICK:String = "fieldDoubleClick";

    public var analysisItem:AnalysisItemWrapper;

    public function FieldDoubleClickEvent(analysisItem:AnalysisItemWrapper) {
        super(FIELD_DOUBLE_CLICK, true);
        this.analysisItem = analysisItem;
    }

    override public function clone():Event {
        return new FieldDoubleClickEvent(analysisItem);
    }
}
}