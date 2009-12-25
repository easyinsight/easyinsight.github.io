package com.easyinsight {
import com.easyinsight.analysis.AnalysisItemWrapper;

import flash.events.Event;

public class AnalysisItemDeleteEvent extends Event {

    public static const ANALYSIS_ITEM_DELETE:String = "analysisItemDelete";

    public var analysisItem:AnalysisItemWrapper;

    public function AnalysisItemDeleteEvent(analysisItem:AnalysisItemWrapper) {
        super(ANALYSIS_ITEM_DELETE, true);
        this.analysisItem = analysisItem;
    }

    override public function clone():Event {
        return new AnalysisItemDeleteEvent(analysisItem);
    }
}
}