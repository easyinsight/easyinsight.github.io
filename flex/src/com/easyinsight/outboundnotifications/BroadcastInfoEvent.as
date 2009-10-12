package com.easyinsight.outboundnotifications {
import flash.events.Event;

public class BroadcastInfoEvent extends Event {
    public var info:BroadcastInfo;

    public function BroadcastInfoEvent(info:BroadcastInfo) {
        this.info = info;
        super(BROADCAST_INFO, true);
    }

    public static const BROADCAST_INFO:String = "broadcastInfo";}
}