package com.easyinsight.solutions;

/**
 * User: James Boe
* Date: Feb 23, 2009
* Time: 11:25:44 AM
*/
public class SolutionElementKey {

    public static final int DATA_SOURCE = 1;
    public static final int INSIGHT = 2;

    private int type;
    private long id;

    public SolutionElementKey(int type, long id) {
        this.type = type;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SolutionElementKey that = (SolutionElementKey) o;

        return id == that.id && type == that.type;

    }

    @Override
    public int hashCode() {
        int result = type;
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }
}
