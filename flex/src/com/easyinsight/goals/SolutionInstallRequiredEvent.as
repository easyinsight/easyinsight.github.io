package com.easyinsight.goals {
import flash.events.Event;
public class SolutionInstallRequiredEvent extends Event {

    public static const SOLUTION_INSTALL:String = "solutionInstall";

    public var solutionDescriptor:SolutionGoalTreeDescriptor;

    public function SolutionInstallRequiredEvent(type:String, solutionDescriptor:SolutionGoalTreeDescriptor) {
        super(type, true);
        this.solutionDescriptor = solutionDescriptor;
    }

    override public function clone():Event {
        return new SolutionInstallRequiredEvent(type, solutionDescriptor);
    }
}
}