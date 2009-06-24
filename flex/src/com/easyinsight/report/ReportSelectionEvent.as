package com.easyinsight.report {
import com.easyinsight.solutions.InsightDescriptor;

import flash.events.Event;

public class ReportSelectionEvent extends Event{

    public static const REPORT_SELECTION:String = "reportSelection";

    public var report:InsightDescriptor;

    public function ReportSelectionEvent(report:InsightDescriptor) {
        super(REPORT_SELECTION);
        this.report = report;
    }

    override public function clone():Event {
        return new ReportSelectionEvent(report);
    }
}
}