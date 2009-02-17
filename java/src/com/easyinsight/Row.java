package com.easyinsight;

import com.easyinsight.core.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.io.Serializable;

/**
 * User: jboe
 * Date: Dec 22, 2007
 * Time: 12:07:59 PM
 */
public class Row implements IRow, Serializable {

    private Map<Key, Value> valueMap;

    public Row() {
        valueMap = new HashMap<Key, Value>();
    }

    public Row(int size) {
        valueMap = new HashMap<Key, Value>(size);
    }

    public Value getValue(Key rowName) {
        Value value = valueMap.get(rowName);
        if (value == null) {
            value = new EmptyValue();
        }
        return value;
    }

    public void addValue(String tag, Value value) {
        addValue(new NamedKey(tag), value);
    }

    public void addValue(Key tag, Value value) {
        valueMap.put(tag, value);
    }

    public void addValue(String tag, String value) {
        valueMap.put(new NamedKey(tag), new StringValue(value));
    }

    public void addValue(Key tag, String value) {
        if (value == null) {
            valueMap.put(tag, new EmptyValue());
        } else {
            valueMap.put(tag, new StringValue(value));
        }
    }

    public Collection<Key> getKeys() {
        return valueMap.keySet();
    }

    public void addValues(Map<Key, Value> valueMap) {
        this.valueMap.putAll(valueMap);
    }

    public Map<Key, Value> getValues() {
        return this.valueMap;
    }

    public void replaceKey(Key existingKey, Key newKey) {
        Value existing = valueMap.remove(existingKey);
        if (existing != null) {
            valueMap.put(newKey, existing);
        }
    }

    public IRow merge(IRow row) {
        Row otherRow = (Row) row;
        Row mergedRow = new Row();
        mergedRow.valueMap.putAll(valueMap);
        mergedRow.valueMap.putAll(otherRow.valueMap);
        return mergedRow;
    }

    public String toString() {
        return valueMap.toString();
    }
}
