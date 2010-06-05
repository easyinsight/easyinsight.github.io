package com.easyinsight.schedule {
[Bindable]
[RemoteClass(alias="com.easyinsight.export.ScheduleType")]
public class ScheduleType {

    public var scheduleID:int;
    public var hour:int;
    public var minute:int;

    public function ScheduleType() {
    }

    public function get interval():String {
        return "";
    }
}
}