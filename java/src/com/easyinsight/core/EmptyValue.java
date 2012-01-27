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

    private int blah = 0;

    public static EmptyValue EMPTY_VALUE = new EmptyValue();

    public int type() {
        return Value.EMPTY;
    }

    @Nullable
    public Double toDouble() {
        return 0.;
    }

    /*public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass());
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmptyValue that = (EmptyValue) o;

        return blah == that.blah;

    }

    @Override
    public int hashCode() {
        return blah;
    }

    @Override
    public String toString() {
        return "(Empty)";
    }

    public int compareTo(Value value) {
        return 0;
    }
}
