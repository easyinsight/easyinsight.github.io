package com.easyinsight.webservice;

import java.util.Date;

/**
 * User: James Boe
 * Date: Aug 12, 2008
 * Time: 6:30:26 PM
 */
public class WSDateValue extends WSListValue {
    private Date value;

    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }
}
