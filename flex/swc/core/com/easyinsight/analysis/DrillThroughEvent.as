package com.easyinsight.analysis {
import com.easyinsight.solutions.InsightDescriptor;

import flash.events.Event;

public class DrillThroughEvent extends Event {

    public static const DRILL_THROUGH:String = "dThrough";

    public var report:InsightDescriptor;
    public var drillThrough:DrillThrough;

    public function DrillThroughEvent(report:InsightDescriptor, drillThrough:DrillThrough) {
        super(DRILL_THROUGH);
        this.report = report;
        this.drillThrough = drillThrough;
    }

    override public function clone():Event {
        return new DrillThroughEvent(report, drillThrough);
    }
}
}