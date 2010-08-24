package com.easyinsight.analysis {
import flash.events.Event;
public class HierarchyRollupEvent extends Event{

    public static const HIERARCHY_ROLLUP:String = "hierarchyRollup";

    public var analysisItem:AnalysisItem;
    public var hierarchy:AnalysisItem;
    public var position:int;

    public function HierarchyRollupEvent(analysisItem:AnalysisItem, hierarchy:AnalysisItem, position:int) {
        super(HIERARCHY_ROLLUP, true, true);
        this.analysisItem = analysisItem;
        this.hierarchy = hierarchy;
        this.position = position;
    }

    override public function clone():Event {
        return new HierarchyRollupEvent(analysisItem, hierarchy, position);
    }
}
}