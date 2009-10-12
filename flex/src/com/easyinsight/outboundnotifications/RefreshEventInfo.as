package com.easyinsight.outboundnotifications {
[Bindable]
[RemoteClass(alias="com.easyinsight.scheduler.RefreshEventInfo")]
public class RefreshEventInfo extends OutboundEvent{

    public static const ADD:int = 1;
    public static const COMPLETE:int = 2;
    public static const ERROR:int = 3;
    public static const CREATE:int = 4;

    public var taskId:int;

    public var feedId:int;
    public var feedName:String;
    public var action:int;
    public var message:String;

    public function RefreshEventInfo() {

    }

    }
}