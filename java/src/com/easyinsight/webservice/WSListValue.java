package com.easyinsight.webservice;

import java.util.Date;

/**
 * User: James Boe
 * Date: Aug 12, 2008
 * Time: 6:21:06 PM
 */
public class WSListValue {
    private int type;
    private String stringValue;
    private Date dateValue;
    private double doubleValue;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }
}
