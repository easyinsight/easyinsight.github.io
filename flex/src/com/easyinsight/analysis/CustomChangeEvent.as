package com.easyinsight.analysis {
import flash.events.Event;
public class CustomChangeEvent extends Event{

    public static const CUSTOM_CHANGE:String = "customChange";

    public function CustomChangeEvent() {
        super(CUSTOM_CHANGE);
    }


    override public function clone():Event {
        return new CustomChangeEvent();
    }
}
}