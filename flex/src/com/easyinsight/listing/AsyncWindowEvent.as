package com.easyinsight.listing {
import flash.events.Event;

public class AsyncWindowEvent extends Event{

    public static const LAUNCH_ASYNC_WINDOW:String = "launchAsyncWindow";

    public function AsyncWindowEvent() {
        super(LAUNCH_ASYNC_WINDOW);
    }

    override public function clone():Event {
        return new LaunchQuickSearchEvent();
    }
}
}