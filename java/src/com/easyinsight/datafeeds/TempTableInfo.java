package com.easyinsight.datafeeds;

import java.util.Map;

/**
 * User: jamesboe
 * Date: 9/4/11
 * Time: 1:09 PM
 */
public class TempTableInfo {
    private Map<Long, String> tableNames;
    private boolean changed;

    public Map<Long, String> getTableNames() {
        return tableNames;
    }

    public boolean isChanged() {
        return changed;
    }

    public TempTableInfo(Map<Long, String> tableNames, boolean changed) {

        this.tableNames = tableNames;
        this.changed = changed;
    }
}
