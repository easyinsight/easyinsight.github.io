package com.easyinsight.api;

import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * User: James Boe
 * Date: Jan 15, 2009
 * Time: 2:37:40 PM
 */
public class DatePair {
    private String key;
    private Date value;

    @Nullable    
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Nullable    
    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }
}
