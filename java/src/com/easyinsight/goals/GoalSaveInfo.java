package com.easyinsight.goals;

/**
 * User: James Boe
 * Date: Jul 2, 2009
 * Time: 9:03:29 PM
 */
public class GoalSaveInfo {
    private GoalTree goalTree;

    public GoalSaveInfo() {
    }

    public GoalSaveInfo(GoalTree goalTree) {
        this.goalTree = goalTree;
    }

    public GoalTree getGoalTree() {
        return goalTree;
    }

    public void setGoalTree(GoalTree goalTree) {
        this.goalTree = goalTree;
    }
}
