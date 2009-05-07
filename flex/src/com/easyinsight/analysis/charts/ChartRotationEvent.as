package com.easyinsight.analysis.charts {
import com.easyinsight.analysis.CustomChangeEvent;

import flash.events.Event;

public class ChartRotationEvent extends CustomChangeEvent {

    public var rotation:int;
    public var elevation:int;

    public function ChartRotationEvent(rotation:int, elevation:int) {
        super();
        this.rotation = rotation;
        this.elevation = elevation;
    }

    override public function clone():Event {
        return new ChartRotationEvent(rotation, elevation);
    }
}
}