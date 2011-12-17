package com.easyinsight.analysis;

import com.easyinsight.core.Key;

import java.util.List;

/**
 * User: jamesboe
 * Date: 12/14/11
 * Time: 1:25 PM
 */
public class AggregateDateKey extends AggregateKey {
    private int dateLevel;

    public AggregateDateKey(Key key, int type, int dateLevel, List<FilterDefinition> filters) {
        super(key, type, filters);
        this.dateLevel = dateLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AggregateDateKey that = (AggregateDateKey) o;

        if (dateLevel != that.dateLevel) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + dateLevel;
        return result;
    }
}
