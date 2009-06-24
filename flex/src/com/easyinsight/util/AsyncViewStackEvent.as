package com.easyinsight.util {
import flash.events.Event;

public class AsyncViewStackEvent extends Event{

    public static const FULL_SCREEN:String = "fullScreen";

    public function AsyncViewStackEvent(type:String) {
        super(type);
    }

    override public function clone():Event {
        return new AsyncViewStackEvent(type);
    }
}
}