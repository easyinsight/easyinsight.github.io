package com.easyinsight.analysis {
import flash.events.Event;
public class ReportRendererEvent extends Event{

    public static const ADD_ITEM:String = "addItem";
    public static const REMOVE_ITEM:String = "removeItem";
    public static const FORCE_RENDER:String = "forceRender";

    public var analysisItem:AnalysisItem;

    public function ReportRendererEvent(type:String, analysisItem:AnalysisItem = null) {
        super(type);
        this.analysisItem = analysisItem;
    }

    override public function clone():Event {
        return new ReportRendererEvent(type, analysisItem);
    }
}
}