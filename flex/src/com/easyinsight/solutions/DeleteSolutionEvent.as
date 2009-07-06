package com.easyinsight.solutions {
import flash.events.Event;
import flash.events.Event;

public class DeleteSolutionEvent extends Event{

    public static const DELETE_SOLUTION:String = "deleteSolution";

    public var solutionID:int;

    public function DeleteSolutionEvent(solutionID:int) {
        super(DELETE_SOLUTION, true);
        this.solutionID = solutionID;
    }

    override public function clone():Event {
        return new DeleteSolutionEvent(solutionID);
    }
}
}