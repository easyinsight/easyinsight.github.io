package com.easyinsight.core;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * User: James Boe
 * Date: Jul 1, 2008
 * Time: 11:01:28 AM
 */
public class StringValue extends Value implements Serializable {    

    private String value;
    private static final long serialVersionUID = -3662307504638205531L;

    public StringValue() {
    }

    @Override
    public String toString() {
        return value;
    }

    public StringValue(String value) {
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public int type() {
        return Value.STRING;
    }

    @Nullable
    public Double toDouble() {
        try {
            return NumericValue.produceDoubleValue(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringValue that = (StringValue) o;

        return value.equals(that.value);

    }

    public int hashCode() {
        return value.hashCode();
    }
}
