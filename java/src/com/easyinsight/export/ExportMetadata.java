package com.easyinsight.export;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

/**
* User: jamesboe
* Date: 7/6/12
* Time: 11:30 AM
*/
public class ExportMetadata implements Serializable {
    public int dateFormat;
    public String currencySymbol;
    public Locale locale;
    public Calendar cal;
    public String accountTimezone;

    ExportMetadata(int dateFormat, String currencySymbol, Calendar cal, Locale locale, String accountTimezone) {
        this.dateFormat = dateFormat;
        this.currencySymbol = currencySymbol;
        this.locale = locale;
        this.cal = cal;
        this.accountTimezone = accountTimezone;
    }
}
