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
        goalTreeNodeData.setFilterDefinition(goalTreeNode.getFilterDefinition());
        goalTreeNodeData.setGoalTreeNodeID(goalTreeNode.getGoalTreeNodeID());
        goalTreeNodeData.setGoalValue(goalTreeNode.getGoalValue());
        goalTreeNodeData.setHighIsGood(goalTreeNode.isHighIsGood());
        goalTreeNodeData.setName(goalTreeNode.getName());
        goalTreeNodeData.setTags(goalTreeNode.getTags());
        List<GoalTreeNode> newChildren = new ArrayList<GoalTreeNode>();
        for (GoalTreeNode child : goalTreeNode.getChildren()) {
            GoalTreeNodeData childData = build(child);
            newChildren.add(childData);
            childData.setParent(goalTreeNode);
        }
        goalTreeNodeData.setChildren(newChildren);
        return goalTreeNodeData;
    }
}
