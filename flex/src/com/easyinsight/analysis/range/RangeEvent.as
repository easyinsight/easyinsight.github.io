package com.easyinsight.analysis.range {
import flash.events.Event;

public class RangeEvent extends Event {

    public static const DELETE_RANGE:String = "deleteRange";
    public static const NEW_RANGE_ABOVE:String = "newRangeAbove";
    public static const NEW_RANGE_BELOW:String = "newRangeBelow";

    public var rangeOption:RangeOption;

    public function RangeEvent(type:String, rangeOption:RangeOption) {
        super(type);
        this.rangeOption = rangeOption;
    }

    override public function clone():Event {
        return new RangeEvent(type, rangeOption);
    }
}
}