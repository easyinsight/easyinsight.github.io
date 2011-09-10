package com.easyinsight.datafeeds;

import com.easyinsight.core.Key;

import java.util.Map;

/**
 * User: jamesboe
 * Date: 9/9/11
 * Time: 11:18 AM
 */
public class MigrationResult {
    private boolean changed;
    private Map<String, Key> keyMap;

    public MigrationResult(boolean changed, Map<String, Key> keyMap) {
        this.changed = changed;
        this.keyMap = keyMap;
    }

    public boolean isChanged() {
        return changed;
    }

    public Map<String, Key> getKeyMap() {
        return keyMap;
    }
}
