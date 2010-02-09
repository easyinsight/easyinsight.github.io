package com.easyinsight.analysis;

import com.easyinsight.core.Key;

/**
 * User: jamesboe
 * Date: Feb 4, 2010
 * Time: 10:38:30 AM
 */
public class AggregateMeasureKey extends AggregateKey {
    private int aggregation;

    public AggregateMeasureKey(Key key, int type, int aggregation) {
        super(key, type);
        this.aggregation = aggregation;
    }

    public int getAggregation() {
        return aggregation;
    }

    public void setAggregation(int aggregation) {
        this.aggregation = aggregation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AggregateMeasureKey that = (AggregateMeasureKey) o;

        if (aggregation != that.aggregation) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + aggregation;
        return result;
    }
}
