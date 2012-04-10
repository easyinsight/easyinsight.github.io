package com.easyinsight.analysis;

import com.easyinsight.core.Value;

import java.util.Collection;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 3/12/12
 * Time: 2:06 PM
 */
public class ActualRow {
    private Map<String, Value> values;
    private long rowID;

    public Map<String, Value> getValues() {
        return values;
    }

    public void setValues(Map<String, Value> values) {
        this.values = values;
    }

    public long getRowID() {
        return rowID;
    }

    public void setRowID(long rowID) {
        this.rowID = rowID;
    }
}
