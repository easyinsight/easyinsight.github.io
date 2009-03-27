package com.easyinsight.goals;

/**
 * User: James Boe
 * Date: Mar 27, 2009
 * Time: 10:42:18 AM
 */
public class GoalDescriptor {
    private String name;
    private long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GoalDescriptor() {
    }

    public GoalDescriptor(String name, long id) {
        this.name = name;
        this.id = id;
    }
}
