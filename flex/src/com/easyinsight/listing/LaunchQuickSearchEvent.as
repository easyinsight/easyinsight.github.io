package com.easyinsight.listing {
import flash.events.Event;

public class LaunchQuickSearchEvent extends Event{

    public static const LAUNCH_QUICK_SEARCH:String = "launchQuickSearch";

    public function LaunchQuickSearchEvent() {
        super(LAUNCH_QUICK_SEARCH);
    }

    override public function clone():Event {
        return new LaunchQuickSearchEvent();
    }
}
}