package com.easyinsight.analysis;

/**
 * User: jamesboe
 * Date: 8/12/13
 * Time: 5:57 PM
 */
public class AdvancedFilterProperties {
    private String table;
    private String key;

    public AdvancedFilterProperties(String table, String key) {
        this.table = table;
        this.key = key;
    }

    public String getTable() {
        return table;
    }

    public String getKey() {
        return key;
    }
}
