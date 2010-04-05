package com.easyinsight.scorecard {
import com.easyinsight.quicksearch.EIDescriptor;

[Bindable]
[RemoteClass(alias="com.easyinsight.scorecard.ScorecardDescriptor")]
public class ScorecardDescriptor extends EIDescriptor {

    public var groupID:int;
    public var groupRole:int;
    public var groupName:String;

    public function ScorecardDescriptor() {
        super();
    }

    override public function getType():int {
        return EIDescriptor.SCORECARD;
    }
}
}