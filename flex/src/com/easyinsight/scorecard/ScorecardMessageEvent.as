package com.easyinsight.scorecard {
import flash.events.Event;

public class ScorecardMessageEvent extends Event {

    public static const SCORECARD_MESSAGE:String = "scorecardMessage";

    public var scorecardAsyncEvent:ScorecardAsyncEvent;

    public function ScorecardMessageEvent(scorecardAsyncEvent:ScorecardAsyncEvent) {
        super(SCORECARD_MESSAGE);
        this.scorecardAsyncEvent = scorecardAsyncEvent;
    }

    override public function clone():Event {
        return new ScorecardMessageEvent(scorecardAsyncEvent);
    }
}
}