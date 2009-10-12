package com.easyinsight.outboundnotifications {
import flash.events.Event;

public class BroadcastDeleteEvent extends Event{
    public function BroadcastDeleteEvent(broadcastItem:BroadcastInfo) {
        super(BROADCAST_DELETE, true);
        this.broadcastItem = broadcastItem;
    }

    public static const BROADCAST_DELETE:String = "broadcastDelete";

    public var broadcastItem:BroadcastInfo;}
}