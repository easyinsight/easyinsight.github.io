package com.easyinsight.scorecard {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.scorecard.ScorecardList")]
public class ScorecardList {

    public var scorecardDescriptors:ArrayCollection;
    public var anyData:Boolean;

    public function ScorecardList() {
    }
}
}