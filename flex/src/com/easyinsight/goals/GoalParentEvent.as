package com.easyinsight.goals {
import flash.events.Event;

public class GoalParentEvent extends Event{

    public static const ASSIGN_PARENT:String = "assignParent";

    public var goal:GoalTreeNode;

    public function GoalParentEvent(goal:GoalTreeNode) {
        super(ASSIGN_PARENT, true);
        this.goal = goal;
    }

    override public function clone():Event {
        return new GoalParentEvent(goal);
    }
}
}