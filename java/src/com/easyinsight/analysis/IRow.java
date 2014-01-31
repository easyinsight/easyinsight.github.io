package com.easyinsight.analysis;

import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.Collection;
import java.util.Map;
import java.util.Date;
import java.util.Set;

/**
 * User: jboe
* Date: Dec 22, 2007
* Time: 10:35:33 AM
*/
public interface IRow {

    Map<String, Set<Value>> getPassthroughRow();

    void incrementJoinCount();

    int getJoinCount();

    void resetJoinCount();

    void setPassthroughRow(Map<String, Set<Value>> passthroughRow);

    public Row clone() throws CloneNotSupportedException;

    Value getValue(Key rowName);

    public DataSetKeys getDataSetKeys();

    void setDataSetKeys(DataSetKeys dataSetKeys);

    Value getValue(AnalysisItem analysisItem);

    public Value getValueNoAdd(Key rowName);

    void addValue(Key tag, Value value);

    void addValue(String tag, String value);

    void addValue(Key tag, String value);
    
    void addValue(String tag, Value value);

    void addValue(Key tag, Number value);

    void addValue(Key tag, Date value);

    IRow merge(IRow row, DataSet dataSet);

    Collection<Key> getKeys();

    void addValues(IRow row);

    public void addValues(Map<Key, Value> valueMap);

    Map<Key, Value> getValues();

    void replaceKey(Key existingKey, Key newKey);

    void removeValue(Key key);

    void addValue(String s, Number value);

    boolean isMarked();

    void setMarked(boolean marked);
}
