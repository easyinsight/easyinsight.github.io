package com.easyinsight.schedule {
[Bindable]
[RemoteClass(alias="com.easyinsight.export.ScheduledActivity")]
public class ScheduledActivity {

    public var scheduleType:ScheduleType;
    public var scheduledActivityID:int;
    public var problemLevel:int;
    public var problemMessage:String;

    public function ScheduledActivity() {
    }

    public function get activityDisplay():String {
        return "";
    }

    public function get describe():String {
        return "";
    }

    public function get interval():String {
        return scheduleType.interval;
    }
}
}