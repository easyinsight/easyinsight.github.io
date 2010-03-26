package com.easyinsight.scorecard {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.scorecard.ScorecardWrapper")]
public class ScorecardWrapper {

    public var scorecard:Scorecard;
    public var credentials:ArrayCollection;
    public var asyncRefresh:Boolean;
    public var asyncRefreshKpis:ArrayCollection;

    public function ScorecardWrapper() {
    }
}
}