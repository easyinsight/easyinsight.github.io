package com.easyinsight.dataset;

import com.easyinsight.core.Value;

import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;

/**
 * User: James Boe
 * Date: May 11, 2008
 * Time: 3:17:04 PM
 */
public class ColumnSegment implements Externalizable {
    private Value[] values;

    public ColumnSegment() {
    }

    public ColumnSegment(Value[] values) {
        this.values = values;
    }

    public Value[] getValues() {
        return values;
    }

    public void setValues(Value[] values) {
        this.values = values;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(values);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        values = (Value[]) in.readObject();
    }
}
