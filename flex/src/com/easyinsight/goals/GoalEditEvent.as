package com.easyinsight.goals {
import flash.events.Event;
public class GoalEditEvent extends Event {

    public static const GOAL_EDIT:String = "goalEdit";

    public function GoalEditEvent() {
        super(GOAL_EDIT);
    }


    override public function clone():Event {
        return new GoalEditEvent();
    }
}
}