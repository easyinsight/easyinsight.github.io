package com.easyinsight.datafeeds;

/**
* User: jamesboe
* Date: 2/11/13
* Time: 2:38 PM
*/
public class DataSourceQueryNodeKey extends QueryNodeKey {
    private long id;

    public DataSourceQueryNodeKey(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataSourceQueryNodeKey that = (DataSourceQueryNodeKey) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
