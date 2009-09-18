package com.easyinsight.genredata {
import flash.events.Event;

import mx.collections.ArrayCollection;

public class ExchangeDataEvent extends Event{

    public static const EXCHANGE_DATA_RETURNED:String = "exchangeDataReturned";

    public var data:ArrayCollection;

    public function ExchangeDataEvent(data:ArrayCollection) {
        super(EXCHANGE_DATA_RETURNED);
        this.data = data;
    }

    override public function clone():Event {
        return new ExchangeDataEvent(data);
    }
}
}