package com.easyinsight.schedule {
[Bindable]
[RemoteClass(alias="com.easyinsight.export.MonthlyScheduleType")]
public class MonthlyScheduleType extends ScheduleType {

    public var dayOfMonth:int;

    public function MonthlyScheduleType() {
        super();
    }

    override public function get interval():String {
        var minuteString:String;
        if (minute < 10) {
            minuteString = "0" + minute;
        } else {
            minuteString = String(minute);
        }
        var suffix:String;
        if (dayOfMonth == 1) {
            suffix = "st";
        } else if (dayOfMonth == 2) {
            suffix = "nd";
        } else if (dayOfMonth == 3) {
            suffix = "rd";
        } else {
            suffix = "th";
        }
        return dayOfMonth + suffix + " day of the month at " + hour + ":" + minuteString;
    }
}
}