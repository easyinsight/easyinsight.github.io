package com.easyinsight.gnip {
import com.easyinsight.datasources.GnipFilter;

import flash.events.Event;
import flash.events.Event;

public class GnipFilterEvent extends Event {

    public static const FILTER_ADDED:String = "filterAdded";
    public static const FILTER_UPDATED:String = "filterUpdated";
    public static const FILTER_DELETED:String = "filterDeleted";

    public var _gnipFilter:GnipFilter;

    public function GnipFilterEvent(type:String, gnipFilter:GnipFilter) {
        super(type, true);
        _gnipFilter = gnipFilter;
    }

    override public function clone():Event {
        return new GnipFilterEvent(type, _gnipFilter);
    }
}
}