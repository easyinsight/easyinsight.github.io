package com.easyinsight.analysis;

import com.easyinsight.core.NamedKey;
import com.easyinsight.core.Key;

import java.util.List;

/**
 * User: James Boe
 * Date: Aug 30, 2008
 * Time: 9:49:45 PM
 */
public class AggregateKey extends NamedKey {
    private int type;
    private Key key;
    private int columnIdentifier;
    private List<FilterDefinition> filters;

    public AggregateKey(Key key, int type, List<FilterDefinition> filters) {
        super(key.toKeyString());
        this.key = key;
        this.type = type;
        this.filters = filters;
    }

    public Key underlyingKey() {
        return key;
    }

    public int aggregationType() {
        return type;
    }

    @Override
    public Key toBaseKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AggregateKey that = (AggregateKey) o;

        if (type != that.type) return false;
        if (filters != null ? !filters.equals(that.filters) : that.filters != null) return false;
        if (!key.equals(that.key)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + type;
        result = 31 * result + key.hashCode();
        result = 31 * result + (filters != null ? filters.hashCode() : 0);
        return result;
    }
}
