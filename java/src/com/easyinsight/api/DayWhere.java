package com.easyinsight.api;

import org.jetbrains.annotations.Nullable;

/**
 * User: James Boe
 * Date: Mar 6, 2009
 * Time: 11:46:19 AM
 */
public class DayWhere {
    private String key;
    private int year;
    private int dayOfYear;

    @Nullable
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDayOfYear() {
        return dayOfYear;
    }

    public void setDayOfYear(int dayOfYear) {
        this.dayOfYear = dayOfYear;
    }
}
