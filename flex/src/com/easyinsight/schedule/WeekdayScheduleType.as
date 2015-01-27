package com.easyinsight.schedule {
import com.easyinsight.framework.User;

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
        if (useAccountTimezone) {
            return "Every Weekday on " + hour + ":" + minuteString + " (" + User.getInstance().accountTimezone + ")";
        } else {
            return "Every Weekday on " + hour + ":" + minuteString + " (UTC" + (timeOffset > 0 ? "-" : "+") + (timeOffset / 60) + ":00)";
        }
    }
}
}