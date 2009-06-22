package com.easyinsight.solutions {
import flash.events.Event;

public class SolutionSelectionEvent extends Event{

    public static const SOLUTION_SELECTION:String = "solutionSelection";

    public var solution:Solution;

    public function SolutionSelectionEvent(solution:Solution) {
        super(SOLUTION_SELECTION, true);
        this.solution = solution;
    }

    override public function clone():Event {
        return new SolutionSelectionEvent(solution);
    }
}
}