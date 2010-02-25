package com.easyinsight.scorecard {
import flash.events.Event;

public class ScorecardEvent extends Event {

    public static const SCORECARD_ADDED:String = "scorecardAdded";
    public static const SCORECARD_UPDATED:String = "scorecardUpdated";
    public static const SCORECARD_REMOVED:String = "scorecardRemoved";
    public static const SCORECARD_FOCUS:String = "scorecardFocus";

    public var scorecard:ScorecardDescriptor;

    public function ScorecardEvent(type:String, scorecard:ScorecardDescriptor) {
        super(type);
        this.scorecard = scorecard;
    }

    override public function clone():Event {
        return new ScorecardEvent(type, scorecard);
    }
}
}