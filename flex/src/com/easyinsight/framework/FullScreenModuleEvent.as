package com.easyinsight.framework {
import flash.display.DisplayObject;
import flash.events.Event;
public class FullScreenModuleEvent extends Event{

    public static const MODULE_LOADED:String = "moduleLoaded";

    public var val:DisplayObject;

    public function FullScreenModuleEvent(val:DisplayObject) {
        super(MODULE_LOADED);
        this.val = val;
    }

    override public function clone():Event {
        return new FullScreenModuleEvent(val);
    }
}
}