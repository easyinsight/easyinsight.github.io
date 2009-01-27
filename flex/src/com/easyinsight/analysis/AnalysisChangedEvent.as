package com.easyinsight.analysis {
import flash.events.Event;
public class AnalysisChangedEvent extends Event {
    public static const ANALYSIS_CHANGED:String = "analysisChanged";

    public var metadata:Boolean = true;

    public function AnalysisChangedEvent(metadata:Boolean = true) {
        super(ANALYSIS_CHANGED, true);
        this.metadata = metadata;
    }


    override public function clone():Event {
        return new AnalysisChangedEvent();
    }
}
}