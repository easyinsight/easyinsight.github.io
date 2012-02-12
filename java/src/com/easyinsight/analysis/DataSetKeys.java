package com.easyinsight.analysis;

import com.easyinsight.core.Key;

import java.util.*;

/**
 * User: jamesboe
 * Date: 3/1/11
 * Time: 10:58 AM
 */
public class DataSetKeys {
    private Map<Key, Short> keyMap = new HashMap<Key, Short>();
    private short max;
    private List<Key> keys = new ArrayList<Key>();

    public void replaceKey(Key source, Key target) {
        short position = keyMap.get(source);
        keyMap.put(target, position);
    }

    public Short getKey(Key key) {
        Short position = keyMap.get(key);
        if (position == null) {
            position = max;
            keyMap.put(key, position);
            max++;
            keys.add(key);
        }
        return position;
    }

    public Short getKey(AnalysisItem analysisItem) {
        Short position = keyMap.get(analysisItem.getKey());
        if (position == null) {
            position = keyMap.get(analysisItem.createAggregateKey());
        }
        if (position == null) {
            position = max;
            keyMap.put(analysisItem.createAggregateKey(), position);
            keys.add(analysisItem.createAggregateKey());
            max++;
        }
        return position;
    }

    public Collection<Key> getKeys() {
        return keys;
    }
}
