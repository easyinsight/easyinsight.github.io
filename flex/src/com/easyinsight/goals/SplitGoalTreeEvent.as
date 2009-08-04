package com.easyinsight.goals {

import flash.events.Event;

public class SplitGoalTreeEvent extends Event{

    public static const SPLIT_TREE:String = "splitTree";

    public var node:GoalTreeNode;
    public var name:String;

    public function SplitGoalTreeEvent(node:GoalTreeNode, name:String) {
        super(SPLIT_TREE);
        this.node = node;
        this.name = name;
    }

    override public function clone():Event {
        return new SplitGoalTreeEvent(node, name);
    }
}
}