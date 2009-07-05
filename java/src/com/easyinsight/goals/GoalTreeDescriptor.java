package com.easyinsight.goals;

import com.easyinsight.core.EIDescriptor;

/**
 * User: James Boe
 * Date: Jan 2, 2009
 * Time: 1:29:33 PM
 */
public class GoalTreeDescriptor extends EIDescriptor {
    private int role;
    private String iconName;

    @Override
    public int getType() {
        return EIDescriptor.GOAL_TREE;
    }

    public GoalTreeDescriptor() {
    }

    public GoalTreeDescriptor(long id, String name, int role, String iconName) {
        super(name, id);
        this.role = role;
        this.iconName = iconName;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
