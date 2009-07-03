package com.easyinsight.goals {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.goals.GoalSaveInfo")]
public class GoalSaveInfo {

    public var goalTree:GoalTree;
    public var installInfos:ArrayCollection;

    public function GoalSaveInfo() {
    }
}
}