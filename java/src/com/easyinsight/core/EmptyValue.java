package com.easyinsight.core;

import org.jetbrains.annotations.Nullable;
import com.easyinsight.logging.LogClass;

/**
 * User: James Boe
 * Date: Jul 1, 2008
 * Time: 1:15:44 PM
 */
public class EmptyValue extends Value {
    private static final long serialVersionUID = -3929345976490773936L;

    private String blah = "";

    public static EmptyValue EMPTY_VALUE = new EmptyValue();

    public int type() {
        return Value.EMPTY;
    }

    @Nullable
    public Double toDouble() {
        return null;
    }

    /*public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass());
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmptyValue that = (EmptyValue) o;

        if (!blah.equals(that.blah)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return blah.hashCode();
    }

    @Override
    public String toString() {
        return "(Empty)";
    }
}
