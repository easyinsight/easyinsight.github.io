package com.easyinsight.goals;

import com.easyinsight.solutions.SolutionGoalTreeDescriptor;

import java.util.List;

/**
 * User: James Boe
 * Date: Feb 15, 2009
 * Time: 12:15:26 AM
 */
public class AvailableGoalTreeList {
    private List<GoalTreeDescriptor> myTrees;
    private List<SolutionGoalTreeDescriptor> solutionTrees;

    public List<GoalTreeDescriptor> getMyTrees() {
        return myTrees;
    }

    public void setMyTrees(List<GoalTreeDescriptor> myTrees) {
        this.myTrees = myTrees;
    }

    public List<SolutionGoalTreeDescriptor> getSolutionTrees() {
        return solutionTrees;
    }

    public void setSolutionTrees(List<SolutionGoalTreeDescriptor> solutionTrees) {
        this.solutionTrees = solutionTrees;
    }
}
