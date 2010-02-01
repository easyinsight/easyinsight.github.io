package com.easyinsight.goals {
import flash.events.Event;

public class GoalDetailEvent extends Event{

    public static const GOAL_DETAIL_NAV:String = "goalDetailNav";

    public var goalTreeNodeData:GoalTreeNode;

    public function GoalDetailEvent(goalTreeNodeData:GoalTreeNode) {
        super(GOAL_DETAIL_NAV);
        this.goalTreeNodeData = goalTreeNodeData;
    }

    override public function clone():Event {
        return new GoalDetailEvent(goalTreeNodeData);
    }
}
}