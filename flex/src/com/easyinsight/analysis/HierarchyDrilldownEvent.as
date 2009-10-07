package com.easyinsight.analysis {
import flash.events.Event;
import com.easyinsight.filtering.FilterRawData;
public class HierarchyDrilldownEvent extends Event {
    public static const DRILLDOWN:String = "drilldown";

    public var filterRawData:FilterRawData;
    public var hierarchyItem:AnalysisHierarchyItem;
    public var position:int;

    public function HierarchyDrilldownEvent(type:String, filterRawData:FilterRawData, hierarchyItem:AnalysisHierarchyItem,
            position:int) {
        super(type, true, true);
        this.hierarchyItem = hierarchyItem;
        this.position = position;
        this.filterRawData = filterRawData;
    }

    override public function clone():Event {
        return new HierarchyDrilldownEvent(type, filterRawData, hierarchyItem, position);
    }
}
}