package com.easyinsight.analysis {
import flash.events.Event;
public class ReportDataEvent extends Event{

    public static const REQUEST_DATA:String = "requestData";

    public function ReportDataEvent(type:String) {
        super(type);
    }

    override public function clone():Event {
        return new ReportDataEvent(type);
    }
}
}