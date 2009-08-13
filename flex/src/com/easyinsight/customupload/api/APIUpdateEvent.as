package com.easyinsight.customupload.api {

import flash.events.Event;

public class APIUpdateEvent extends Event{

    public static const API_UPDATE:String = "apiUpdate";

    public function APIUpdateEvent() {
        super(API_UPDATE);
    }

    override public function clone():Event {
        return new APIUpdateEvent();
    }
}
}