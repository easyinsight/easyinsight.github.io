package com.easyinsight.dataset;

import com.easyinsight.core.Key;
import com.easyinsight.core.Value;

import java.util.Map;
import java.util.LinkedHashMap;

/**
 * User: jboe
* Date: Dec 22, 2007
* Time: 10:34:55 AM
*/
class DataKey {
    private Map<Key, Value> rowKey = new LinkedHashMap<Key, Value>();
    private Map<Key, Value> columnKey = new LinkedHashMap<Key, Value>();

    public void addRowKeyPair(Key rowKeyName, Value rowValue) {
        rowKey.put(rowKeyName, rowValue);
    }

    public void addColumnKeyPair(Key columnKeyName, Value columnValue) {
        columnKey.put(columnKeyName, columnValue);
    }

    public Map<Key, Value> getRowKey() {
        return rowKey;
    }

    public void setRowKey(Map<Key, Value> rowKey) {
        this.rowKey = rowKey;
    }

    public Map<Key, Value> getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(Map<Key, Value> columnKey) {
        this.columnKey = columnKey;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataKey dataKey = (DataKey) o;

        if (!columnKey.equals(dataKey.columnKey)) return false;
        return rowKey.equals(dataKey.rowKey);

    }

    public int hashCode() {
        int result;
        result = rowKey.hashCode();
        result = 31 * result + columnKey.hashCode();
        return result;
    }
}
