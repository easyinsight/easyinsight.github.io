package com.easyinsight.goals;

import java.util.Date;

/**
 * User: James Boe
 * Date: Jan 2, 2009
 * Time: 10:32:49 AM
 */
public class GoalOutcome {
    // outcome is exceeding goal, positive, neutral, negative

    public static final int NO_DATA = 0;
    public static final int EXCEEDING_GOAL = 1;
    public static final int POSITIVE = 2;
    public static final int NEUTRAL = 3;
    public static final int NEGATIVE = 4;

    public static final int DOWN_DIRECTION = 1;
    public static final int NO_DIRECTION = 2;
    public static final int UP_DIRECTION = 3;

    private int outcomeState;
    private int direction;
    private Double previousValue;
    private boolean problemEvaluated;
    private Double outcomeValue;
    private Date evaluationDate;
    private long goalTreeNodeID;
    private boolean valueDefined;

    public GoalOutcome() {
    }

    public long getGoalTreeNodeID() {
        return goalTreeNodeID;
    }

    public void setGoalTreeNodeID(long goalTreeNodeID) {
        this.goalTreeNodeID = goalTreeNodeID;
    }

    public GoalOutcome(int outcomeState, int direction, Double previousValue, boolean problemEvaluated, Double outcomeValue, Date evaluationDate,
                       long goalTreeNodeID) {
        this.outcomeState = outcomeState;
        this.direction = direction;
        this.previousValue = previousValue;
        this.problemEvaluated = problemEvaluated;
        this.outcomeValue = outcomeValue;
        valueDefined = this.outcomeValue != null;
        this.evaluationDate = evaluationDate;
        this.goalTreeNodeID = goalTreeNodeID;

    }

    public boolean isValueDefined() {
        return valueDefined;
    }

    public void setValueDefined(boolean valueDefined) {
        this.valueDefined = valueDefined;
    }

    public int getOutcomeState() {
        return outcomeState;
    }

    public void setOutcomeState(int outcomeState) {
        this.outcomeState = outcomeState;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public Double getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(Double previousValue) {
        this.previousValue = previousValue;
    }

    public boolean isProblemEvaluated() {
        return problemEvaluated;
    }

    public void setProblemEvaluated(boolean problemEvaluated) {
        this.problemEvaluated = problemEvaluated;
    }

    public Double getOutcomeValue() {
        return outcomeValue;
    }

    public void setOutcomeValue(Double outcomeValue) {
        this.outcomeValue = outcomeValue;
    }

    public Date getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(Date evaluationDate) {
        this.evaluationDate = evaluationDate;
    }
}
