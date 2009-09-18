package com.easyinsight.genredata {
import flash.events.Event;

public class ExchangeControllerEvent extends Event{

    public static const CHANGE_VIEW:String = "changeView";

    public var exchangePage:ExchangePage;

    public function ExchangeControllerEvent(exchangePage:ExchangePage) {
        super(CHANGE_VIEW);
        this.exchangePage = exchangePage;
    }

    override public function clone():Event {
        return new ExchangeControllerEvent(exchangePage);
    }
}
}