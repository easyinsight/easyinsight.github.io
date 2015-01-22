package com.easyinsight.schedule {
import com.easyinsight.framework.User;

[Bindable]
[RemoteClass(alias="com.easyinsight.export.TRScheduleType")]
public class TRScheduleType extends ScheduleType {
    public function TRScheduleType() {
        super();
    }

    override public function get interval():String {
        var minuteString:String;
        if (minute < 10) {
            minuteString = "0" + minute;
        } else {
            minuteString = String(minute);
        }
        if (useAccountTimezone && User.getInstance().accountTimezone != null && User.getInstance().accountTimezone != "") {
            return "Every T/R on " + hour + ":" + minuteString + " (" + User.getInstance().accountTimezone + ")";
        } else {
            return "Every T/R on " + hour + ":" + minuteString + " (UTC" + (timeOffset > 0 ? "-" : "+") + (timeOffset / 60) + ":00)";
        }
    }
}
}