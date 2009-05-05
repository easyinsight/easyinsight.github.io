package com.easyinsight.api;

import org.jetbrains.annotations.Nullable;

/**
 * User: James Boe
 * Date: Jan 19, 2009
 * Time: 10:59:38 PM
 */
public class StringWhere {
    private String key;
    private String value;

    @Nullable    
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Nullable
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
