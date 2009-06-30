package com.easyinsight.goals {
import flash.events.Event;

public class GoalContextEvent extends Event{

    public static const NEW_PARENT:String = "newParent";
    public static const NEW_TREE:String = "newTree";
    public static const MOVE_GOAL:String = "moveGoal";

    public var goalTreeNode:GoalTreeNode;
    public var renderer:GoalAdminRenderer2;

    public function GoalContextEvent(type:String, goalTreeNode:GoalTreeNode, renderer:GoalAdminRenderer2) {
        super(type, true);
        this.goalTreeNode = goalTreeNode;
        this.renderer = renderer;
    }

    override public function clone():Event {
        return new GoalContextEvent(type, goalTreeNode, renderer);
    }
}
}