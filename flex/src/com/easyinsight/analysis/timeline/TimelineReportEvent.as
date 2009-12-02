package com.easyinsight.analysis.timeline {
import com.easyinsight.analysis.AnalysisDefinition;

import flash.events.Event;

public class TimelineReportEvent extends Event {

    public static const TIMELINE_REPORT:String = "timelineReport";

    public var report:AnalysisDefinition;

    public function TimelineReportEvent(report:AnalysisDefinition) {
        super(TIMELINE_REPORT);
        this.report = report;
    }

    override public function clone():Event {
        return new TimelineReportEvent(report);
    }
}
}