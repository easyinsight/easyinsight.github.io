package com.easyinsight.listing {
import flash.events.Event;
import flash.events.Event;

public class ReportBugEvent extends Event {

    public function ReportBugEvent() {
        super(REPORT_BUG);
    }

    override public function clone():Event {
        return new ReportBugEvent();
    }

    public static const REPORT_BUG:String = "reportBug";
}
}