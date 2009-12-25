package com.easyinsight.analysis {
import flash.events.Event;

public class ReportSizingEvent extends Event {

    public static const REPORT_SIZING:String = "reportSizing";
    public var width:int;

    public function ReportSizingEvent(width:int) {
        super(REPORT_SIZING);
        this.width = width;
    }

    override public function clone():Event {
        return new ReportSizingEvent(width);
    }
}
}