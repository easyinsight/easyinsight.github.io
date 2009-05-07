package com.easyinsight.analysis {
import flash.events.Event;
public class ReportDataEvent extends Event{

    public static const REQUEST_DATA:String = "requestData";

    public var reload:Boolean = false;

    public function ReportDataEvent(type:String, reload:Boolean = true) {
        super(type);
        this.reload = reload;
    }

    override public function clone():Event {
        return new ReportDataEvent(type);
    }
}
}