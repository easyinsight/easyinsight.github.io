package com.easyinsight.goals;

/**
 * User: James Boe
 * Date: Oct 23, 2008
 * Time: 7:43:18 PM
 */
public class GoalToNumberCondition extends GoalCondition {
    private double target;

    public double getTarget() {
        return target;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public double calculateScore() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
