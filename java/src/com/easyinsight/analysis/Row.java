package com.easyinsight.analysis;

import com.easyinsight.core.*;

import java.util.*;
import java.io.Serializable;

/**
 * User: jboe
 * Date: Dec 22, 2007
 * Time: 12:07:59 PM
 */
public class Row implements IRow, Serializable {

    private Value[] valueMap;

    private DataSetKeys dataSetKeys;

    public Row(DataSetKeys dataSetKeys) {
        valueMap = new Value[1];
        this.dataSetKeys = dataSetKeys;
    }

    public DataSetKeys getDataSetKeys() {
        return dataSetKeys;
    }

    public Row(int size, DataSetKeys dataSetKeys) {
        valueMap = new Value[size];
        this.dataSetKeys = dataSetKeys;
    }

    public Value getValue(Key rowName) {
        int key = dataSetKeys.getKey(rowName);
        if (key >= valueMap.length) {
            return EmptyValue.EMPTY_VALUE;
        }
        return valueMap[key];
    }

    public Value getValue(AnalysisItem analysisItem) {
        int key = dataSetKeys.getKey(analysisItem);
        if (key >= valueMap.length) {
            return EmptyValue.EMPTY_VALUE;
        }
        return valueMap[key];
    }

    public void addValue(String tag, Value value) {
        addValue(new NamedKey(tag), value);
    }

    public void addValue(Key tag, Date value) {
        addValue(tag, new DateValue(value));
    }

    public void addValue(Key tag, Number value) {
        addValue(tag, new NumericValue(value));
    }

    public void addValue(Key tag, Value value) {
        int key = dataSetKeys.getKey(tag);
        if (key >= valueMap.length) {
            Value[] newVals = new Value[key + 1];
            System.arraycopy(valueMap, 0, newVals, 0, valueMap.length);
            valueMap = newVals;
        }
        valueMap[key] = value;
    }

    public void addValue(String tag, String value) {
        addValue(new NamedKey(tag), new StringValue(value));
    }

    public void addValue(Key tag, String value) {
        if (value == null) {
            addValue(tag, new EmptyValue());
        } else {
            addValue(tag, new StringValue(value));
        }
    }

    public Collection<Key> getKeys() {
        return dataSetKeys.getKeys();
    }

    public void addValues(Map<Key, Value> valueMap) {
        for (Map.Entry<Key, Value> entry : valueMap.entrySet()) {
            addValue(entry.getKey(), entry.getValue());
        }
    }

    public void addValues(IRow row) {
        for (Key key : row.getKeys()) {
            Value value = row.getValue(key);
            if (value != null) {
                addValue(key, value);
            }
        }
    }

    public Map<Key, Value> getValues() {
        Map<Key, Value> values = new HashMap<Key, Value>();
        for (Key key : dataSetKeys.getKeys()) {
            Value value = getValue(key);
            if (value != null) {
                values.put(key, value);
            }
        }
        return values;
    }

    public void replaceKey(Key existingKey, Key newKey) {
        Value existing = getValue(existingKey);
        if (existing != null) {
            addValue(newKey, existing);
        }
    }

    public void removeValue(Key key) {
        int position = dataSetKeys.getKey(key);
        if (position < valueMap.length) {
            valueMap[position] = null;
        }
    }

    public void addValue(String s, Number value) {
        addValue(new NamedKey(s), new NumericValue(value));
    }

    public IRow merge(IRow row) {
        Row otherRow = (Row) row;
        Row mergedRow = new Row(dataSetKeys);
        for (Key key : dataSetKeys.getKeys()) {
            Value value = getValue(key);
            if (value != null) {
                mergedRow.addValue(key, value);
            }
        }
        for (Key key : otherRow.dataSetKeys.getKeys()) {
            Value value = otherRow.getValue(key);
            if (value != null) {
                mergedRow.addValue(key, value);
            }
        }
        return mergedRow;
    }

    public String toString() {
        Map<Key, Value> map = new HashMap<Key, Value>();
        for (Key key : dataSetKeys.getKeys()) {
            map.put(key, getValue(key));
        }
        return map.toString();
    }
}
