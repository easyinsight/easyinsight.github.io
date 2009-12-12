package com.easyinsight.reportpackage {
import com.easyinsight.solutions.InsightDescriptor;

import flash.events.Event;

public class PackageReportEvent extends Event {

    public static const ADD_REPORT:String = "addReport";
    public static const REMOVE_REPORT:String = "removeReport";

    public var insightDescriptor:InsightDescriptor;

    public function PackageReportEvent(type:String, insightDescriptor:InsightDescriptor) {
        super(type, true);
        this.insightDescriptor = insightDescriptor;
    }

    override public function clone():Event {
        return new PackageReportEvent(type, insightDescriptor);
    }
}
}