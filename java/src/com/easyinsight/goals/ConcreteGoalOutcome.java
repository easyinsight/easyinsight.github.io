package com.easyinsight.goals;

/**
 * User: James Boe
 * Date: Jan 4, 2009
 * Time: 3:43:44 PM
 */
public class ConcreteGoalOutcome extends GoalOutcome {
    private double goalValue;
    private double endValue;
    private double startValue;
    private double percentChange;

    public ConcreteGoalOutcome(int outcomeState, double goalValue, double endValue, double startValue, double percentChange, double outcomeValue) {
        super(outcomeState, outcomeValue);
        this.goalValue = goalValue;
        this.endValue = endValue;
        this.startValue = startValue;
        this.percentChange = percentChange;
    }

    public ConcreteGoalOutcome() {
    }

    public double getGoalValue() {
        return goalValue;
    }

    public void setGoalValue(double goalValue) {
        this.goalValue = goalValue;
    }

    public double getEndValue() {
        return endValue;
    }

    public void setEndValue(double endValue) {
        this.endValue = endValue;
    }

    public double getStartValue() {
        return startValue;
    }

    public void setStartValue(double startValue) {
        this.startValue = startValue;
    }

    public double getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(double percentChange) {
        this.percentChange = percentChange;
    }
}
