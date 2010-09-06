package com.easyinsight.api.v2;

/**
 * User: jamesboe
 * Date: Sep 3, 2010
 * Time: 12:48:16 PM
 */
public class SerialNumericValue extends SerialValue {
    private double value;
    private static final long serialVersionUID = -7540324903647505637L;


    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
