package com.easyinsight.goals {
import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.goals.AvailableGoalTreeList")]
public class AvailableGoalTreeList {

    public var myTrees:ArrayCollection;
    public var solutionTrees:ArrayCollection;

    public function AvailableGoalTreeList() {

    }
}
}