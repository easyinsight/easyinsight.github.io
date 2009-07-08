package com.easyinsight.goals {
import flash.events.Event;

public class GoalSubscriptionEvent extends Event{

    public static const GOAL_REMOVE:String = "goalRemove";

    public var goalID:int;

    public function GoalSubscriptionEvent(type:String, goalID:int) {
        super(type, true);
        this.goalID = goalID;
    }

    override public function clone():Event {
        return new GoalSubscriptionEvent(type, goalID);
    }
}
}