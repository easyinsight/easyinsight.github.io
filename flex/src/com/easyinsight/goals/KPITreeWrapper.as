package com.easyinsight.goals {
import com.easyinsight.analysis.ReportFault;

[Bindable]
[RemoteClass(alias="com.easyinsight.goals.KPITreeWrapper")]
public class KPITreeWrapper {

    public var goalTree:GoalTree;
    public var reportFault:ReportFault;
    public var asyncRefresh:Boolean;

    public function KPITreeWrapper() {
    }
}
}