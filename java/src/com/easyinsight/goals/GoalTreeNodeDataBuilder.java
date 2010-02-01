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

        goalTreeNodeData.setIconImage(goalTreeNode.getIconImage());
        goalTreeNodeData.setDescription(goalTreeNode.getDescription());
        goalTreeNodeData.setGoalTreeNodeID(goalTreeNode.getGoalTreeNodeID());
        goalTreeNodeData.setName(goalTreeNode.getName());
        goalTreeNodeData.setTags(goalTreeNode.getTags());
        goalTreeNodeData.setSubTreeID(goalTreeNode.getSubTreeID());
        goalTreeNodeData.setSubTreeName(goalTreeNode.getSubTreeName());
        
        goalTreeNodeData.setKpi(goalTreeNode.getKpi());

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
