package com.easyinsight.analysis {
import flash.events.Event;

public class UpdateReportEvent extends Event{

    public static const UPDATE_REPORT:String = "updateReport";

    public var report:AnalysisDefinition;

    public function UpdateReportEvent(report:AnalysisDefinition) {
        super(UPDATE_REPORT);
        this.report = report;
    }

    override public function clone():Event {
        return new UpdateReportEvent(report);
    }
}
}