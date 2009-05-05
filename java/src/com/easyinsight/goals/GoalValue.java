package com.easyinsight.goals;

import java.util.Date;

/**
 * User: James Boe
 * Date: Jan 4, 2009
 * Time: 2:14:36 PM
 */
public class GoalValue {
    private long goalTreeNodeID;
    private Date date;
    private double value;

    public GoalValue() {
    }

    public GoalValue(long goalTreeNodeID, Date date, double value) {
        this.goalTreeNodeID = goalTreeNodeID;
        this.date = date;
        this.value = value;
    }

    public long getGoalTreeNodeID() {
        return goalTreeNodeID;
    }

    public void setGoalTreeNodeID(long goalTreeNodeID) {
        this.goalTreeNodeID = goalTreeNodeID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return date.toString() + " - " + value;
    }
}
