package com.easyinsight.notifications {
[Bindable]
[RemoteClass(alias="com.easyinsight.notifications.TodoEventInfo")]
public class TodoEventInfo extends OutboundEvent{

    public static var ADD:int  = 1;
    public static var COMPLETE:int = 2;
    public static var ERROR:int = 3;

    public var todoID:int;
    public var action:int;

    public function TodoEventInfo() {
        super();
    }

    public function getTitle():String {
        return "";
    }

    public function get title():String {
        return getTitle(); 
    }
}
}