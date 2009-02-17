package com.easyinsight.goals {
[Bindable]
[RemoteClass(alias="com.easyinsight.solutions.SolutionGoalTreeDescriptor")]
public class SolutionGoalTreeDescriptor extends GoalTreeDescriptor{

    public var solutionID:int;
    public var solutionName:String;

    public function SolutionGoalTreeDescriptor() {
        super();
    }
}
}