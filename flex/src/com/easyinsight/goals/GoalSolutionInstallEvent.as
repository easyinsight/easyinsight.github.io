package com.easyinsight.goals {
import com.easyinsight.solutions.Solution;
import flash.events.Event;
public class GoalSolutionInstallEvent extends Event {
    public static const GOAL_SOLUTION_INSTALL:String = "goalSolutionInstall";

    public var solution:Solution;


    public function GoalSolutionInstallEvent(solution:Solution) {
        super(GOAL_SOLUTION_INSTALL, true);
        this.solution = solution;
    }


    override public function clone():Event {
        return new GoalSolutionInstallEvent(solution);
    }
}
}