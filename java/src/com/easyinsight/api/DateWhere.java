package com.easyinsight.api;

import java.util.Date;

/**
 * User: James Boe
 * Date: Jan 20, 2009
 * Time: 11:11:19 AM
 */
public class DateWhere {
    private String key;
    private Comparison comparison;
    private Date value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Comparison getComparison() {
        return comparison;
    }

    public void setComparison(Comparison comparison) {
        this.comparison = comparison;
    }

    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }
}
