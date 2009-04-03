package com.easyinsight.goals;

import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.Tag;
import com.easyinsight.solutions.SolutionGoalTreeDescriptor;
import com.easyinsight.core.InsightDescriptor;

import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Oct 23, 2008
 * Time: 3:02:04 PM
 */
public class GoalTreeNode implements Cloneable {

    public static final int ABSTRACT_NODE = 0;
    public static final int SPECIFICATION = 1;
    public static final int CONCRETE_NODE = 2;

    private long goalTreeNodeID;
    private List<GoalTreeNode> children = new ArrayList<GoalTreeNode>();
    private GoalTreeNode parent;
    private long coreFeedID;
    private String coreFeedName;
    private AnalysisMeasure analysisMeasure;
    private double goalValue;
    private boolean highIsGood;
    private FilterDefinition filterDefinition;
    private List<GoalFeed> associatedFeeds = new ArrayList<GoalFeed>();
    private List<InsightDescriptor> associatedInsights = new ArrayList<InsightDescriptor>();
    private List<GoalSolution> associatedSolutions = new ArrayList<GoalSolution>();
    private List<Tag> tags = new ArrayList<Tag>();
    private String name;
    private String description;
    private String iconImage;
    private List<Integer> users = new ArrayList<Integer>();
    private GoalTreeMilestone milestone;
    private long subTreeID;
    private SolutionGoalTreeDescriptor newSubTree;

    public GoalTreeMilestone getMilestone() {
        return milestone;
    }

    public void setMilestone(GoalTreeMilestone milestone) {
        this.milestone = milestone;
    }

    public SolutionGoalTreeDescriptor getNewSubTree() {
        return newSubTree;
    }

    public void setNewSubTree(SolutionGoalTreeDescriptor newSubTree) {
        this.newSubTree = newSubTree;
    }

    public long getSubTreeID() {
        return subTreeID;
    }

    public void setSubTreeID(long subTreeID) {
        this.subTreeID = subTreeID;
    }

    public List<Integer> getUsers() {
        return users;
    }

    public void setUsers(List<Integer> users) {
        this.users = users;
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

    public List<InsightDescriptor> getAssociatedInsights() {
        return associatedInsights;
    }

    public void setAssociatedInsights(List<InsightDescriptor> associatedInsights) {
        this.associatedInsights = associatedInsights;
    }

    public List<GoalSolution> getAssociatedSolutions() {
        return associatedSolutions;
    }

    public void setAssociatedSolutions(List<GoalSolution> associatedSolutions) {
        this.associatedSolutions = associatedSolutions;
    }

    @Override
    public GoalTreeNode clone() throws CloneNotSupportedException {
        GoalTreeNode node = (GoalTreeNode) super.clone();
        node.setGoalTreeNodeID(0);
        List<GoalTreeNode> cloneChildren = new ArrayList<GoalTreeNode>();
        for (GoalTreeNode child : getChildren()) {
            GoalTreeNode clonedChild = child.clone();
            cloneChildren.add(clonedChild);
            clonedChild.setParent(node);
        }
        node.setChildren(cloneChildren);
        return node;
    }
}
