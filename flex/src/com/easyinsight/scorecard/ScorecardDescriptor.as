package com.easyinsight.scorecard {
import com.easyinsight.quicksearch.EIDescriptor;

[Bindable]
[RemoteClass(alias="com.easyinsight.scorecard.ScorecardDescriptor")]
public class ScorecardDescriptor extends EIDescriptor {

    public var dataSourceID:int;

    public function ScorecardDescriptor() {
        super();
    }

    override public function getType():int {
        return EIDescriptor.SCORECARD;
    }

    override public function get typeString():String {
        return "Scorecard";
    }

    public function get size():int {
        return 0;
    }
}
}