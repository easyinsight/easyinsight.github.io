package com.easyinsight.schedule {
[Bindable]
[RemoteClass(alias="com.easyinsight.export.WeekdayScheduleType")]
public class WeekdayScheduleType extends ScheduleType {
    public function WeekdayScheduleType() {
        super();
    }

    override public function get interval():String {
        var minuteString:String;
        if (minute < 10) {
            minuteString = "0" + minute;
        } else {
            minuteString = String(minute);
        }
        return "Every Weekday on " + hour + ":" + minuteString;
    }
}
}