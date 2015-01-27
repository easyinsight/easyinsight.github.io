package com.easyinsight.schedule {
import com.easyinsight.framework.User;

[Bindable]
[RemoteClass(alias="com.easyinsight.export.MWFScheduleType")]
public class MWFScheduleType extends ScheduleType {
    public function MWFScheduleType() {
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
            return "Every M/W/F on " + hour + ":" + minuteString + " (" + User.getInstance().accountTimezone + ")";
        } else {
            return "Every M/W/F on " + hour + ":" + minuteString + " (UTC" + (timeOffset > 0 ? "-" : "+") + (timeOffset / 60) + ":00)";
        }
    }
}
}