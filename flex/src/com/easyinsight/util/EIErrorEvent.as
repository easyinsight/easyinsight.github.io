package com.easyinsight.util {
import flash.events.Event;

public class EIErrorEvent extends Event {

    public static const ERROR:String = "eiError";

    public var error:Error;

    public function EIErrorEvent(error:Error) {
        super(ERROR);
        this.error = error;
    }

    override public function clone():Event {
        return new EIErrorEvent(error);
    }
}
}