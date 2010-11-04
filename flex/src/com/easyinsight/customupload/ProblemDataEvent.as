package com.easyinsight.customupload {
import flash.events.Event;

public class ProblemDataEvent extends Event {

    public static const PROBLEM_RESOLVED:String = "problemResolved";

    public function ProblemDataEvent() {
        super(PROBLEM_RESOLVED);
    }

    override public function clone():Event {
        return new ProblemDataEvent();
    }
}
}