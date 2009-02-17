package com.easyinsight.goals {
import flash.events.Event;
import flash.events.Event;
public class DeleteGoalTreeEvent extends Event{

    public static const DELETE_GOAL_TREE:String = "deleteGoalTree";

    public var goalTree:GoalTreeDescriptor;

    public function DeleteGoalTreeEvent(goalTree:GoalTreeDescriptor) {
        super(DELETE_GOAL_TREE, true);
        this.goalTree = goalTree;
    }

    override public function clone():Event {
        return new DeleteGoalTreeEvent(goalTree);
    }
}
}