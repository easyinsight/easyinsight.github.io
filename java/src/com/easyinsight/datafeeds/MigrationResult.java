package com.easyinsight.datafeeds;

import com.easyinsight.core.Key;

import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 9/9/11
 * Time: 11:18 AM
 */
public class MigrationResult {
    private boolean changed;
    private Map<String, Key> keyMap;
    private List<FieldChange> fieldChanges;
    private boolean fieldsAdded;
    private boolean fieldsRenamed;

    public MigrationResult(boolean changed, Map<String, Key> keyMap, List<FieldChange> fieldChanges, boolean fieldsAdded,
                           boolean fieldsRenamed) {
        this.changed = changed;
        this.keyMap = keyMap;
        this.fieldChanges = fieldChanges;
        this.fieldsAdded = fieldsAdded;
        this.fieldsRenamed = fieldsRenamed;
    }

    public List<FieldChange> getFieldChanges() {
        return fieldChanges;
    }

    public boolean isFieldsAdded() {
        return fieldsAdded;
    }

    public boolean isFieldsRenamed() {
        return fieldsRenamed;
    }

    public boolean isChanged() {
        return changed;
    }

    public Map<String, Key> getKeyMap() {
        return keyMap;
    }
}
