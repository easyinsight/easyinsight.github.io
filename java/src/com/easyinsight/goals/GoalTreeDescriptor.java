package com.easyinsight.goals;

import com.easyinsight.core.EIDescriptor;

/**
 * User: James Boe
 * Date: Jan 2, 2009
 * Time: 1:29:33 PM
 */
public class GoalTreeDescriptor extends EIDescriptor {
    private String iconName;
    private long dataSourceID;

    @Override
    public int getType() {
        return EIDescriptor.GOAL_TREE;
    }

    public GoalTreeDescriptor() {
    }

    public GoalTreeDescriptor(long id, String name, int role, String iconName, String urlKey, long dataSourceID) {
        super(name, id, urlKey, false);
        setRole(role);
        this.iconName = iconName;
        this.dataSourceID = dataSourceID;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
}
