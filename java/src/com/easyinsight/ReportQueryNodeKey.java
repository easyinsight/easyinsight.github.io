package com.easyinsight;

import com.easyinsight.datafeeds.QueryNodeKey;

/**
 * User: jamesboe
 * Date: 2/11/13
 * Time: 2:38 PM
 */
public class ReportQueryNodeKey extends QueryNodeKey {
    private long id;

    public ReportQueryNodeKey(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportQueryNodeKey that = (ReportQueryNodeKey) o;

        return id == that.id;

    }

    public long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
