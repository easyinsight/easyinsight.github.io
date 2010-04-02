package com.easyinsight.goals;

import com.easyinsight.datafeeds.FeedConsumer;

import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Oct 23, 2008
 * Time: 2:44:25 PM
 */
public class GoalTree implements Cloneable {

    private GoalTreeNode rootNode;

    private String urlKey;

    private String name;

    private String description;

    private List<FeedConsumer> administrators = new ArrayList<FeedConsumer>();

    private List<FeedConsumer> consumers = new ArrayList<FeedConsumer>();

    private long goalTreeID;

    private List<Integer> newSolutions = new ArrayList<Integer>();

    private List<GoalTreeMilestone> milestones = new ArrayList<GoalTreeMilestone>();

    private List<GoalTreeDescriptor> subTreeParents = new ArrayList<GoalTreeDescriptor>();

    private String iconImage;

    private GoalTreeMilestone defaultMilestone;

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public GoalTreeMilestone getDefaultMilestone() {
        return defaultMilestone;
    }

    public void setDefaultMilestone(GoalTreeMilestone defaultMilestone) {
        this.defaultMilestone = defaultMilestone;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public List<GoalTreeDescriptor> getSubTreeParents() {
        return subTreeParents;
    }

    public void setSubTreeParents(List<GoalTreeDescriptor> subTreeParents) {
        this.subTreeParents = subTreeParents;
    }

    public List<GoalTreeMilestone> getMilestones() {
        return milestones;
    }

    public void setMilestones(List<GoalTreeMilestone> milestones) {
        this.milestones = milestones;
    }

    public List<Integer> getNewSolutions() {
        return newSolutions;
    }

    public void setNewSolutions(List<Integer> newSolutions) {
        this.newSolutions = newSolutions;
    }

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

    @Override
    public GoalTree clone() throws CloneNotSupportedException {
        GoalTree goalTree = (GoalTree) super.clone();
        goalTree.setRootNode(rootNode.clone());
        goalTree.setGoalTreeID(0);
        goalTree.setAdministrators(new ArrayList<FeedConsumer>());
        goalTree.setConsumers(new ArrayList<FeedConsumer>());
        goalTree.setUrlKey(null);
        return goalTree;
    }
}
