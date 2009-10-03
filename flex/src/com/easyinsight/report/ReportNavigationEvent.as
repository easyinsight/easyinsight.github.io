package com.easyinsight.report {
import com.easyinsight.solutions.InsightDescriptor;

import flash.events.Event;

import mx.collections.ArrayCollection;

public class ReportNavigationEvent extends Event{

    public static const TO_REPORT:String = "toReport";

    public var descriptor:InsightDescriptor;
    public var filters:ArrayCollection;

    public function ReportNavigationEvent(type:String, descriptor:InsightDescriptor, filters:ArrayCollection) {
        super(type, true);
        this.descriptor = descriptor;
        this.filters = filters;
    }

    override public function clone():Event {
        return new ReportNavigationEvent(type, descriptor, filters);
    }
}
}