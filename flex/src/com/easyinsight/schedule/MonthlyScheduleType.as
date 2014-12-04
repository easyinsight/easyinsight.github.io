package com.easyinsight.schedule {
import com.easyinsight.framework.User;

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
        if (useAccountTimezone) {
            return dayOfMonth + suffix + " day of the month at " + hour + ":" + minuteString + " (" + User.getInstance().accountTimezone + ")";
        } else {
            return dayOfMonth + suffix + " day of the month at " + hour + ":" + minuteString + " (UTC" + (timeOffset > 0 ? "-" : "+") + (timeOffset / 60) + ":00)";
        }
    }
}
}