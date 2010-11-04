package com.easyinsight.scorecard {
import com.easyinsight.analysis.ReportFault;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.scorecard.ScorecardWrapper")]
public class ScorecardWrapper {

    public var scorecard:Scorecard;
    public var reportFault:ReportFault;
    public var asyncRefresh:Boolean;
    public var asyncRefreshKpis:ArrayCollection;

    public function ScorecardWrapper() {
    }
}
}