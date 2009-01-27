package com.easyinsight.administration.feed {
import com.easyinsight.analysis.AnalysisItemWrapper;
import flash.events.Event;
public class DeleteAnalysisItemEvent extends Event {
    public static const DELETE_ANALYSIS_ITEM:String = "deleteAnalysisItem";

    public var analysisItemWrapper:AnalysisItemWrapper;


    public function DeleteAnalysisItemEvent(analysisItemWrapper:AnalysisItemWrapper) {
        super(DELETE_ANALYSIS_ITEM, true);
        this.analysisItemWrapper = analysisItemWrapper;
    }


    override public function clone():Event {
        return new DeleteAnalysisItemEvent(analysisItemWrapper);
    }
}
}