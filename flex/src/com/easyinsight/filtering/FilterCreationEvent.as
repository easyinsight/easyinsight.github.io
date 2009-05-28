package com.easyinsight.filtering {
import flash.events.Event;

public class FilterCreationEvent extends Event{

    public static const FILTER_CREATION:String = "filterCreation";

    public var filter:IFilter;

    public function FilterCreationEvent(filter:IFilter) {
        super(FILTER_CREATION);
        this.filter = filter;
    }

    override public function clone():Event {
        return new FilterCreationEvent(filter);
    }
}
}