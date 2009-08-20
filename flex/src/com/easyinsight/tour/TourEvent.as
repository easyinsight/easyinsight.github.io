package com.easyinsight.tour {

import flash.events.Event;

public class TourEvent extends Event{

    public static const BACK_TO_WINDOW:String = "backToWindow";

    public function TourEvent() {
        super(BACK_TO_WINDOW);
    }

    override public function clone():Event {
        return new TourEvent();
    }
}
}