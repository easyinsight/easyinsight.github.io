package com.easyinsight.util {
import flash.display.DisplayObject;
import flash.events.Event;

public class ModelessWindowEvent extends Event{

    public static const WINDOW_ADDED:String = "windowAdded";
    public static const WINDOW_REMOVED:String = "windowRemoved";

    public var displayObject:DisplayObject;

    public function ModelessWindowEvent(type:String, displayObject:DisplayObject) {
        super(type, true);
        this.displayObject = displayObject;
    }

    override public function clone():Event {
        return new ModelessWindowEvent(type, displayObject);
    }
}
}