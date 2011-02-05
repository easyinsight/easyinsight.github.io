package com.easyinsight.scorecard {
import com.easyinsight.analysis.ReportFault;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.scorecard.ScorecardResults")]
public class ScorecardResults {

    public var reportFault:ReportFault;
    public var outcomes:ArrayCollection;

    public function ScorecardResults() {
    }
}
}