package com.easyinsight.framework {
import flash.events.Event;

public class ActivationEvent extends Event{

    public static const URL_ACTIVATION:String = "urlActivation";

    public var url:String;

    public function ActivationEvent(url:String) {
        super(URL_ACTIVATION, true);
        this.url = url;
    }

    override public function clone():Event {
        return new ActivationEvent(url);
    }
}
}