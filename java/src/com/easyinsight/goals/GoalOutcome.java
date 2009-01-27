package com.easyinsight.goals;

/**
 * User: James Boe
 * Date: Jan 2, 2009
 * Time: 10:32:49 AM
 */
public class GoalOutcome {
    // outcome is exceeding goal, positive, neutral, negative

    public static final int EXCEEDING_GOAL = 1;
    public static final int POSITIVE = 2;
    public static final int NEUTRAL = 3;
    public static final int NEGATIVE = 4;
    public static final int NO_DATA = 5;

    private int outcomeState;
    private double outcomeValue;

    public GoalOutcome() {
    }

    public double getOutcomeValue() {
        return outcomeValue;
    }

    public void setOutcomeValue(double outcomeValue) {
        this.outcomeValue = outcomeValue;
    }

    public int getOutcomeState() {
        return outcomeState;
    }

    public void setOutcomeState(int outcomeState) {
        this.outcomeState = outcomeState;
    }

    public GoalOutcome(int outcomeState, double outcomeValue) {
        this.outcomeState = outcomeState;
        this.outcomeValue = outcomeValue;
    }
}
