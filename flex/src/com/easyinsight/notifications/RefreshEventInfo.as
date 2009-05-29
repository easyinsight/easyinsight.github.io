package com.easyinsight.notifications {
[Bindable]
[RemoteClass(alias="com.easyinsight.scheduler.RefreshEventInfo")]
public class RefreshEventInfo {

    public static var ADD:int = 1;
    public static var COMPLETE:int = 2;
    public static var ERROR:int = 3;

    public var taskId:int;
    public var userId:int;
    public var feedId:int;
    public var feedName:String;
    public var action:int;
    public var message:String;

    public function RefreshEventInfo() {

    }
}
}