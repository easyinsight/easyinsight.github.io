package com.easyinsight.solutions {

import flash.events.Event;

public class SolutionAdminEvent extends Event{

    public static const SOLUTION_ADMIN:String = "solutionAdmin";

    public function SolutionAdminEvent() {
        super(SOLUTION_ADMIN, true);
    }

    override public function clone():Event {
        return new SolutionAdminEvent();
    }
}
}