package com.easyinsight.administration.feed {
import com.easyinsight.analysis.AnalysisHierarchyItem;
import flash.events.Event;
public class HierarchyUpdateEvent extends Event {
    public static const NEW_HIERARCHY:String = "newHierarchy";
    public static const UPDATE_HIERARCHY:String = "updateHierarchy";
    public static const DELETE_HIERARCHY:String = "deleteHierarchy";

    public var analysisHierarchyItem:AnalysisHierarchyItem;


    public function HierarchyUpdateEvent(type:String, analysisHierarchyItem:AnalysisHierarchyItem) {
        super(type);
        this.analysisHierarchyItem = analysisHierarchyItem;
    }


    override public function clone():Event {
        return new HierarchyUpdateEvent(type, analysisHierarchyItem);
    }
}
}