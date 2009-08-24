package com.easyinsight.analysis;

import com.easyinsight.core.Value;

import java.io.Serializable;

/**
 * User: James Boe
 * Date: Jan 9, 2008
 * Time: 9:04:57 PM
 */
public class ListRow implements Serializable {
    private Value[] values;
    private String[] urls;

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

    public Value[] getValues() {
        return values;
    }

    public void setValues(Value[] values) {
        this.values = values;
    }
}
