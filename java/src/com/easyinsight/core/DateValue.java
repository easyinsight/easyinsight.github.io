package com.easyinsight.core;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: James Boe
 * Date: Jul 1, 2008
 * Time: 11:01:23 AM
 */
public class DateValue extends Value implements Serializable {
    private Date date;
    private String format;
    private static final long serialVersionUID = 8170674055682369820L;

    public DateValue() {
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        if (date == null) {
            return "";
        }
        if (format == null) {
            return date.toString();
        } else {
            return new SimpleDateFormat(format).format(date);
        }
    }

    public DateValue(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int type() {
        return Value.DATE;
    }

    @Nullable
    public Double toDouble() {
        return null;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DateValue dateValue = (DateValue) o;

        return date.equals(dateValue.date);

    }

    public int hashCode() {
        return date.hashCode();
    }
}
