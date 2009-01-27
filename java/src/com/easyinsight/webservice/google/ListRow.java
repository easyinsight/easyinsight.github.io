package com.easyinsight.webservice.google;

import com.easyinsight.core.Value;

import java.io.Serializable;

/**
 * User: James Boe
 * Date: Jan 9, 2008
 * Time: 9:04:57 PM
 */
public class ListRow implements Serializable {
    private Value[] values;

    public Value[] getValues() {
        return values;
    }

    public void setValues(Value[] values) {
        this.values = values;
    }
}
