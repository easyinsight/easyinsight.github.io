package com.easyinsight.goals;

import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Oct 23, 2008
 * Time: 2:44:25 PM
 */
public class GoalTree {

    private GoalTreeNode rootNode;

    private String name;

    private String description;

    private long goalTreeID;

    public GoalTreeNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(GoalTreeNode rootNode) {
        this.rootNode = rootNode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getGoalTreeID() {
        return goalTreeID;
    }

    public void setGoalTreeID(long goalTreeID) {
        this.goalTreeID = goalTreeID;
    }
}
