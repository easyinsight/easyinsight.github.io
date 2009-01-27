package com.easyinsight.customupload.api {
import com.easyinsight.analysis.AnalysisItem;
import flash.events.Event;
public class MethodItemEvent extends Event{
    public static const METHOD_ITEM_DELETED:String = "methodItemDeleted";

    public var analysisItem:AnalysisItem;

    public function MethodItemEvent(type:String, analysisItem:AnalysisItem) {
        super(type, true);
        this.analysisItem = analysisItem;
    }


    override public function clone():Event {
        return new MethodItemEvent(type, analysisItem);
    }
}
}