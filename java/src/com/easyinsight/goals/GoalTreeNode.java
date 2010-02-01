package com.easyinsight.goals;

import com.easyinsight.analysis.Tag;
import com.easyinsight.kpi.KPI;
import com.easyinsight.solutions.SolutionGoalTreeDescriptor;

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

    private List<Tag> tags = new ArrayList<Tag>();

    private String name;
    private String description;
    private String iconImage;

    private long subTreeID;
    private String subTreeName;
    private String subTreeIcon;
    private SolutionGoalTreeDescriptor newSubTree;

    private KPI kpi;

    public KPI getKpi() {
        return kpi;
    }

    public void setKpi(KPI kpi) {
        this.kpi = kpi;
    }

    public String getSubTreeIcon() {
        return subTreeIcon;
    }

    public void setSubTreeIcon(String subTreeIcon) {
        this.subTreeIcon = subTreeIcon;
    }

    public String getSubTreeName() {
        return subTreeName;
    }

    public void setSubTreeName(String subTreeName) {
        this.subTreeName = subTreeName;
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

    /*public List<Integer> getUsers() {
        return users;
    }

    public void setUsers(List<Integer> users) {
        this.users = users;
    }*/

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }



    public long getGoalTreeNodeID() {
        return goalTreeNodeID;
    }

    public void setGoalTreeNodeID(long goalTreeNodeID) {
        this.goalTreeNodeID = goalTreeNodeID;
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
