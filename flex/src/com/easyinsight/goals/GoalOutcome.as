package com.easyinsight.goals {
[Bindable]
[RemoteClass(alias="com.easyinsight.goals.GoalOutcome")]
public class GoalOutcome {

    public static const EXCEEDING_GOAL:int = 1;
    public static const POSITIVE:int = 2;
    public static const NEUTRAL:int = 3;
    public static const NEGATIVE:int = 4;
    public static const NO_DATA:int = 5;

    public var outcomeState:int;
    public var outcomeValue:Number;
}
}