package com.easyinsight.goals {
[Bindable]
[RemoteClass(alias="com.easyinsight.goals.GoalValue")]
public class GoalValue {
    public var goalTreeNodeID:int;
    public var value:Number;
    public var date:Date;

    public function GoalValue() {
    }
}
}