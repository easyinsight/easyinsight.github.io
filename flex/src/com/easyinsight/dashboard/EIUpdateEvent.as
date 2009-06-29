package com.easyinsight.dashboard {
import flash.events.Event;

public class EIUpdateEvent extends Event{

    public static const EI_UPDATE:String = "eiUpdate";

    public function EIUpdateEvent() {
        super(EI_UPDATE);
    }

    override public function clone():Event {
        return new EIUpdateEvent();
    }
}
}