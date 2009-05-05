package com.easyinsight.api;

import org.jetbrains.annotations.Nullable;

/**
 * User: James Boe
 * Date: Aug 28, 2008
 * Time: 11:07:06 PM
 */
public class StringPair {
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
