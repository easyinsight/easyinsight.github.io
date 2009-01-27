package com.easyinsight.webservice;

/**
 * User: James Boe
 * Date: Aug 12, 2008
 * Time: 6:30:34 PM
 */
public class WSNumericValue extends WSListValue {
    private double value;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
