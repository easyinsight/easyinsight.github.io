package com.easyinsight.goals {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.goals.KPITreeWrapper")]
public class KPITreeWrapper {

    public var goalTree:GoalTree;
    public var credentials:ArrayCollection;
    public var asyncRefresh:Boolean;

    public function KPITreeWrapper() {
    }
}
}