package com.easyinsight.report {
import com.easyinsight.solutions.InsightDescriptor;

import flash.events.Event;

public class ReportNavigationEvent extends Event{

    public static const TO_REPORT:String = "toReport";

    public var descriptor:InsightDescriptor;

    public function ReportNavigationEvent(type:String, descriptor:InsightDescriptor) {
        super(type, true);
        this.descriptor = descriptor;
    }

    override public function clone():Event {
        return new ReportNavigationEvent(type, descriptor);
    }
}
}