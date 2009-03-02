package com.easyinsight.analysis;

import com.easyinsight.core.Key;

/**
 * User: James Boe
 * Date: Feb 27, 2009
 * Time: 7:59:36 PM
 */
public class AggregateDateKey extends AggregateKey {

    private int level;

    public AggregateDateKey(Key key, int type, int level) {
        super(key, type);
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AggregateDateKey that = (AggregateDateKey) o;

        return level == that.level;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + level;
        return result;
    }
}
