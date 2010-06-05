package com.easyinsight.schedule {
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
        return "Every T/R on " + hour + ":" + minuteString;
    }
}
}