package com.easyinsight.goals {
[Bindable]
[RemoteClass(alias="com.easyinsight.goals.GoalTreeNodeData")]
public class GoalTreeNodeData extends GoalTreeNode {
    public var goalOutcome:GoalOutcome;
    public var currentValue:GoalValue;

    public function GoalTreeNodeData() {
        
    }
}
}