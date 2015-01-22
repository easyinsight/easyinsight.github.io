package com.easyinsight.schedule {
import com.easyinsight.framework.User;

[Bindable]
[RemoteClass(alias="com.easyinsight.export.WeeklyScheduleType")]
public class WeeklyScheduleType extends ScheduleType {

    public var dayOfWeek:int;

    public function WeeklyScheduleType() {
        super();
    }

    override public function get interval():String {
        var minuteString:String;
        if (minute < 10) {
            minuteString = "0" + minute;
        } else {
            minuteString = String(minute);
        }
        var day:String;
        if (dayOfWeek == 1) {
            day = "Sunday";
        } else if (dayOfWeek == 2) {
            day = "Monday";
        } else if (dayOfWeek == 3) {
            day = "Tuesday";
        } else if (dayOfWeek == 4) {
            day = "Wednesday";
        } else if (dayOfWeek == 5) {
            day = "Thursday";
        } else if (dayOfWeek == 6) {
            day = "Friday";
        } else if (dayOfWeek == 7) {
            day = "Saturday";
        }
        if (useAccountTimezone && User.getInstance().accountTimezone != null && User.getInstance().accountTimezone != "") {
            return "Every " + day + " at " + hour + ":" + minuteString + " (" + User.getInstance().accountTimezone + ")";
        } else {
            return "Every " + day + " at " + hour + ":" + minuteString + " (UTC" + (timeOffset > 0 ? "-" : "+") + (timeOffset / 60) + ":00)";
        }
    }
}
}