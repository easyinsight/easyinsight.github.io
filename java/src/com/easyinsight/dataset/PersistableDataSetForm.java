package com.easyinsight.dataset;

import com.easyinsight.core.Key;
import com.easyinsight.IRow;

import java.util.Map;
import java.util.HashMap;

/**
 * User: James Boe
 * Date: May 11, 2008
 * Time: 11:21:40 AM
 */
public class PersistableDataSetForm {
    private Map<Key, ColumnSegment> columnSegmentMap = new HashMap<Key, ColumnSegment>();

    public PersistableDataSetForm() {
        
    }

    public PersistableDataSetForm(Map<Key, ColumnSegment> columnSegmentMap) {
        this.columnSegmentMap = columnSegmentMap;
    }

    public Map<Key, ColumnSegment> getColumnSegmentMap() {
        return columnSegmentMap;
    }

    public void refreshKey(Key key) {
        ColumnSegment columnSegment = columnSegmentMap.remove(key);
        columnSegmentMap.put(key, columnSegment);
    }

    public DataSet toDataSet() {
        DataSet dataSet = new DataSet();
        ColumnSegment columnSegment = columnSegmentMap.values().iterator().next();
        int length = columnSegment.getValues().length;
        for (int i = 0; i < length; i++) {
            IRow row = dataSet.createRow();
            for (Map.Entry<Key, ColumnSegment> entry : columnSegmentMap.entrySet()) {
                row.addValue(entry.getKey(), entry.getValue().getValues()[i]);
            }
        }
        return dataSet;
    }
}
