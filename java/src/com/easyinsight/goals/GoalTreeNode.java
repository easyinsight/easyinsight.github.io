package com.easyinsight.goals;

import com.easyinsight.AnalysisMeasure;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.Tag;

import java.util.List;

/**
 * User: James Boe
 * Date: Oct 23, 2008
 * Time: 3:02:04 PM
 */
public class GoalTreeNode {

    public static final int ABSTRACT_NODE = 0;
    public static final int SPECIFICATION = 1;
    public static final int CONCRETE_NODE = 2;

    private long goalTreeNodeID;
    private List<GoalTreeNode> children;
    private GoalTreeNode parent;
    private long coreFeedID;
    private String coreFeedName;
    private AnalysisMeasure analysisMeasure;
    private double goalValue;
    private boolean highIsGood;
    private FilterDefinition filterDefinition;
    private List<GoalFeed> associatedFeeds;
    private List<GoalInsight> associatedInsights;
    private List<GoalSolution> associatedSolutions;
    private List<Tag> tags;
    private String name;
    private String description;
    private String iconImage;
    private List<Integer> newSolutions;

    public List<Integer> getNewSolutions() {
        return newSolutions;
    }

    public void setNewSolutions(List<Integer> newSolutions) {
        this.newSolutions = newSolutions;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public boolean isHighIsGood() {
        return highIsGood;
    }

    public void setHighIsGood(boolean highIsGood) {
        this.highIsGood = highIsGood;
    }

    public long getGoalTreeNodeID() {
        return goalTreeNodeID;
    }

    public void setGoalTreeNodeID(long goalTreeNodeID) {
        this.goalTreeNodeID = goalTreeNodeID;
    }

    public FilterDefinition getFilterDefinition() {
        return filterDefinition;
    }

    public void setFilterDefinition(FilterDefinition filterDefinition) {
        this.filterDefinition = filterDefinition;
    }

    public long getCoreFeedID() {
        return coreFeedID;
    }

    public void setCoreFeedID(long coreFeedID) {
        this.coreFeedID = coreFeedID;
    }

    public String getCoreFeedName() {
        return coreFeedName;
    }

    public void setCoreFeedName(String coreFeedName) {
        this.coreFeedName = coreFeedName;
    }

    public AnalysisMeasure getAnalysisMeasure() {
        return analysisMeasure;
    }

    public void setAnalysisMeasure(AnalysisMeasure analysisMeasure) {
        this.analysisMeasure = analysisMeasure;
    }

    public double getGoalValue() {
        return goalValue;
    }

    public void setGoalValue(double goalValue) {
        this.goalValue = goalValue;
    }

    public GoalTreeNode getParent() {
        return parent;
    }

    public void setParent(GoalTreeNode parent) {
        this.parent = parent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<GoalTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<GoalTreeNode> children) {
        this.children = children;
    }

    public List<GoalFeed> getAssociatedFeeds() {
        return associatedFeeds;
    }

    public void setAssociatedFeeds(List<GoalFeed> associatedFeeds) {
        this.associatedFeeds = associatedFeeds;
    }

    public List<GoalInsight> getAssociatedInsights() {
        return associatedInsights;
    }

    public void setAssociatedInsights(List<GoalInsight> associatedInsights) {
        this.associatedInsights = associatedInsights;
    }

    public List<GoalSolution> getAssociatedSolutions() {
        return associatedSolutions;
    }

    public void setAssociatedSolutions(List<GoalSolution> associatedSolutions) {
        this.associatedSolutions = associatedSolutions;
    }
}
