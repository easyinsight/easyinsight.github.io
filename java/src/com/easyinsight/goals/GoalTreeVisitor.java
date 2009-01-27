package com.easyinsight.goals;

/**
 * User: James Boe
 * Date: Jan 4, 2009
 * Time: 1:58:14 PM
 */
public abstract class GoalTreeVisitor {
    public void visit(GoalTreeNode goalTreeNode) {
        accept(goalTreeNode);
        if (goalTreeNode.getChildren() != null) {
            for (GoalTreeNode childNode : goalTreeNode.getChildren()) {
                visit(childNode);
            }
        }
    }

    protected abstract void accept(GoalTreeNode goalTreeNode);
}
