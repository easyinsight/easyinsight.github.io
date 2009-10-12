package com.easyinsight.outboundnotifications {
[Bindable]
[RemoteClass(alias="com.easyinsight.scheduler.OutboundEvent")]
public class OutboundEvent {

    public var broadcast:Boolean;
    public var userId:int;

    public function OutboundEvent() {

    }
}
}