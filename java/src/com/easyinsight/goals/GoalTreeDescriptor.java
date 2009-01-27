package com.easyinsight.goals;

/**
 * User: James Boe
 * Date: Jan 2, 2009
 * Time: 1:29:33 PM
 */
public class GoalTreeDescriptor {
    private long goalTreeID;
    private String goalTreeName;
    private int role;

    public GoalTreeDescriptor() {
    }

    public GoalTreeDescriptor(long goalTreeID, String goalTreeName, int role) {

        this.goalTreeID = goalTreeID;
        this.goalTreeName = goalTreeName;
        this.role = role;
    }

    public long getGoalTreeID() {
        return goalTreeID;
    }

    public void setGoalTreeID(long goalTreeID) {
        this.goalTreeID = goalTreeID;
    }

    public String getGoalTreeName() {
        return goalTreeName;
    }

    public void setGoalTreeName(String goalTreeName) {
        this.goalTreeName = goalTreeName;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
