package com.easyinsight.core;

/**
 * User: James Boe
 * Date: Mar 28, 2009
 * Time: 8:30:25 AM
 */
public abstract class EIDescriptor {

    public static final int DATA_SOURCE = 1;
    public static final int REPORT = 2;
    public static final int GROUP = 3;
    public static final int GOAL_TREE = 4;
    public static final int SOLUTION = 5;

    private String name;
    private long id;

    public abstract int getType();

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

    public EIDescriptor() {
    }

    public EIDescriptor(String name, long id) {
        this.name = name;
        this.id = id;
    }
}
