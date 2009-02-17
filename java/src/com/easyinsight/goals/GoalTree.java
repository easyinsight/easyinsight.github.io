package com.easyinsight.goals;

import com.easyinsight.datafeeds.FeedConsumer;

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

    private List<FeedConsumer> administrators = new ArrayList<FeedConsumer>();

    private List<FeedConsumer> consumers = new ArrayList<FeedConsumer>();

    private long goalTreeID;

    public List<FeedConsumer> getAdministrators() {
        return administrators;
    }

    public void setAdministrators(List<FeedConsumer> administrators) {
        this.administrators = administrators;
    }

    public List<FeedConsumer> getConsumers() {
        return consumers;
    }

    public void setConsumers(List<FeedConsumer> consumers) {
        this.consumers = consumers;
    }

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
