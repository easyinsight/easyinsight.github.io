package com.easyinsight.schedule {
[Bindable]
[RemoteClass(alias="com.easyinsight.export.DailyScheduleType")]
public class DailyScheduleType extends ScheduleType {
    public function DailyScheduleType() {
        super();
    }

    override public function get interval():String {
        var minuteString:String;
        if (minute < 10) {
            minuteString = "0" + minute;
        } else {
            minuteString = String(minute);
        }
        return "Daily on " + hour + ":" + minuteString;
    }
}
}