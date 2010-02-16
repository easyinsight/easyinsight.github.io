package com.easyinsight.analysis {
import flash.events.Event;

public class AnalysisItemValidationEvent extends Event {

    public static const ANALYSIS_ITEM_VALIDATION:String = "analysisItemValidation";

    public var successful:Boolean;

    public function AnalysisItemValidationEvent(successful:Boolean) {
        super(ANALYSIS_ITEM_VALIDATION);
        this.successful = successful;
    }

    override public function clone():Event {
        return new AnalysisItemValidationEvent(successful);
    }
}
}