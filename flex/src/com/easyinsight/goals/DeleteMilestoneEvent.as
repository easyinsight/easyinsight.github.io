package com.easyinsight.goals {

import flash.events.Event;

public class DeleteMilestoneEvent extends Event{

    public static const DELETE_MILESTONE:String = "deleteMilestone";

    public var milestone:GoalTreeMilestone;

    public function DeleteMilestoneEvent(milestone:GoalTreeMilestone) {
        super(DELETE_MILESTONE, true);
        this.milestone = milestone;
    }

    override public function clone():Event {
        return new DeleteMilestoneEvent(milestone);
    }
}
}