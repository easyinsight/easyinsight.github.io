package com.easyinsight.goals;

import com.easyinsight.core.EIDescriptor;

/**
 * User: James Boe
 * Date: Jan 2, 2009
 * Time: 1:29:33 PM
 */
public class GoalTreeDescriptor extends EIDescriptor {
    private int role;

    public GoalTreeDescriptor() {
    }

    public GoalTreeDescriptor(long id, String name, int role) {
        super(name, id);
        this.role = role;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
