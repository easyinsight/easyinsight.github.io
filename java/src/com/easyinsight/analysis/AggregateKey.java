package com.easyinsight.analysis;

import com.easyinsight.core.NamedKey;
import com.easyinsight.core.Key;

/**
 * User: James Boe
 * Date: Aug 30, 2008
 * Time: 9:49:45 PM
 */
public class AggregateKey extends NamedKey {
    private int type;
    private Key key;

    public AggregateKey(Key key, int type) {
        super(key.toKeyString());
        this.key = key;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AggregateKey that = (AggregateKey) o;

        return type == that.type && key.equals(that.key);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + type;
        result = 31 * result + key.hashCode();
        return result;
    }
}
