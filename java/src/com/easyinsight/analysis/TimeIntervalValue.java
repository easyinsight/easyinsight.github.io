package com.easyinsight.analysis;

import com.easyinsight.core.Value;

import java.io.Serializable;

/**
* User: jamesboe
* Date: 12/2/11
* Time: 6:06 PM
*/
public class TimeIntervalValue implements Serializable {
    private Value dateValue;
    private Value value;

    public Value getDateValue() {
        return dateValue;
    }

    public void setDateValue(Value dateValue) {
        this.dateValue = dateValue;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }
}
