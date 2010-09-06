package com.easyinsight.api.v2;

import java.io.Serializable;

/**
 * User: jamesboe
 * Date: Sep 3, 2010
 * Time: 12:39:41 PM
 */
public class SerialRow implements Serializable {
    private SerialStringValue[] stringValues;
    private SerialNumericValue[] numberValues;
    private SerialDateValue[] dateValues;
    private static final long serialVersionUID = 5215604172273795175L;


    public SerialStringValue[] getStringValues() {
        return stringValues;
    }

    public void setStringValues(SerialStringValue[] stringValues) {
        this.stringValues = stringValues;
    }

    public SerialNumericValue[] getNumberValues() {
        return numberValues;
    }

    public void setNumberValues(SerialNumericValue[] numberValues) {
        this.numberValues = numberValues;
    }

    public SerialDateValue[] getDateValues() {
        return dateValues;
    }

    public void setDateValues(SerialDateValue[] dateValues) {
        this.dateValues = dateValues;
    }
}
