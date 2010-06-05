package com.easyinsight.schedule {
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
        return "Every M/W/F on " + hour + ":" + minuteString;
    }
}
}