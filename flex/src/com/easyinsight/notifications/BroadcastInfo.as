package com.easyinsight.notifications {
[Bindable]
[RemoteClass(alias="com.easyinsight.notifications.BroadcastInfo")]
public class BroadcastInfo extends OutboundEvent{

    public var message:String;

    public function BroadcastInfo() {
        super();
    }

}
}