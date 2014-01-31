package com.easyinsight.filtering {
import com.easyinsight.dashboard.*;

import flash.events.Event;

public class FilterSetSaveEvent extends Event {

    public static const FILTER_SET_SAVE:String = "filterSetSave";

    public var filterSet:FilterSet;

    public function FilterSetSaveEvent(filterSet:FilterSet) {
        super(FILTER_SET_SAVE);
        this.filterSet = filterSet;
    }

    override public function clone():Event {
        return new FilterSetSaveEvent(filterSet);
    }
}
}