package com.easyinsight.api.v2;

/**
 * User: jamesboe
 * Date: Sep 3, 2010
 * Time: 12:40:03 PM
 */
public class SerialStringValue extends SerialValue {
    private String value;
    private static final long serialVersionUID = 6510146780055488213L;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
