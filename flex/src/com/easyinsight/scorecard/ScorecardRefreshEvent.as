package com.easyinsight.scorecard {
import flash.events.Event;

public class ScorecardRefreshEvent extends Event {

    public static const SCORECARD_REFRESH:String = "scorecardRefresh";

    public function ScorecardRefreshEvent() {
        super(SCORECARD_REFRESH);
    }

    override public function clone():Event {
        return new ScorecardRefreshEvent();
    }
}
}