package com.easyinsight.analysis {

import flash.events.Event;

public class DrillThroughEvent extends Event {

    public static const DRILL_THROUGH:String = "dThrough";

    public var drillThrough:DrillThrough;
    public var drillThroughResponse:DrillThroughResponse;

    public function DrillThroughEvent(drillThrough:DrillThrough, drillThroughResponse:DrillThroughResponse) {
        super(DRILL_THROUGH);
        this.drillThrough = drillThrough;
        this.drillThroughResponse = drillThroughResponse;
    }

    override public function clone():Event {
        return new DrillThroughEvent(drillThrough, drillThroughResponse);
    }
}
}