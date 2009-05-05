package com.easyinsight.api;

import org.jetbrains.annotations.Nullable;

/**
 * User: James Boe
 * Date: Jan 15, 2009
 * Time: 2:37:37 PM
 */
public class NumberPair {
    private String key;
    private double value;

    @Nullable
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
