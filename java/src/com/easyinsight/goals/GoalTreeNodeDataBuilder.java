package com.easyinsight.goals;

import java.util.ArrayList;
import java.util.List;

/**
 * User: James Boe
 * Date: Jan 4, 2009
 * Time: 4:03:11 PM
 */
public class GoalTreeNodeDataBuilder {
    public GoalTreeNodeData build(GoalTreeNode goalTreeNode) {
        GoalTreeNodeData goalTreeNodeData = new GoalTreeNodeData();
        goalTreeNodeData.setAnalysisMeasure(goalTreeNode.getAnalysisMeasure());
        goalTreeNodeData.setIconImage(goalTreeNode.getIconImage());
        goalTreeNodeData.setAssociatedFeeds(goalTreeNode.getAssociatedFeeds());
        goalTreeNodeData.setAssociatedInsights(goalTreeNode.getAssociatedInsights());
        goalTreeNodeData.setAssociatedSolutions(goalTreeNode.getAssociatedSolutions());
        goalTreeNodeData.setCoreFeedID(goalTreeNode.getCoreFeedID());
        goalTreeNodeData.setCoreFeedName(goalTreeNode.getCoreFeedName());
        goalTreeNodeData.setDescription(goalTreeNode.getDescription());
        goalTreeNodeData.setFilters(goalTreeNode.getFilters());
        goalTreeNodeData.setProblemConditions(goalTreeNode.getProblemConditions());
        goalTreeNodeData.setGoalTreeNodeID(goalTreeNode.getGoalTreeNodeID());
        goalTreeNodeData.setGoalValue(goalTreeNode.getGoalValue());
        goalTreeNodeData.setHighIsGood(goalTreeNode.isHighIsGood());
        goalTreeNodeData.setName(goalTreeNode.getName());
        goalTreeNodeData.setTags(goalTreeNode.getTags());
        goalTreeNodeData.setSubTreeID(goalTreeNode.getSubTreeID());
        goalTreeNodeData.setSubTreeName(goalTreeNode.getSubTreeName());
        goalTreeNodeData.setMilestone(goalTreeNode.getMilestone());
        goalTreeNodeData.setGoalDefined(goalTreeNode.isGoalDefined());
        goalTreeNodeData.setMeasureLabel(goalTreeNode.getMeasureLabel());
        List<GoalTreeNode> newChildren = new ArrayList<GoalTreeNode>();
        for (GoalTreeNode child : goalTreeNode.getChildren()) {
            GoalTreeNodeData childData = build(child);
            newChildren.add(childData);
            childData.setParent(goalTreeNode);
        }
        if (goalTreeNodeData.getSubTreeID() > 0) {
            
        }
        goalTreeNodeData.setChildren(newChildren);
        return goalTreeNodeData;
    }
}
