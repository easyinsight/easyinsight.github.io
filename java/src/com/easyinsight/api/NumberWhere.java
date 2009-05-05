package com.easyinsight.api;

import org.jetbrains.annotations.Nullable;

/**
 * User: James Boe
 * Date: Jan 20, 2009
 * Time: 11:11:14 AM
 */
public class NumberWhere {
    private String key;
    private Comparison comparison;
    private double value;

    @Nullable
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Nullable
    public Comparison getComparison() {
        return comparison;
    }

    public void setComparison(Comparison comparison) {
        this.comparison = comparison;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
