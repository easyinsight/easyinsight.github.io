package com.easyinsight.scorecard {
import flash.events.Event;

import mx.collections.ArrayCollection;

public class ScorecardReorderEvent extends Event {

    public static const SCORECARD_REORDER:String = "scorecardReorder";

    public var scorecards:ArrayCollection;

    public function ScorecardReorderEvent(scorecards:ArrayCollection) {
        super(SCORECARD_REORDER);
        this.scorecards = scorecards;
    }

    override public function clone():Event {
        return new ScorecardReorderEvent(scorecards);
    }
}
}