package com.easyinsight.administration.feed {
import com.easyinsight.analysis.AnalysisHierarchyItem;

import flash.events.Event;

public class DeleteHierarchyEvent extends Event{

    public static const DELETE_HIERARCHY:String = "deleteHierarchy";

    public var hierarchy:AnalysisHierarchyItem;

    public function DeleteHierarchyEvent(hierarchy:AnalysisHierarchyItem) {
        super(DELETE_HIERARCHY, true);
        this.hierarchy = hierarchy;
    }

    override public function clone():Event {
        return new DeleteHierarchyEvent(hierarchy);
    }
}
}