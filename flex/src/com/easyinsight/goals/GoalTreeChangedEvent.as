package com.easyinsight.goals {
import flash.events.Event;
public class GoalTreeChangedEvent extends Event {

    public static const GOAL_TREE_CHANGED:String = "goalTreeChanged";

    public function GoalTreeChangedEvent() {
        super(GOAL_TREE_CHANGED, true);
    }

    override public function clone():Event {
        return new GoalTreeChangedEvent();
    }
}
}