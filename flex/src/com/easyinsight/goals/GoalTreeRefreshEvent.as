package com.easyinsight.goals {

import flash.events.Event;

public class GoalTreeRefreshEvent extends Event{

    public static const GOAL_TREE_REFRESH:String = "goalTreeRefresh";

    public var goalTree:GoalTree;

    public function GoalTreeRefreshEvent(goalTree:GoalTree) {
        super(GOAL_TREE_REFRESH);
        this.goalTree = goalTree;
    }

    override public function clone():Event {
        return new GoalTreeRefreshEvent(goalTree);
    }
}
}