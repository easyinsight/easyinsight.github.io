package com.easyinsight.analysis;

/**
 * User: jamesboe
 * Date: 6/25/13
 * Time: 6:32 PM
 */
public class UniqueKey {

    public static final int DERIVED = 1;
    public static final int REPORT = 2;

    private long id;
    private int type;

    public UniqueKey(long id, int type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UniqueKey uniqueKey = (UniqueKey) o;

        if (id != uniqueKey.id) return false;
        if (type != uniqueKey.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + type;
        return result;
    }
}
