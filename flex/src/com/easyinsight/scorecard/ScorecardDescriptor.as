package com.easyinsight.scorecard {
import com.easyinsight.quicksearch.EIDescriptor;

[Bindable]
[RemoteClass(alias="com.easyinsight.scorecard.ScorecardDescriptor")]
public class ScorecardDescriptor extends EIDescriptor {

    public var lastDataTime:Date;

    public function ScorecardDescriptor() {
        super();
    }

    override public function getType():int {
        return EIDescriptor.SCORECARD;
    }

    public function get size():int {
        return 0;
    }
}
}