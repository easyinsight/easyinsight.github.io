package com.easyinsight.analysis {
import flash.events.Event;
import com.easyinsight.filtering.FilterRawData;
public class ChildFilterCreationEvent extends Event {
    public static const CHILD_FILTER_INCLUDE:String = "childFilterCreationInclude";
    public static const CHILD_FILTER_EXCLUDE:String = "childFilterCreationExclude";

    public var filterRawData:FilterRawData;

    public function ChildFilterCreationEvent(type:String, filterRawData:FilterRawData) {
        super(type, true, true);
        this.filterRawData = filterRawData;
    }


    override public function clone():Event {
        return new ChildFilterCreationEvent(type, filterRawData);
    }
}
}