package com.easyinsight.analysis {
import flash.events.Event;
public class HierarchyRollupEvent extends Event{

    public static const HIERARCHY_ROLLUP:String = "hierarchyRollup";

    public var analysisItem:AnalysisItem;

    public function HierarchyRollupEvent(analysisItem:AnalysisItem) {
        super(HIERARCHY_ROLLUP, true, true);
        this.analysisItem = analysisItem;
    }

    override public function clone():Event {
        return new HierarchyRollupEvent(analysisItem);
    }
}
}