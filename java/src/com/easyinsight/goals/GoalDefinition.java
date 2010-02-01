package com.easyinsight.goals;

/**
 * User: jamesboe
 * Date: Jan 21, 2010
 * Time: 12:03:35 PM
 */
public class GoalDefinition extends Objective {
    private double goalValue;
    private boolean highIsGood;
    private GoalTreeMilestone milestone;

    public double getGoalValue() {
        return goalValue;
    }

    public void setGoalValue(double goalValue) {
        this.goalValue = goalValue;
    }

    public boolean isHighIsGood() {
        return highIsGood;
    }

    public void setHighIsGood(boolean highIsGood) {
        this.highIsGood = highIsGood;
    }

    public GoalTreeMilestone getMilestone() {
        return milestone;
    }

    public void setMilestone(GoalTreeMilestone milestone) {
        this.milestone = milestone;
    }
}
