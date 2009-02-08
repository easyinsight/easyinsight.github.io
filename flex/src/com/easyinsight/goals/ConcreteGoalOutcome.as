package com.easyinsight.goals {
[Bindable]
[RemoteClass(alias="com.easyinsight.goals.ConcreteGoalOutcome")]
public class ConcreteGoalOutcome extends GoalOutcome {
    public var goalValue:Number;
    public var endValue:Number;
    public var startValue:Number;
    public var percentChange:Number;

    public function ConcreteGoalOutcome() {
        super();
    }
}
}