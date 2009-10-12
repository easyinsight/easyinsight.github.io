package com.easyinsight.outboundnotifications {
import flash.events.Event;

public class AsyncDeleteEvent extends Event{
    public var info:RefreshEventInfo;
    public function AsyncDeleteEvent(refreshInfo:RefreshEventInfo) {
        this.info = refreshInfo;
        super(ASYNC_DELETE, true);
    }

    public static const ASYNC_DELETE:String = "asyncDelete";}
}