package com.easyinsight.core;

import java.util.Set;
import java.util.HashSet;

/**
 * User: James Boe
 * Date: Jul 16, 2008
 * Time: 12:49:27 AM
 */
public class PersistableValueFactory {
    public static PersistableValue fromValue(Value value) {
        PersistableValue persistableValue;
        switch (value.type()) {
            case Value.STRING:
                persistableValue = new PersistableStringValue(((StringValue) value).getValue());
                break;
            case Value.NUMBER:
                persistableValue = new PersistableNumericValue(((NumericValue) value).getValue());
                break;
            case Value.DATE:
                persistableValue = new PersistableDateValue(((DateValue) value).getDate());
                break;
            case Value.EMPTY:
                persistableValue = new PersistableEmptyValue();
                break;
            default:
                throw new RuntimeException();
        }
        return persistableValue;
    }

    public static Set<PersistableValue> fromValue(Set<Value> values) {
        Set<PersistableValue> persistableValues = new HashSet<PersistableValue>();
        for (Value value : values) {
            persistableValues.add(fromValue(value));
        }
        return persistableValues;
    }
}
