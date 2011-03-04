package com.easyinsight.analysis;

import com.easyinsight.core.Key;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 3/1/11
 * Time: 10:58 AM
 */
public class DataSetKeys {
    private Map<Key, Short> keyMap = new HashMap<Key, Short>();
    private short max;

    public int getKey(Key key) {
        Short position = keyMap.get(key);
        if (position == null) {
            position = (short) (max + 1);
            keyMap.put(key, position);
            max++;
        }
        return position;
    }

    public int getKey(AnalysisItem analysisItem) {
        Short position = keyMap.get(analysisItem.getKey());
        if (position == null) {
            position = keyMap.get(analysisItem.createAggregateKey());
        }
        if (position == null) {
            position = (short) (max + 1);
            keyMap.put(analysisItem.createAggregateKey(), position);
            max++;
        }
        return position;
    }

    public Collection<Key> getKeys() {
        return keyMap.keySet();
    }
}
