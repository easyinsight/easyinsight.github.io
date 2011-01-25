package com.easyinsight.analysis;

import com.easyinsight.core.Key;

import java.util.List;

/**
 * User: jamesboe
 * Date: Feb 4, 2010
 * Time: 10:38:30 AM
 */
public class AggregateMeasureKey extends AggregateKey {
    private int aggregation;
    private String displayName;

    public AggregateMeasureKey(Key key, int type, int aggregation, String displayName, List<FilterDefinition> filters) {
        super(key, type, filters);
        this.aggregation = aggregation;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
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
        if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + aggregation;
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return displayName == null ? super.toString() : displayName;
    }
}
