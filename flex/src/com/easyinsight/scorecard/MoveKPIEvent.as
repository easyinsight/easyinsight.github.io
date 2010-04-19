package com.easyinsight.scorecard {
import com.easyinsight.kpi.KPI;

import flash.events.Event;

public class MoveKPIEvent extends Event {

    public static const KPI_MOVE:String = "kpiMove";

    public var kpi:KPI;
    public var destinationScorecard:ScorecardDescriptor;
    public var sourceScorecard:ScorecardDescriptor;

    public function MoveKPIEvent(kpi:KPI, destinationScorecard:ScorecardDescriptor, sourceScorecard:ScorecardDescriptor = null) {
        super(KPI_MOVE);
        this.kpi = kpi;
        this.destinationScorecard = destinationScorecard;
        this.sourceScorecard = sourceScorecard;
    }

    override public function clone():Event {
        return new MoveKPIEvent(kpi, destinationScorecard, sourceScorecard);
    }
}
}