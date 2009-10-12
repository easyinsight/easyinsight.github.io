package com.easyinsight.outboundnotifications {
[Bindable]
[RemoteClass(alias="com.easyinsight.outboundnotifications.BroadcastInfo")]
public class BroadcastInfo extends OutboundEvent{

    public var message:String;

    public function BroadcastInfo() {
        super();
    }

}
}