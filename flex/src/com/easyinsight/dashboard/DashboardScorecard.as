package com.easyinsight.dashboard {
import com.easyinsight.analysis.CheckBoxReportFormItem;
import com.easyinsight.scorecard.ScorecardDescriptor;

import mx.collections.ArrayCollection;
import mx.core.UIComponent;

[Bindable]
[RemoteClass(alias="com.easyinsight.dashboard.DashboardScorecard")]
public class DashboardScorecard extends DashboardElement {

    public var scorecard:ScorecardDescriptor;
    public var labelPlacement:int;
    public var showLabel:Boolean;

    public function DashboardScorecard() {
        super();
    }
}
}