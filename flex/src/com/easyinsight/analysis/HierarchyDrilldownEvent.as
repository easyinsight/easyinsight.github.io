package com.easyinsight.analysis {
import flash.events.Event;
import com.easyinsight.filtering.FilterRawData;
public class HierarchyDrilldownEvent extends Event {
    public static const DRILLDOWN:String = "drilldown";

    public var filterRawData:FilterRawData;

    public function HierarchyDrilldownEvent(type:String, filterRawData:FilterRawData) {
        super(type, true, true);
        this.filterRawData = filterRawData;
    }

    override public function clone():Event {
        return new HierarchyDrilldownEvent(type, filterRawData);
    }
}
}