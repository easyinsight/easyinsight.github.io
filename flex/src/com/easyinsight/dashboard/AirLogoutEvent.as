package com.easyinsight.dashboard {
import flash.events.Event;

public class AirLogoutEvent extends Event{

    public static const AIR_LOGOUT:String = "airLogout";

    public function AirLogoutEvent() {
        super(AIR_LOGOUT);
    }

    override public function clone():Event {
        return new AirLogoutEvent();
    }
}
}