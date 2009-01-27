package com.easyinsight.scrubbing;

import com.easyinsight.core.Key;

/**
 * User: James Boe
 * Date: Jul 30, 2008
 * Time: 12:04:39 PM
 */
public class ScrubPair {
    private Key key;
    private String value;

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
